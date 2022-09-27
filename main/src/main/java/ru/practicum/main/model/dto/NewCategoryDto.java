package ru.practicum.main.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class NewCategoryDto {

    @NotBlank(message = "Name isn't empty.")
    private String name;
}
