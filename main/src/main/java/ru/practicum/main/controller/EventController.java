package ru.practicum.main.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.model.AdminUpdateEventRequest;
import ru.practicum.main.model.SortEvent;
import ru.practicum.main.model.State;
import ru.practicum.main.model.dto.EventFullDto;
import ru.practicum.main.model.dto.EventShortDto;
import ru.practicum.main.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PutMapping("/admin/events/{eventId}")
    public ResponseEntity<EventFullDto> editEvent(@RequestBody AdminUpdateEventRequest updateEvent,
                                                  @PathVariable(name = "eventId") int eventId) {
        return ResponseEntity.ok(eventService.editEvent(updateEvent, eventId));
    }

    @PatchMapping("/admin/events/{eventId}/publish")
    public ResponseEntity<EventFullDto> publishEvent(@PathVariable(name = "eventId") int eventId) {
        return ResponseEntity.ok(eventService.publishEvent(eventId));
    }

    @PatchMapping("/admin/events/{eventId}/reject")
    public ResponseEntity<EventFullDto> rejectEvent(@PathVariable(name = "eventId") int eventId) {
        return ResponseEntity.ok(eventService.rejectEvent(eventId));
    }

    @GetMapping("/admin/events")
    public ResponseEntity<Page<EventFullDto>> getEventsForAdmin(
            @RequestParam(required = false) Integer[] users,
            @RequestParam(required = false) State[] states,
            @RequestParam(required = false) Integer[] categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                eventService.findEventsForAdmin(users, states, categories, rangeStart, rangeEnd, from, size)
        );
    }

    @GetMapping("/events")
    public ResponseEntity<Page<EventShortDto>> getEventsForUser(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) Integer[] categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) SortEvent sort,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(eventService.findEventsForUser(
                        text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request
                )
        );
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<EventFullDto> getEventForUser(@PathVariable(name = "id") int id,
                                                        HttpServletRequest request) {
        return ResponseEntity.ok(eventService.findEventForUser(id, request));
    }
}
