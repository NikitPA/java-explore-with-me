package ru.practicum.main.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main.CustomLocalDateTimeSerializer;
import ru.practicum.main.model.Status;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequestDto {

    private int id;

    private int event;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime created;

    private int requester;

    private Status status;

}
