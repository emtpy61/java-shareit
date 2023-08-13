package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class BookingRequestDto {

    @NotNull(message = "не может быть null.")
    private Long itemId;
    @NotNull(message = "не может быть null.")
    @FutureOrPresent(message = "не может быть в прошлом.")
    private LocalDateTime start;
    @NotNull(message = "не может быть null.")
    @FutureOrPresent(message = "не может быть в прошлом.")
    private LocalDateTime end;
}
