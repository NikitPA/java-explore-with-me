package ru.practicum.main.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests", uniqueConstraints = {@UniqueConstraint(columnNames = {"event_id", "requester_id"})})
@Data
@NoArgsConstructor
public class ParticipationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Event event;

    private LocalDateTime created;

    @ManyToOne
    private User requester;

    @Enumerated(EnumType.STRING)
    private Status status;

}
