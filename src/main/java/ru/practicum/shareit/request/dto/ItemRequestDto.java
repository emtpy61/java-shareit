package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ItemRequestDto {

    private Long id;
    @NotNull
    @NotBlank
    private String description;
    @NotNull
    @NotBlank
    private Integer requesterId;
    private LocalDateTime created;
}
