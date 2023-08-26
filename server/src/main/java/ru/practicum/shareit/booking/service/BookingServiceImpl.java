package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.QBooking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.common.ecxeption.ItemNotAvailbaleException;
import ru.practicum.shareit.common.ecxeption.NotFoundException;
import ru.practicum.shareit.common.ecxeption.UnsupportedStateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static ru.practicum.shareit.common.ecxeption.NotFoundException.notFoundException;

@Service
@Slf4j
@AllArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public BookingDto createBooking(BookingRequestDto bookingRequestDto, Long userId) {
        if (bookingRequestDto.getEnd().isBefore(bookingRequestDto.getStart()) ||
                bookingRequestDto.getEnd().isEqual(bookingRequestDto.getStart())) {
            throw new IllegalArgumentException("Время конца не может быть раньше начала.");
        }
        Long itemId = bookingRequestDto.getItemId();
        User user = getUser(userId);
        Item item = getItem(itemId);
        if (Objects.equals(item.getOwner().getId(), userId)) {
            throw new NotFoundException("Нельзя забронировать свою вещь.");
        }
        if (!item.getAvailable()) {
            throw new ItemNotAvailbaleException("Вещь недоступна для бронирования");
        }
        Booking booking = bookingMapper.bookingRequestDtoToBooking(bookingRequestDto);
        booking.setBooker(user);
        booking.setItem(item);
        booking = bookingRepository.save(booking);
        return bookingMapper.bookingToBookingDto(booking);
    }

    @Transactional
    @Override
    public BookingDto updateBooking(Long bookingId, boolean approved, Long userId) {
        getUser(userId);
        Booking booking = getBooking(bookingId);
        Long ownerId = booking.getItem().getOwner().getId();
        if (!Objects.equals(ownerId, userId)) {
            String message = String.format("Нельзя поменять статус. Вещь не принадлежит полюзователю %s", userId);
            throw new NotFoundException(message);
        }
        if (Objects.equals(booking.getStatus(), BookingStatus.APPROVED)) {
            throw new IllegalArgumentException("Нельзя поменять статус.");
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        booking = bookingRepository.save(booking);
        return bookingMapper.bookingToBookingDto(booking);
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        getUser(userId);
        Booking booking = getBooking(bookingId);
        Long ownerId = booking.getItem().getOwner().getId();
        Long bookerId = booking.getBooker().getId();
        if (!Objects.equals(ownerId, userId) && !Objects.equals(bookerId, userId)) {
            throw new NotFoundException("Может быть выполнено либо автором бронирования, либо владельцем вещи.");
        }
        return bookingMapper.bookingToBookingDto(booking);
    }

    @Override
    public List<BookingDto> getBookingsByBooker(String state, Long userId, int from, int size) {
        getUser(userId);
        QBooking booking = QBooking.booking;
        Pageable page = PageRequest.of(from / size, size, Sort.by("start").descending());
        Pageable curpage = PageRequest.of(from / size, size, Sort.by("start").ascending());
        Iterable<Booking> bookings;

        switch (state) {
            case "ALL":
                bookings = bookingRepository.findAll(booking.booker.id.eq(userId), page);
                break;
            case "CURRENT":
                bookings = bookingRepository.findAll(
                        booking.booker.id.eq(userId)
                                .and(booking.start.before(LocalDateTime.now()))
                                .and(booking.end.after(LocalDateTime.now())), curpage);
                break;
            case "PAST":
                bookings = bookingRepository.findAll(
                        booking.booker.id.eq(userId)
                                .and(booking.end.before(LocalDateTime.now())), page);
                break;
            case "FUTURE":
                bookings = bookingRepository.findAll(
                        booking.booker.id.eq(userId)
                                .and(booking.start.after(LocalDateTime.now())), page);
                break;
            case "WAITING":
                bookings = bookingRepository.findAll(
                        booking.booker.id.eq(userId)
                                .and(booking.status.eq(BookingStatus.WAITING)), page);
                break;
            case "REJECTED":
                bookings = bookingRepository.findAll(
                        booking.booker.id.eq(userId)
                                .and(booking.status.eq(BookingStatus.REJECTED)), page);
                break;
            default:
                throw new UnsupportedStateException("Unknown state: " + state);
        }
        return bookingMapper.bookingsToBookingDtos(bookings);
    }

    @Override
    public List<BookingDto> getBookingsByOwner(String state, Long userId, int from, int size) {
        getUser(userId);
        QBooking booking = QBooking.booking;
        Pageable page = PageRequest.of(from / size, size, Sort.by("start").descending());
        Pageable curpage = PageRequest.of(from / size, size, Sort.by("start").ascending());
        Iterable<Booking> bookings;

        switch (state) {
            case "ALL":
                bookings = bookingRepository.findAll(booking.item.owner.id.eq(userId), page);
                break;
            case "CURRENT":
                bookings = bookingRepository.findAll(
                        booking.item.owner.id.eq(userId)
                                .and(booking.start.before(LocalDateTime.now()))
                                .and(booking.end.after(LocalDateTime.now())), curpage);
                break;
            case "PAST":
                bookings = bookingRepository.findAll(
                        booking.item.owner.id.eq(userId)
                                .and(booking.end.before(LocalDateTime.now())), page);
                break;
            case "FUTURE":
                bookings = bookingRepository.findAll(
                        booking.item.owner.id.eq(userId)
                                .and(booking.start.after(LocalDateTime.now())), page);
                break;
            case "WAITING":
                bookings = bookingRepository.findAll(
                        booking.item.owner.id.eq(userId)
                                .and(booking.status.eq(BookingStatus.WAITING)), page);
                break;
            case "REJECTED":
                bookings = bookingRepository.findAll(
                        booking.item.owner.id.eq(userId)
                                .and(booking.status.eq(BookingStatus.REJECTED)), page);
                break;
            default:
                throw new UnsupportedStateException("Unknown state: " + state);
        }
        return bookingMapper.bookingsToBookingDtos(bookings);
    }

    private Item getItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(notFoundException("Вещь с id = {0} не найдена.", itemId));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(notFoundException("Пользователь с id = {0} не найден.", userId));
    }

    private Booking getBooking(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(notFoundException("Бронирование с id = {0} не найдено.", bookingId));
    }
}
