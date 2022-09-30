package ru.practicum.main.model.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class CompilationDto {

    private Integer id;
    private boolean pinned;
    private String title;
    private Set<EventShortDto> events;

}
