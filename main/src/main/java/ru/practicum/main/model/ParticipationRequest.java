package ru.practicum.main.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests", uniqueConstraints = {@UniqueConstraint(columnNames = {"event_id", "requester_id"})})
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class ParticipationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Event event;

    @Column(nullable = false)
    private LocalDateTime created;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private User requester;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Status status;

}
