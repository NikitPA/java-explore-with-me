package ru.practicum.main.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class CompilationDto {

    private Integer id;
    private boolean pinned;
    private String title;
    private Set<EventShortDto> events;

}
