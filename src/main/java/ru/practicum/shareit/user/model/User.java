package ru.practicum.shareit.user.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.common.repository.GenericEntity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Data
public class User extends GenericEntity {

    private String name;
    @NotNull(message = "не может быть пустым.")
    @Email(message = "не является адресом электронной почты.")
    private String email;
}
