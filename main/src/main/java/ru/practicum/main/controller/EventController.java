package ru.practicum.main.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.model.AdminUpdateEventRequest;
import ru.practicum.main.model.SortEvent;
import ru.practicum.main.model.State;
import ru.practicum.main.model.dto.EventFullDto;
import ru.practicum.main.model.dto.EventShortDto;
import ru.practicum.main.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PutMapping("/admin/events/{eventId}")
    public ResponseEntity<EventFullDto> editEvent(@RequestBody AdminUpdateEventRequest updateEvent,
                                                  @PathVariable(name = "eventId") int eventId) {
        return new ResponseEntity<>(eventService.editEvent(updateEvent, eventId), HttpStatus.OK);
    }

    @PatchMapping("/admin/events/{eventId}/publish")
    public ResponseEntity<EventFullDto> publishEvent(@PathVariable(name = "eventId") int eventId) {
        return new ResponseEntity<>(eventService.publishEvent(eventId), HttpStatus.OK);
    }

    @PatchMapping("/admin/events/{eventId}/reject")
    public ResponseEntity<EventFullDto> rejectEvent(@PathVariable(name = "eventId") int eventId) {
        return new ResponseEntity<>(eventService.rejectEvent(eventId), HttpStatus.OK);
    }

    @GetMapping("/admin/events")
    public ResponseEntity<List<EventFullDto>> getEventsForAdmin(
            @RequestParam(required = false) Integer[] users,
            @RequestParam(required = false) State[] states,
            @RequestParam(required = false) Integer[] categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        return new ResponseEntity<>(
                eventService.findEventsForAdmin(users, states, categories, rangeStart, rangeEnd, from, size), HttpStatus.OK
        );
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventShortDto>> getEventsForUser(
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
        return new ResponseEntity<>(
                eventService.findEventsForUser(
                        text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request
                ), HttpStatus.OK
        );
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<EventFullDto> getEventForUser(@PathVariable(name = "id") int id,
                                                        HttpServletRequest request) {
        return new ResponseEntity<>(eventService.findEventForUser(id, request), HttpStatus.OK);
    }
}
