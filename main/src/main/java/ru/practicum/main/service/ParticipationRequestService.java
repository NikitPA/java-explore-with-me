package ru.practicum.main.service;

import ru.practicum.main.model.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {

    List<ParticipationRequestDto> getInformationRequests(int userId, int eventId);

    ParticipationRequestDto confirmRequest(int userId, int eventId, int reqId);

    ParticipationRequestDto rejectRequest(int userId, int eventId, int reqId);

    ParticipationRequestDto createRequest(int userId, int eventId);

    List<ParticipationRequestDto> findRequestsUser(int userId);

    ParticipationRequestDto cancelYourParticipationRequest(int userId, int reqId);

}
