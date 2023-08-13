package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring",
        uses = {ItemMapper.class,
                UserMapper.class})
public interface BookingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "booker", ignore = true)
    @Mapping(target = "status", constant = "WAITING")
    @Mapping(target = "item.id", source = "itemId")
    Booking bookingRequestDtoToBooking(BookingRequestDto bookingRequestDto);

    BookingDto bookingToBookingDto(Booking booking);

    List<BookingDto> bookingsToBookingDtos(Iterable<Booking> bookings);

    BookingItemDto itemToBookingItemDto(Item item);

    default String bookingStatusToString(BookingStatus status) {
        return status == null ? null : status.name();
    }

    default String localDateTimeToString(LocalDateTime localDateTime) {
        return DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                .format(localDateTime);
    }

    default LocalDateTime stringToLocalDateTime(String stringTime) {
        return LocalDateTime.parse(stringTime);
    }
}
