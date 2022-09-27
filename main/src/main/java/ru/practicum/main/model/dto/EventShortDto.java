package ru.practicum.main.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main.CustomLocalDateTimeSerializer;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EventShortDto {

    private int id;

    @NotNull
    private String annotation;

    @NotNull
    private CategoryDto category;

    private int confirmedRequests;

    @NotNull
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime eventDate;

    @NotNull
    private UserShortDto initiator;

    private boolean paid;

    @NotNull
    private String title;

    private int views;

}
