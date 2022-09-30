package ru.practicum.main.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import ru.practicum.main.CustomLocalDateTimeSerializer;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class EventShortDto {

    private int id;

    private String annotation;

    private CategoryDto category;

    private int confirmedRequests;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime eventDate;

    private UserShortDto initiator;

    private boolean paid;

    private String title;

    private int views;

}
