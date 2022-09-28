package ru.practicum.main.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.model.dto.ParticipationRequestDto;
import ru.practicum.main.service.ParticipationRequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class ParticipationRequestController {

    private final ParticipationRequestService requestService;

    @GetMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getInformationRequests(
            @PathVariable(name = "userId") int userId,
            @PathVariable(name = "eventId") int eventId
    ) {
        return new ResponseEntity<>(requestService.getInformationRequests(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ResponseEntity<ParticipationRequestDto> confirmRequest(@PathVariable(name = "userId") int userId,
                                                                  @PathVariable(name = "eventId") int eventId,
                                                                  @PathVariable(name = "reqId") int reqId
    ) {
        return new ResponseEntity<>(requestService.confirmRequest(userId, eventId, reqId), HttpStatus.OK);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ResponseEntity<ParticipationRequestDto> rejectRequest(@PathVariable(name = "userId") int userId,
                                                                 @PathVariable(name = "eventId") int eventId,
                                                                 @PathVariable(name = "reqId") int reqId
    ) {
        return new ResponseEntity<>(requestService.rejectRequest(userId, eventId, reqId), HttpStatus.OK);
    }

    @PostMapping("/{userId}/requests")
    public ResponseEntity<ParticipationRequestDto> createRequest(@PathVariable(name = "userId") int userId,
                                                                 @RequestParam(name = "eventId") int eventId
    ) {
        return new ResponseEntity<>(requestService.createRequest(userId, eventId), HttpStatus.OK);
    }

    @GetMapping("/{userId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getRequestsUser(@PathVariable(name = "userId") int userId
    ) {
        return new ResponseEntity<>(requestService.findRequestsUser(userId), HttpStatus.OK);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelYourParticipationRequest(
            @PathVariable(name = "userId") int userId,
            @PathVariable(name = "requestId") int reqId
    ) {
        return new ResponseEntity<>(requestService.cancelYourParticipationRequest(userId, reqId), HttpStatus.OK);
    }

}
