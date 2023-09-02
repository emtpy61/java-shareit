package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookItemRequestDto {
    private long itemId;
    @NotNull(message = "не может быть null.")
    @FutureOrPresent(message = "не может быть в прошлом.")
    private LocalDateTime start;

    @NotNull(message = "не может быть null.")
    @Future(message = "не может быть в прошлом.")
    private LocalDateTime end;
}
