package ru.practicum.main.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.ParticipationRequest;
import ru.practicum.main.model.State;
import ru.practicum.main.model.Status;
import ru.practicum.main.model.User;
import ru.practicum.main.model.dto.ParticipationRequestDto;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.repository.ParticipationRequestRepository;
import ru.practicum.main.repository.UserRepository;
import ru.practicum.main.service.ParticipationRequestService;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ParticipationRequestRepository requestRepository;
    private final int noLimit = 0;

    @Override
    public List<ParticipationRequestDto> getInformationRequests(int userId, int eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("User {0} not found", userId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("Event {0} not found.", eventId)));
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

    @Transactional
    @Override
    public ParticipationRequestDto confirmRequest(int userId, int eventId, int reqId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("User {0} not found", userId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("Event {0} not found.", eventId)));
        ParticipationRequest request = requestRepository.findById(reqId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("Request {0} not found.", reqId)));
        if (user.getId() == event.getInitiator().getId() && request.getStatus().equals(Status.PENDING)) {
            return setStatusRequest(event, request);
        }
        throw new IllegalArgumentException("Event isn't belong User.");
    }

    @Transactional
    @Override
    public ParticipationRequestDto rejectRequest(int userId, int eventId, int reqId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("User {0} not found", userId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("Event {0} not found.", eventId)));
        ParticipationRequest request = requestRepository.findById(reqId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("Request {0} not found.", reqId)));
        if (!request.getStatus().equals(Status.REJECTED) && user.getId() == event.getInitiator().getId()) {
            request.setStatus(Status.REJECTED);
            ParticipationRequest saveRequest = requestRepository.save(request);
            return new ParticipationRequestDto(saveRequest.getId(), saveRequest.getEvent().getId(),
                    saveRequest.getCreated(), saveRequest.getRequester().getId(), saveRequest.getStatus());
        }
        throw new IllegalArgumentException("Request has already been rejected.");
    }

    @Transactional
    @Override
    public ParticipationRequestDto createRequest(int userId, int eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("User {0} not found", userId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("Event {0} not found.", eventId)));
        if (user.getId() != event.getInitiator().getId()) {
            if (event.getState().equals(State.PUBLISHED)) {
                if (event.getParticipantLimit() > event.getConfirmedRequests() ||
                        event.getParticipantLimit() == noLimit) {
                    ParticipationRequest saveRequest = requestRepository.save(createRequest(user, event));
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
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("User {0} not found", userId)));
        List<ParticipationRequest> requests = requestRepository.findAllByRequester(user);
        return requests.stream()
                .map(request -> new ParticipationRequestDto(request.getId(), request.getEvent().getId(),
                        request.getCreated(), request.getRequester().getId(), request.getStatus())
                )
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelYourParticipationRequest(int userId, int reqId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("User {0} not found", userId)));
        ParticipationRequest request = requestRepository.findById(reqId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("Request {0} not found.", reqId)));
        if (user.getId() == request.getRequester().getId()) {
            request.setStatus(Status.CANCELED);
            ParticipationRequest saveRequest = requestRepository.save(request);
            return new ParticipationRequestDto(saveRequest.getId(), saveRequest.getEvent().getId(),
                    saveRequest.getCreated(), saveRequest.getRequester().getId(), saveRequest.getStatus());
        }
        throw new IllegalArgumentException("Request isn't belong User.");
    }

    private ParticipationRequestDto setStatusRequest(Event event, ParticipationRequest request) {
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
            rejectRequestAtTheEndOfTheLimit(saveEvent);
            return new ParticipationRequestDto(saveRequest.getId(), saveRequest.getEvent().getId(),
                    saveRequest.getCreated(), saveRequest.getRequester().getId(), saveRequest.getStatus());
        }
        throw new IllegalArgumentException("Incorrect guest limit value");
    }

    private void rejectRequestAtTheEndOfTheLimit(Event saveEvent) {
        if (saveEvent.getParticipantLimit() == saveEvent.getConfirmedRequests()) {
            List<ParticipationRequest> requests = requestRepository
                    .findAllByEventAndStatus(saveEvent, State.PENDING);
            for (ParticipationRequest req : requests) {
                req.setStatus(Status.REJECTED);
                requestRepository.save(req);
            }
        }
    }

    private ParticipationRequest createRequest(User user, Event event) {
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
        return participationRequest;
    }

}
