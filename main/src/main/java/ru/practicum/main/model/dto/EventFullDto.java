package ru.practicum.main.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main.CustomLocalDateTimeSerializer;
import ru.practicum.main.model.Location;
import ru.practicum.main.model.State;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EventFullDto {

    private int id;

    private String annotation;

    private CategoryDto category;

    private int confirmedRequests;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime createdOn;

    private String description;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime eventDate;

    private UserShortDto initiator;

    private Location location;

    private boolean paid;

    private int participantLimit = 0;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime publishedOn;

    private boolean requestModeration = true;

    private State state;

    private String title;

    private int views;

}
