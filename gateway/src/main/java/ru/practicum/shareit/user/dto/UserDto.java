package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String name;
    @NotNull(message = "не может быть пустым.")
    @Email(message = "не является адресом электронной почты.")
    private String email;
}