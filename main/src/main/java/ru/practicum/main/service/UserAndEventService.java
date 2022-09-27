package ru.practicum.main.service;

import ru.practicum.main.model.dto.*;
import ru.practicum.main.model.UpdateEventRequest;

import java.util.List;

public interface UserAndEventService {

    List<EventShortDto> findUserEvents(int userId, int from, int size);

    EventFullDto changeEvent(int userId, UpdateEventRequest updateEventRequest);

    EventFullDto createEvent(int userId, NewEventDto newEventDto);

    EventFullDto findEvent(int userId, int eventId);

    EventFullDto cancelEvent(int userId, int eventId);

    List<ParticipationRequestDto> getInformationRequests(int userId, int eventId);

    ParticipationRequestDto confirmRequest(int userId, int eventId, int reqId);

    ParticipationRequestDto rejectRequest(int userId, int eventId, int reqId);

    ParticipationRequestDto createRequest(int userId, int eventId);

    List<ParticipationRequestDto> findRequestsUser(int userId);

    ParticipationRequestDto cancelYourParticipationRequest(int userId, int reqId);
}
