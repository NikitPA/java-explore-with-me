package ru.practicum.main.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "events")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(nullable = false)
    @Size(min = 20, max = 2000)
    private String annotation;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Category category;

    private int confirmedRequests;

    private LocalDateTime createdOn;

    @Column(nullable = false)
    @Size(min = 20, max = 7000)
    private String description;

    private LocalDateTime eventDate;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private User initiator;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Location location;

    private boolean paid;

    private int participantLimit;

    private LocalDateTime publishedOn;

    private boolean requestModeration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private State state;

    @Column(nullable = false)
    @Size(min = 3, max = 120)
    private String title;

    @ManyToMany(mappedBy = "events", cascade = CascadeType.ALL)
    private List<Compilation> compilations;

    private int views;

    public void addView() {
        views++;
    }

}
