package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class ItemDto {
    private Integer id;
    @NotNull(message = "не может быть null.")
    @NotBlank(message = "не может быть пустым.")
    private String name;
    @NotNull(message = "не может быть null.")
    @NotBlank(message = "не может быть пустым.")
    private String description;
    @NotNull(message = "не может быть null.")
    private Boolean available;
    private Integer ownerId;
    private Integer requestId;
}
