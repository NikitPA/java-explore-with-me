package ru.practicum.main.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
public class NewCompilationDto {

    private boolean pinned = false;

    @NotBlank(message = "Title isn't empty")
    private String title;

    private List<Integer> events;

}
