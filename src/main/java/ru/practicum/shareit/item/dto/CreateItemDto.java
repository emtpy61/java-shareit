package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class CreateItemDto {
    @NotNull(message = "не может быть null.")
    @NotBlank(message = "не может быть пустым.")
    private String name;
    @NotNull(message = "не может быть null.")
    @NotBlank(message = "не может быть пустым.")
    private String description;
    @NotNull(message = "не может быть null.")
    private Boolean available;
    private Long requestId;
}
