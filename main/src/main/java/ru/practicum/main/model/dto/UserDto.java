package ru.practicum.main.model.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class UserDto {

    private Integer id;

    @NotBlank
    private String name;

    @Email
    @NotNull
    private String email;

    private boolean isSubscribe = false;

}
