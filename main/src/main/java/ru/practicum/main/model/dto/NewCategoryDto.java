package ru.practicum.main.model.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class NewCategoryDto {

    @NotBlank(message = "Name isn't empty.")
    private String name;

}
