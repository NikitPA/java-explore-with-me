package ru.practicum.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.ParticipationRequest;
import ru.practicum.main.model.State;
import ru.practicum.main.model.User;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Integer> {

    List<ParticipationRequest> findAllByRequesterAndEvent(User requester, Event event);

    List<ParticipationRequest> findAllByEventAndStatus(Event event, State state);

    List<ParticipationRequest> findAllByRequester(User user);

    List<ParticipationRequest> findAllByEvent(Event event);

}
