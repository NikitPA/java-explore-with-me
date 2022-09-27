package ru.practicum.main.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main.exception.EventNotFoundException;
import ru.practicum.main.exception.RequestNotFoundException;
import ru.practicum.main.exception.UserNotFoundException;
import ru.practicum.main.model.*;
import ru.practicum.main.model.dto.EventFullDto;
import ru.practicum.main.model.dto.EventShortDto;
import ru.practicum.main.model.dto.NewEventDto;
import ru.practicum.main.model.dto.ParticipationRequestDto;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.repository.LocationRepository;
import ru.practicum.main.repository.ParticipationRequestRepository;
import ru.practicum.main.repository.UserRepository;
import ru.practicum.main.service.CategoryService;
import ru.practicum.main.service.UserAndEventService;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAndEventServiceImpl implements UserAndEventService {

    private final CategoryService categoryService;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final ParticipationRequestRepository requestRepository;
    private final ModelMapper mapper;

    @Override
    public List<EventShortDto> findUserEvents(int userId, int from, int size) {
        int page = from / size;
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        List<Event> events = eventRepository.findAllByInitiator(user, PageRequest.of(page, size));
        return events.stream()
                .map(event -> mapper.map(event, EventShortDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto changeEvent(int userId, UpdateEventRequest updateEventRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository
                .findById(updateEventRequest.getEventId()).orElseThrow(
                        () -> new EventNotFoundException(updateEventRequest.getEventId())
                );
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

    @Override
    public EventFullDto createEvent(int userId, NewEventDto newEventDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Category category = categoryService.findCategory(newEventDto.getCategory());
        Location location = locationRepository
                .findAllByLatAndLon(newEventDto.getLocation().getLat(), newEventDto.getLocation().getLon())
                .orElseGet(() -> locationRepository.save(newEventDto.getLocation()));
        LocalDateTime eventDate = newEventDto.getEventDate();
        LocalDateTime createdOn = LocalDateTime.now();
        if (eventDate.isAfter(createdOn.plusHours(2))) {
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
                .orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        if (user.getId() == event.getInitiator().getId()) {
            return mapper.map(event, EventFullDto.class);
        }
        throw new IllegalArgumentException(
                MessageFormat.format("Event {0} isn't belong User {1}.", eventId, userId)
        );
    }

    @Override
    public EventFullDto cancelEvent(int userId, int eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
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
    public List<ParticipationRequestDto> getInformationRequests(int userId, int eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        if (user.getId() == event.getInitiator().getId()) {
            List<ParticipationRequest> requests = requestRepository.findAllByEvent(event);
            return requests.stream()
                    .map(request -> new ParticipationRequestDto(request.getId(), request.getEvent().getId(),
                            request.getCreated(), request.getRequester().getId(), request.getStatus())
                    )
                    .collect(Collectors.toList());
        }
        throw new IllegalArgumentException("Event isn't belong User.");
    }

    @Override
    public ParticipationRequestDto confirmRequest(int userId, int eventId, int reqId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        ParticipationRequest request = requestRepository.findById(reqId)
                .orElseThrow(() -> new RequestNotFoundException(reqId));
        if (user.getId() == event.getInitiator().getId() && request.getStatus().equals(Status.PENDING)) {
            if (event.getParticipantLimit() == 0 && !event.isRequestModeration()) {
                request.setStatus(Status.CONFIRMED);
                ParticipationRequest saveRequest = requestRepository.save(request);
                return new ParticipationRequestDto(saveRequest.getId(), saveRequest.getEvent().getId(),
                        saveRequest.getCreated(), saveRequest.getRequester().getId(), saveRequest.getStatus());
            } else if (event.getParticipantLimit() > event.getConfirmedRequests()) {
                request.setStatus(Status.CONFIRMED);
                int confirmedRequests = event.getConfirmedRequests();
                event.setConfirmedRequests(++confirmedRequests);
                Event saveEvent = eventRepository.save(event);
                ParticipationRequest saveRequest = requestRepository.save(request);
                if (saveEvent.getParticipantLimit() == saveEvent.getConfirmedRequests()) {
                    List<ParticipationRequest> requests = requestRepository
                            .findAllByEventAndStatus(saveEvent, State.PENDING);
                    for (ParticipationRequest req : requests) {
                        req.setStatus(Status.REJECTED);
                        requestRepository.save(req);
                    }
                }
                return new ParticipationRequestDto(saveRequest.getId(), saveRequest.getEvent().getId(),
                        saveRequest.getCreated(), saveRequest.getRequester().getId(), saveRequest.getStatus());
            }
        }
        throw new IllegalArgumentException("Event isn't belong User.");
    }

    @Override
    public ParticipationRequestDto rejectRequest(int userId, int eventId, int reqId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        ParticipationRequest request = requestRepository.findById(reqId)
                .orElseThrow(() -> new RequestNotFoundException(reqId));
        if (!request.getStatus().equals(Status.REJECTED) && user.getId() == event.getInitiator().getId()) {
            request.setStatus(Status.REJECTED);
            ParticipationRequest saveRequest = requestRepository.save(request);
            return new ParticipationRequestDto(saveRequest.getId(), saveRequest.getEvent().getId(),
                    saveRequest.getCreated(), saveRequest.getRequester().getId(), saveRequest.getStatus());
        }
        throw new IllegalArgumentException("Request has already been rejected.");
    }

    @Override
    public ParticipationRequestDto createRequest(int userId, int eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        if (user.getId() != event.getInitiator().getId()) {
            if (event.getState().equals(State.PUBLISHED)) {
                if (event.getParticipantLimit() > event.getConfirmedRequests() || event.getParticipantLimit() == 0) {
                    ParticipationRequest participationRequest = new ParticipationRequest();
                    participationRequest.setRequester(user);
                    participationRequest.setEvent(event);
                    participationRequest.setCreated(LocalDateTime.now());
                    if (!event.isRequestModeration()) {
                        participationRequest.setStatus(Status.CONFIRMED);
                        int confirmedRequests = event.getConfirmedRequests();
                        event.setConfirmedRequests(++confirmedRequests);
                        eventRepository.save(event);
                    } else {
                        participationRequest.setStatus(Status.PENDING);
                    }
                    ParticipationRequest saveRequest = requestRepository.save(participationRequest);
                    return new ParticipationRequestDto(saveRequest.getId(), saveRequest.getEvent().getId(),
                            saveRequest.getCreated(), saveRequest.getRequester().getId(), saveRequest.getStatus());
                }
                throw new IllegalArgumentException("Event has reached the limit of participation requests");
            }
            throw new IllegalArgumentException("Cannot participate in an unpublished event");
        }
        throw new IllegalArgumentException("InitiatorEvent can't add request to participate in his event.");
    }

    @Override
    public List<ParticipationRequestDto> findRequestsUser(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        List<ParticipationRequest> requests = requestRepository.findAllByRequester(user);
        return requests.stream()
                .map(request -> new ParticipationRequestDto(request.getId(), request.getEvent().getId(),
                        request.getCreated(), request.getRequester().getId(), request.getStatus())
                )
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto cancelYourParticipationRequest(int userId, int reqId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        ParticipationRequest request = requestRepository.findById(reqId)
                .orElseThrow(() -> new RequestNotFoundException(reqId));
        if (user.getId() == request.getRequester().getId()) {
            request.setStatus(Status.CANCELED);
            ParticipationRequest saveRequest = requestRepository.save(request);
            return new ParticipationRequestDto(saveRequest.getId(), saveRequest.getEvent().getId(),
                    saveRequest.getCreated(), saveRequest.getRequester().getId(), saveRequest.getStatus());
        }
        throw new IllegalArgumentException("Request isn't belong User.");
    }

}
