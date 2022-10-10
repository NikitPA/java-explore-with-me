package ru.practicum.main.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.model.UpdateEventRequest;
import ru.practicum.main.model.dto.EventFullDto;
import ru.practicum.main.model.dto.EventShortDto;
import ru.practicum.main.model.dto.NewEventDto;
import ru.practicum.main.service.EventForUserService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class EventForUserController {

    private final EventForUserService eventForUserService;

    @GetMapping("/{userId}/events")
    public ResponseEntity<Page<EventShortDto>> getUserEvents(
            @PathVariable(name = "userId") int userId,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(eventForUserService.findUserEvents(userId, from, size));
    }

    @PatchMapping("/{userId}/events")
    public ResponseEntity<EventFullDto> changeEvent(@PathVariable(name = "userId") int userId,
                                                    @Valid @RequestBody UpdateEventRequest updateEventRequest
    ) {
        return ResponseEntity.ok(eventForUserService.changeEvent(userId, updateEventRequest));
    }

    @PostMapping("/{userId}/events")
    public ResponseEntity<EventFullDto> createEvent(@PathVariable(name = "userId") int userId,
                                                    @Valid @RequestBody NewEventDto newEventDto
    ) {
        return ResponseEntity.ok(eventForUserService.createEvent(userId, newEventDto));
    }

    @GetMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> getEvent(@PathVariable(name = "userId") int userId,
                                                 @PathVariable(name = "eventId") int eventId
    ) {
        return ResponseEntity.ok(eventForUserService.findEvent(userId, eventId));
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> cancelEvent(@PathVariable(name = "userId") int userId,
                                                    @PathVariable(name = "eventId") int eventId
    ) {
        return ResponseEntity.ok(eventForUserService.cancelEvent(userId, eventId));
    }

    @GetMapping("/{userId}/events/subscriptions")
    public ResponseEntity<Page<EventFullDto>> getEventsOnUserSubscriptions(
            @PathVariable(name = "userId") Integer userId,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(eventForUserService.getEventsOnUserSubscriptions(userId, from, size));
    }

}
