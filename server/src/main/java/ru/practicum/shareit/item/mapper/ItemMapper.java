package ru.practicum.shareit.item.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


@Mapper(componentModel = "spring",
        uses = {CommentMapper.class})
public interface ItemMapper {


    @Mapping(target = "nextBooking", expression = "java(bookingToItemBookingDto(getNextBooking(item,userId)))")
    @Mapping(target = "lastBooking", expression = "java(bookingToItemBookingDto(getLastBooking(item,userId)))")
    @Mapping(target = "requestId", source = "item.request.id")
    @Mapping(target = "ownerId", source = "item.owner.id")
    ItemDto itemToItemDto(Item item, @Context Long userId);


    @Mapping(target = "request", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    Item createItemDtoToItem(CreateItemDto createItemDto);

    List<ItemDto> itemsToItemDtos(List<Item> items, @Context Long userId);

    @Mapping(target = "bookerId", source = "booker.id")
    ItemBookingDto bookingToItemBookingDto(Booking booking);

    default Booking getLastBooking(Item item, Long userId) {
        if (!Objects.equals(item.getOwner().getId(), userId)) {
            return null;
        }
        if (item.getBookings() == null) {
            return null;
        }
        return item.getBookings().stream()
                .filter(booking -> booking.getStatus().equals(BookingStatus.APPROVED))
                .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                .max(Comparator.comparing(Booking::getStart)).orElse(null);
    }

    default Booking getNextBooking(Item item, Long userId) {
        if (!Objects.equals(item.getOwner().getId(), userId)) {
            return null;
        }
        if (item.getBookings() == null) {
            return null;
        }
        return item.getBookings().stream()
                .filter(booking -> booking.getStatus().equals(BookingStatus.APPROVED))
                .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                .min(Comparator.comparing(Booking::getStart)).orElse(null);
    }
}
