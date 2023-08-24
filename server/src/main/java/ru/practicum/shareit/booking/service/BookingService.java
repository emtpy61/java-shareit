package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(BookingRequestDto bookingDto, Long userId);

    BookingDto updateBooking(Long bookingId, boolean approved, Long userId);

    BookingDto getBookingById(Long bookingId, Long userId);

    List<BookingDto> getBookingsByBooker(String state, Long userId, int from, int size);

    List<BookingDto> getBookingsByOwner(String state, Long userId, int from, int size);
}
