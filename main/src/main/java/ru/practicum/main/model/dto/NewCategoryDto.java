package ru.practicum.main.model.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class NewCategoryDto {

    @NotBlank(message = "Name isn't empty.")
    private String name;

}
