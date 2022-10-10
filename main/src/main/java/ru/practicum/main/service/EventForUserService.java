package ru.practicum.main.service;

import org.springframework.data.domain.Page;
import ru.practicum.main.model.UpdateEventRequest;
import ru.practicum.main.model.dto.EventFullDto;
import ru.practicum.main.model.dto.EventShortDto;
import ru.practicum.main.model.dto.NewEventDto;

public interface EventForUserService {

    Page<EventShortDto> findUserEvents(int userId, int from, int size);

    EventFullDto changeEvent(int userId, UpdateEventRequest updateEventRequest);

    EventFullDto createEvent(int userId, NewEventDto newEventDto);

    EventFullDto findEvent(int userId, int eventId);

    EventFullDto cancelEvent(int userId, int eventId);

    Page<EventFullDto> getEventsOnUserSubscriptions(int userId, int from, int size);

}
