package ru.practicum.main.service;

import org.springframework.data.domain.Page;
import ru.practicum.main.model.AdminUpdateEventRequest;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.SortEvent;
import ru.practicum.main.model.State;
import ru.practicum.main.model.dto.EventFullDto;
import ru.practicum.main.model.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

public interface EventService {

    EventFullDto editEvent(AdminUpdateEventRequest updateEvent, int eventId);

    EventFullDto publishEvent(int eventId);

    EventFullDto rejectEvent(int eventId);

    Page<EventFullDto> findEventsForAdmin(
            Integer[] users, State[] states, Integer[] categories, LocalDateTime rangeStart,
            LocalDateTime rangeEnd, int from, int size
    );

    Event findById(int eventId);

    Page<EventShortDto> findEventsForUser(
            String text, Integer[] categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd,
            Boolean onlyAvailable, SortEvent sortEvent, int from, int size, HttpServletRequest request
    );

    EventFullDto findEventForUser(int id, HttpServletRequest request);

}
