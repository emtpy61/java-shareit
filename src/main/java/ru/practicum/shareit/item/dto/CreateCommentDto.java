package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class CreateCommentDto {
    @NotBlank
    private String text;
    private String authorName;
}
