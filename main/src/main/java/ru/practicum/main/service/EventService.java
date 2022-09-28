package ru.practicum.main.service;

import ru.practicum.main.model.*;
import ru.practicum.main.model.dto.EventFullDto;
import ru.practicum.main.model.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    EventFullDto editEvent(AdminUpdateEventRequest updateEvent, int eventId);

    EventFullDto publishEvent(int eventId);

    EventFullDto rejectEvent(int eventId);

    List<EventFullDto> findEventsForAdmin(
            Integer[] users, State[] states, Integer[] categories, LocalDateTime rangeStart,
            LocalDateTime rangeEnd, int from, int size
    );

    Event findById(int eventId);

    List<EventShortDto> findEventsForUser(
            String text, Integer[] categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd,
            Boolean onlyAvailable, SortEvent sortEvent, int from, int size, HttpServletRequest request
    );

    EventFullDto findEventForUser(int id, HttpServletRequest request);

}
