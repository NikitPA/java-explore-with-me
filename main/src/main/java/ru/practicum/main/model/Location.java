package ru.practicum.main.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "locations", uniqueConstraints = {@UniqueConstraint(columnNames = {"lat", "lon"})})
@Data
@NoArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private float lat;

    private float lon;

}
