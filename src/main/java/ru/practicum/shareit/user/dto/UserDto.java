package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String name;
    @NotNull(message = "не может быть пустым.")
    @Email(message = "не является адресом электронной почты.")
    private String email;
}