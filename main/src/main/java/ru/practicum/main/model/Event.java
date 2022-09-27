package ru.practicum.main.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 2000)
    private String annotation;

    @ManyToOne
    private Category category;

    private int confirmedRequests;

    private LocalDateTime createdOn;

    @Column(length = 7000)
    private String description;

    private LocalDateTime eventDate;

    @ManyToOne
    private User initiator;

    @ManyToOne
    private Location location;

    private boolean paid;

    private int participantLimit;

    private LocalDateTime publishedOn;

    private boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private State state;

    @Column(length = 120)
    private String title;

    private int views;

    @ManyToMany(mappedBy = "events")
    private List<Compilation> compilations;

}
