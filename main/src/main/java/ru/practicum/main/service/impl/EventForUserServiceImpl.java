package ru.practicum.main.service.impl;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.model.Category;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.Location;
import ru.practicum.main.model.QEvent;
import ru.practicum.main.model.State;
import ru.practicum.main.model.UpdateEventRequest;
import ru.practicum.main.model.User;
import ru.practicum.main.model.dto.EventFullDto;
import ru.practicum.main.model.dto.EventShortDto;
import ru.practicum.main.model.dto.NewEventDto;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.repository.LocationRepository;
import ru.practicum.main.repository.UserRepository;
import ru.practicum.main.service.CategoryService;
import ru.practicum.main.service.EventForUserService;
import ru.practicum.main.util.QPredicates;

import java.text.MessageFormat;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventForUserServiceImpl implements EventForUserService {

    private final CategoryService categoryService;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final ModelMapper mapper;
    private final int noEarlierThanTwoHours = 2;

    @Override
    public Page<EventShortDto> findUserEvents(int userId, int from, int size) {
        int page = from / size;
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("User {0} not found", userId)));
        return eventRepository
                .findAllByInitiator(user, PageRequest.of(page, size))
                .map(event -> mapper.map(event, EventShortDto.class));
    }

    @Transactional
    @Override
    public EventFullDto changeEvent(int userId, UpdateEventRequest updateEventRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("User {0} not found", userId)));
        Event event = eventRepository
                .findById(updateEventRequest.getEventId()).orElseThrow(
                        () -> new NotFoundException(
                                MessageFormat.format("Event {0} not found.", updateEventRequest.getEventId())
                        ));
        if (!event.getState().equals(State.PUBLISHED) && user.getId() == event.getInitiator().getId()) {
            if (updateEventRequest.getAnnotation() != null) {
                event.setAnnotation(updateEventRequest.getAnnotation());
            }
            if (updateEventRequest.getCategory() != null) {
                Category category = categoryService.findCategory(updateEventRequest.getCategory());
                event.setCategory(category);
            }
            if (updateEventRequest.getDescription() != null) {
                event.setDescription(updateEventRequest.getDescription());
            }
            LocalDateTime eventDate = updateEventRequest.getEventDate();
            if (eventDate != null && eventDate.isAfter(LocalDateTime.now().plusHours(2))) {
                event.setEventDate(updateEventRequest.getEventDate());
            }
            if (updateEventRequest.getPaid() != null) {
                event.setPaid(updateEventRequest.getPaid());
            }
            if (updateEventRequest.getParticipantLimit() != null) {
                event.setParticipantLimit(updateEventRequest.getParticipantLimit());
            }
            if (updateEventRequest.getTitle() != null) {
                event.setTitle(updateEventRequest.getTitle());
            }
            if (event.getState().equals(State.CANCELED)) {
                event.setState(State.PENDING);
            }
            Event saveEvent = eventRepository.save(event);
            return mapper.map(saveEvent, EventFullDto.class);
        }
        throw new IllegalArgumentException(
                MessageFormat.format("Event {0} should not be published", event.getId())
        );
    }

    @Transactional
    @Override
    public EventFullDto createEvent(int userId, NewEventDto newEventDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("User {0} not found", userId)));
        Category category = categoryService.findCategory(newEventDto.getCategory());
        Location location = locationRepository
                .findAllByLatAndLon(newEventDto.getLocation().getLat(), newEventDto.getLocation().getLon())
                .orElseGet(() -> locationRepository.save(newEventDto.getLocation()));
        LocalDateTime eventDate = newEventDto.getEventDate();
        LocalDateTime createdOn = LocalDateTime.now();
        if (eventDate.isAfter(createdOn.plusHours(noEarlierThanTwoHours))) {
            Event event = mapper.map(newEventDto, Event.class);
            event.setInitiator(user);
            event.setCreatedOn(createdOn);
            event.setState(State.PENDING);
            event.setCategory(category);
            event.setLocation(location);
            Event saveEvent = eventRepository.save(event);
            return mapper.map(saveEvent, EventFullDto.class);
        }
        throw new IllegalArgumentException(
                MessageFormat.format(
                        "Date of the event ({0}) cannot be earlier than two hours from the current moment ({1}).",
                        eventDate, createdOn
                )
        );
    }

    @Override
    public EventFullDto findEvent(int userId, int eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("User {0} not found", userId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("Event {0} not found.", eventId)));
        if (user.getId() == event.getInitiator().getId()) {
            return mapper.map(event, EventFullDto.class);
        }
        throw new IllegalArgumentException(
                MessageFormat.format("Event {0} isn't belong User {1}.", eventId, userId)
        );
    }

    @Transactional
    @Override
    public EventFullDto cancelEvent(int userId, int eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("User {0} not found", userId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("Event {0} not found.", eventId)));
        if (user.getId() == event.getInitiator().getId() && event.getState().equals(State.PENDING)) {
            event.setState(State.CANCELED);
            Event saveEvent = eventRepository.save(event);
            return mapper.map(saveEvent, EventFullDto.class);
        }
        throw new IllegalArgumentException(
                MessageFormat.format(
                        "Event {0} isn't belong User {1} or EventState isn't PENDING", eventId, userId
                )
        );
    }

    @Override
    public Page<EventFullDto> getEventsOnUserSubscriptions(int userId, int from, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("User {0} not found", userId)));
        Predicate predicate = QPredicates.builder()
                .add(user.getSubscriptions(), QEvent.event.initiator::in)
                .add(State.PUBLISHED, QEvent.event.state::eq)
                .buildAnd();
        return eventRepository
                .findAll(predicate, PageRequest.of(from / size, size))
                .map(event -> mapper.map(event, EventFullDto.class));
    }

}
