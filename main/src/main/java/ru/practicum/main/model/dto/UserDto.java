package ru.practicum.main.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class UserDto {

    private int id;

    @NotBlank
    private String name;

    @Email
    @NotNull
    private String email;

}
