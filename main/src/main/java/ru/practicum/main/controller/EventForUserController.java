package ru.practicum.main.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.model.UpdateEventRequest;
import ru.practicum.main.model.dto.EventFullDto;
import ru.practicum.main.model.dto.EventShortDto;
import ru.practicum.main.model.dto.NewEventDto;
import ru.practicum.main.service.EventForUserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class EventForUserController {

    private final EventForUserService eventForUserService;

    @GetMapping("/{userId}/events")
    public ResponseEntity<List<EventShortDto>> getUserEvents(
            @PathVariable(name = "userId") int userId,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return new ResponseEntity<>(eventForUserService.findUserEvents(userId, from, size), HttpStatus.OK);
    }

    @PatchMapping("/{userId}/events")
    public ResponseEntity<EventFullDto> changeEvent(@PathVariable(name = "userId") int userId,
                                                    @Valid @RequestBody UpdateEventRequest updateEventRequest
    ) {
        return new ResponseEntity<>(eventForUserService.changeEvent(userId, updateEventRequest), HttpStatus.OK);
    }

    @PostMapping("/{userId}/events")
    public ResponseEntity<EventFullDto> createEvent(@PathVariable(name = "userId") int userId,
                                                    @Valid @RequestBody NewEventDto newEventDto
    ) {
        return new ResponseEntity<>(eventForUserService.createEvent(userId, newEventDto), HttpStatus.OK);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> getEvent(@PathVariable(name = "userId") int userId,
                                                 @PathVariable(name = "eventId") int eventId
    ) {
        return new ResponseEntity<>(eventForUserService.findEvent(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> cancelEvent(@PathVariable(name = "userId") int userId,
                                                    @PathVariable(name = "eventId") int eventId
    ) {
        return new ResponseEntity<>(eventForUserService.cancelEvent(userId, eventId), HttpStatus.OK);
    }

}
