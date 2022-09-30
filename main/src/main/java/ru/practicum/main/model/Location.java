package ru.practicum.main.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "locations", uniqueConstraints = {@UniqueConstraint(columnNames = {"lat", "lon"})})
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(nullable = false)
    private float lat;

    @Column(nullable = false)
    private float lon;

}
