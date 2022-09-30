package ru.practicum.main.model.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class NewCompilationDto {

    private boolean pinned = false;

    @NotBlank(message = "Title isn't empty")
    private String title;

    private Set<Integer> events;

}
