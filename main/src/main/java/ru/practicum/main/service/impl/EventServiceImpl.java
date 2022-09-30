package ru.practicum.main.service.impl;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.client.EventClient;
import ru.practicum.main.exception.EventNotFoundException;
import ru.practicum.main.model.*;
import ru.practicum.main.model.dto.EndpointHit;
import ru.practicum.main.model.dto.EventFullDto;
import ru.practicum.main.model.dto.EventShortDto;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.service.CategoryService;
import ru.practicum.main.service.EventService;
import ru.practicum.main.service.UserService;
import ru.practicum.main.util.QPredicates;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.main.model.QEvent.event;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoryService categoryService;
    private final UserService userService;
    private final EventClient eventClient;
    private final ModelMapper mapper;
    private final int anHourBeforePublication = 1;

    @Transactional
    @Override
    public EventFullDto editEvent(AdminUpdateEventRequest updateEvent, int eventId) {
        Event event = findById(eventId);
        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            event.setCategory(categoryService.findCategory(updateEvent.getCategory()));
        }
        if (updateEvent.getDescription() != null) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getEventDate() != null) {
            event.setEventDate(updateEvent.getEventDate());
        }
        if (updateEvent.getLocation() != null) {
            event.setLocation(updateEvent.getLocation());
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getTitle() != null) {
            event.setTitle(updateEvent.getTitle());
        }
        Event saveEvent = eventRepository.save(event);
        return mapper.map(saveEvent, EventFullDto.class);
    }

    @Transactional
    @Override
    public EventFullDto publishEvent(int eventId) {
        LocalDateTime datePublished = LocalDateTime.now();
        Event event = findById(eventId);
        State eventState = event.getState();
        LocalDateTime eventDate = event.getEventDate();
        if (eventDate.isAfter(datePublished.plusHours(anHourBeforePublication))
                && eventState.equals(State.PENDING)) {
            event.setState(State.PUBLISHED);
            event.setPublishedOn(datePublished);
            eventRepository.save(event);
            return mapper.map(event, EventFullDto.class);
        }
        throw new IllegalArgumentException(
                MessageFormat.format("StateEvent ({0}) isn't PENDING or the publication date " +
                        "should be an hour later than the start dateEvent ({1})", eventState, eventDate)
        );
    }

    @Transactional
    @Override
    public EventFullDto rejectEvent(int eventId) {
        Event event = findById(eventId);
        State eventState = event.getState();
        if (eventState.equals(State.PENDING)) {
            event.setState(State.CANCELED);
            eventRepository.save(event);
            return mapper.map(event, EventFullDto.class);
        }
        throw new IllegalArgumentException(
                MessageFormat.format("StateEvent ({0}) isn't PENDING.", eventState)
        );
    }

    @Override
    public List<EventFullDto> findEventsForAdmin(
            Integer[] users, State[] states, Integer[] categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size
    ) {
        int page = from / size;
        List<User> usersForIds = userService.findUsersForIds(users);
        List<Category> categoryForIds = categoryService.findCategoryForIds(categories);
        Predicate predicate = QPredicates.builder()
                .add(usersForIds, event.initiator::in)
                .add(states, event.state::in)
                .add(categoryForIds, event.category::in)
                .add(rangeStart, event.publishedOn::after)
                .add(rangeEnd, event.publishedOn::before)
                .buildAnd();
        Page<Event> events = eventRepository.findAll(predicate, PageRequest.of(page, size));
        return events.stream()
                .map(event -> mapper.map(event, EventFullDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Event findById(int eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
    }

    @Override
    public List<EventShortDto> findEventsForUser(
            String text, Integer[] categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd,
            Boolean onlyAvailable, SortEvent sort, int from, int size, HttpServletRequest request
    ) {
        int page = from / size;
        List<Category> categoryForIds = categoryService.findCategoryForIds(categories);
        QPredicates qPredicates = QPredicates.builder()
                .add(text, event.annotation::containsIgnoreCase)
                .add(categoryForIds, event.category::in)
                .add(State.PUBLISHED, event.state::eq)
                .add(paid, event.paid::eq)
                .add(rangeStart, event.publishedOn::after)
                .add(rangeEnd, event.publishedOn::before);
        if (onlyAvailable) {
            qPredicates.add(event.confirmedRequests, event.participantLimit::lt);
        } else {
            qPredicates.add(event.confirmedRequests, event.participantLimit::goe);
        }
        Predicate predicate = qPredicates.buildAnd();
        Page<Event> events = eventRepository.findAll(
                predicate, PageRequest.of(page, size, sort.equals(SortEvent.EVENT_DATE) ?
                        Sort.by("eventDate").descending() :
                        Sort.by("views").descending()));
        saveStatistic(request);
        return events.stream()
                .map(event -> mapper.map(event, EventShortDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto findEventForUser(int id, HttpServletRequest request) {
        Event event = eventRepository.findEventByIdAndState(id, State.PUBLISHED)
                .orElseThrow(() -> new EventNotFoundException(id));
        event.addView();
        saveStatistic(request);
        return mapper.map(event, EventFullDto.class);
    }

    private void saveStatistic(HttpServletRequest request) {
        EndpointHit endpointHit = new EndpointHit(
                null, "main-service", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now()
        );
        eventClient.saveEndpointHit(endpointHit);
    }

}
