package ru.practicum.main.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.model.UpdateEventRequest;
import ru.practicum.main.model.dto.EventFullDto;
import ru.practicum.main.model.dto.EventShortDto;
import ru.practicum.main.model.dto.NewEventDto;
import ru.practicum.main.model.dto.ParticipationRequestDto;
import ru.practicum.main.service.UserAndEventService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserAndEventController {

    private final UserAndEventService userAndEventService;

    @GetMapping("/{userId}/events")
    public ResponseEntity<List<EventShortDto>> getUserEvents(
            @PathVariable(name = "userId") int userId,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return new ResponseEntity<>(userAndEventService.findUserEvents(userId, from, size), HttpStatus.OK);
    }

    @PatchMapping("/{userId}/events")
    public ResponseEntity<EventFullDto> changeEvent(@PathVariable(name = "userId") int userId,
                                                    @Valid @RequestBody UpdateEventRequest updateEventRequest
    ) {
        return new ResponseEntity<>(userAndEventService.changeEvent(userId, updateEventRequest), HttpStatus.OK);
    }

    @PostMapping("/{userId}/events")
    public ResponseEntity<EventFullDto> createEvent(@PathVariable(name = "userId") int userId,
                                                    @Valid @RequestBody NewEventDto newEventDto
    ) {
        return new ResponseEntity<>(userAndEventService.createEvent(userId, newEventDto), HttpStatus.OK);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> getEvent(@PathVariable(name = "userId") int userId,
                                                 @PathVariable(name = "eventId") int eventId
    ) {
        return new ResponseEntity<>(userAndEventService.findEvent(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> cancelEvent(@PathVariable(name = "userId") int userId,
                                                    @PathVariable(name = "eventId") int eventId
    ) {
        return new ResponseEntity<>(userAndEventService.cancelEvent(userId, eventId), HttpStatus.OK);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getInformationRequests(
            @PathVariable(name = "userId") int userId,
            @PathVariable(name = "eventId") int eventId
    ) {
        return new ResponseEntity<>(userAndEventService.getInformationRequests(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ResponseEntity<ParticipationRequestDto> confirmRequest(@PathVariable(name = "userId") int userId,
                                                                  @PathVariable(name = "eventId") int eventId,
                                                                  @PathVariable(name = "reqId") int reqId
    ) {
        return new ResponseEntity<>(userAndEventService.confirmRequest(userId, eventId, reqId), HttpStatus.OK);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ResponseEntity<ParticipationRequestDto> rejectRequest(@PathVariable(name = "userId") int userId,
                                                                 @PathVariable(name = "eventId") int eventId,
                                                                 @PathVariable(name = "reqId") int reqId
    ) {
        return new ResponseEntity<>(userAndEventService.rejectRequest(userId, eventId, reqId), HttpStatus.OK);
    }

    @PostMapping("/{userId}/requests")
    public ResponseEntity<ParticipationRequestDto> createRequest(@PathVariable(name = "userId") int userId,
                                                                 @RequestParam(name = "eventId") int eventId
    ) {
        return new ResponseEntity<>(userAndEventService.createRequest(userId, eventId), HttpStatus.OK);
    }

    @GetMapping("/{userId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getRequestsUser(@PathVariable(name = "userId") int userId
    ) {
        return new ResponseEntity<>(userAndEventService.findRequestsUser(userId), HttpStatus.OK);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelYourParticipationRequest(
            @PathVariable(name = "userId") int userId,
            @PathVariable(name = "requestId") int reqId
    ) {
        return new ResponseEntity<>(userAndEventService.cancelYourParticipationRequest(userId, reqId), HttpStatus.OK);
    }

}
