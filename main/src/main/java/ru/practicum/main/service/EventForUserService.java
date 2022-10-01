package ru.practicum.main.service;

import ru.practicum.main.model.dto.*;
import ru.practicum.main.model.UpdateEventRequest;

import java.util.List;

public interface EventForUserService {

    List<EventShortDto> findUserEvents(int userId, int from, int size);

    EventFullDto changeEvent(int userId, UpdateEventRequest updateEventRequest);

    EventFullDto createEvent(int userId, NewEventDto newEventDto);

    EventFullDto findEvent(int userId, int eventId);

    EventFullDto cancelEvent(int userId, int eventId);

    List<EventFullDto> getEventsOnUserSubscriptions(int userId, int from, int size);
}
