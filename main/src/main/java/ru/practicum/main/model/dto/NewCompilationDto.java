package ru.practicum.main.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@NoArgsConstructor
public class NewCompilationDto {

    private boolean pinned = false;

    @NotBlank(message = "Title isn't empty")
    private String title;

    private Set<Integer> events;

}
