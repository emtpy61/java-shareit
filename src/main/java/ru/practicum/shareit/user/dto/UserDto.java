package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class UserDto {

    private Integer id;
    private String name;
    @NotNull(message = "не может быть пустым.")
    @Email(message = "не является адресом электронной почты.")
    private String email;
}