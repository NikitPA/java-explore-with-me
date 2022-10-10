package ru.practicum.main.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main.CustomLocalDateTimeSerializer;
import ru.practicum.main.model.Status;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
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
