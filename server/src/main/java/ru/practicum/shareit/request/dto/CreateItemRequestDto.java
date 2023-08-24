package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateItemRequestDto {
    @NotNull(message = "не может быть null.")
    @NotBlank(message = "не может быть пустым.")
    private String description;
}
