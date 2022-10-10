package ru.practicum.main.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
        return ResponseEntity.ok(requestService.getInformationRequests(userId, eventId));
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ResponseEntity<ParticipationRequestDto> confirmRequest(@PathVariable(name = "userId") int userId,
                                                                  @PathVariable(name = "eventId") int eventId,
                                                                  @PathVariable(name = "reqId") int reqId
    ) {
        return ResponseEntity.ok(requestService.confirmRequest(userId, eventId, reqId));
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ResponseEntity<ParticipationRequestDto> rejectRequest(@PathVariable(name = "userId") int userId,
                                                                 @PathVariable(name = "eventId") int eventId,
                                                                 @PathVariable(name = "reqId") int reqId
    ) {
        return ResponseEntity.ok(requestService.rejectRequest(userId, eventId, reqId));
    }

    @PostMapping("/{userId}/requests")
    public ResponseEntity<ParticipationRequestDto> createRequest(@PathVariable(name = "userId") int userId,
                                                                 @RequestParam(name = "eventId") int eventId
    ) {
        return ResponseEntity.ok(requestService.createRequest(userId, eventId));
    }

    @GetMapping("/{userId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getRequestsUser(@PathVariable(name = "userId") int userId
    ) {
        return ResponseEntity.ok(requestService.findRequestsUser(userId));
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelYourParticipationRequest(
            @PathVariable(name = "userId") int userId,
            @PathVariable(name = "requestId") int reqId
    ) {
        return ResponseEntity.ok(requestService.cancelYourParticipationRequest(userId, reqId));
    }

}
