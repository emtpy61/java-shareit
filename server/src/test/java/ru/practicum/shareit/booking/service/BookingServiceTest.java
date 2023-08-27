package ru.practicum.shareit.booking.service;

import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.mapper.BookingMapperImpl;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.common.ecxeption.ItemNotAvailbaleException;
import ru.practicum.shareit.common.ecxeption.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
class BookingServiceTest {

    private static Booking booking;
    private static User user;
    private static User booker;
    private static Item item;
    private static Item itemNotAvailable;
    private static BookingRequestDto bookingRequestDto;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private BookingServiceImpl bookingService;
    @Autowired
    private BookingMapperImpl bookingMapper;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(bookingService, "bookingMapper", bookingMapper);
        user = new User(1L, "User", "email@email.com");
        booker = new User(2L, "User2", "email2@email.com");
        item = new Item(1L, "Example item", "Example item", true, user, null,
                null, null);
        itemNotAvailable = new Item(2L, "Example item", "Example item", false, user, null,
                null, null);
        booking = new Booking(1L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), item, booker,
                BookingStatus.WAITING);
        bookingRequestDto = new BookingRequestDto(item.getId(), booking.getStart(), booking.getEnd());
    }

    @Test
    public void testCreateAndGetBooking() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        BookingDto createdBooking = bookingService.createBooking(bookingRequestDto, booker.getId());
        BookingDto retrievedBooking = bookingService.getBookingById(createdBooking.getId(), booker.getId());
        assertThat(retrievedBooking).isEqualTo(createdBooking);
    }

    @Test
    public void testCreateBookingWithEndBeforeStart() {
        bookingRequestDto.setStart(LocalDateTime.now().plusDays(2));
        bookingRequestDto.setEnd(LocalDateTime.now().plusDays(1));

        assertThatThrownBy(() -> bookingService.createBooking(bookingRequestDto, 1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testCreateBookingWithNotAvailableItem() {
        bookingRequestDto.setItemId(itemNotAvailable.getId());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(itemNotAvailable));

        assertThatThrownBy(() -> bookingService.createBooking(bookingRequestDto, 2L))
                .isInstanceOf(ItemNotAvailbaleException.class);
    }

    @Test
    public void testCreateBookingWithOwnItem() {
        bookingRequestDto.setItemId(itemNotAvailable.getId());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> bookingService.createBooking(bookingRequestDto, 1L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void testUpdateBooking() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        BookingDto updatedBooking = bookingService.updateBooking(1L, true, 1L);
        assertThat(updatedBooking.getStatus()).isEqualTo("APPROVED");
    }

    @Test
    public void testUpdateBookingWithInvalidUser() {
        when(userRepository.findById(eq(1L))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.updateBooking(1L, true, 1L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void testUpdateBookingWithNonOwnerUser() {
        when(userRepository.findById(eq(2L))).thenReturn(Optional.of(booker));
        when(bookingRepository.findById(eq(1L))).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.updateBooking(1L, true, 2L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void testUpdateBookingWithApprovedBooking() {
        booking.setStatus(BookingStatus.APPROVED);
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));
        when(bookingRepository.findById(eq(1L))).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.updateBooking(1L, true, 1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testGetBookingByIdWithInvalidUser() {
        when(userRepository.findById(eq(1L))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.getBookingById(1L, 1L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void testGetBookingByIdWithUnauthorizedUser() {
        when(userRepository.findById(eq(3L))).thenReturn(
                Optional.of(new User(3L, "Unauthorized", "unauthorized@example.com")));
        when(bookingRepository.findById(eq(1L))).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.getBookingById(1L, 3L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void testGetBookingsAllState() {
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));
        when(bookingRepository.findAll(any(Predicate.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDto> bookings = bookingService.getBookingsByBooker("ALL", 1L, 0, 10);

        assertThat(bookings.size()).isEqualTo(1);
    }

    @Test
    public void testGetBookingsCurrentState() {
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));
        when(bookingRepository.findAll(any(Predicate.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDto> bookings = bookingService.getBookingsByBooker("CURRENT", 1L, 0, 10);

        assertThat(bookings.size()).isEqualTo(1);
    }

    @Test
    public void testGetBookingsPastState() {
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));
        when(bookingRepository.findAll(any(Predicate.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDto> bookings = bookingService.getBookingsByBooker("PAST", 1L, 0, 10);

        assertThat(bookings.size()).isEqualTo(1);
    }

    @Test
    public void testGetBookingsFutureState() {
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));
        when(bookingRepository.findAll(any(Predicate.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDto> bookings = bookingService.getBookingsByBooker("FUTURE", 1L, 0, 10);

        assertThat(bookings.size()).isEqualTo(1);
    }

    @Test
    public void testGetBookingsWaitingState() {
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));
        when(bookingRepository.findAll(any(Predicate.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDto> bookings = bookingService.getBookingsByBooker("WAITING", 1L, 0, 10);

        assertThat(bookings.size()).isEqualTo(1);
    }

    @Test
    public void testGetBookingsRejectedState() {
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));
        when(bookingRepository.findAll(any(Predicate.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDto> bookings = bookingService.getBookingsByBooker("REJECTED", 1L, 0, 10);

        assertThat(bookings.size()).isEqualTo(1);
    }

    @Test
    public void testGetBookingsByOwnerAllState() {
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));
        when(bookingRepository.findAll(any(Predicate.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDto> bookings = bookingService.getBookingsByOwner("ALL", 1L, 0, 10);

        assertThat(bookings.size()).isEqualTo(1);
    }

    @Test
    public void testGetBookingsByOwnerCurrentState() {
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));
        when(bookingRepository.findAll(any(Predicate.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDto> bookings = bookingService.getBookingsByOwner("CURRENT", 1L, 0, 10);

        assertThat(bookings.size()).isEqualTo(1);
    }

    @Test
    public void testGetBookingsByOwnerPastState() {
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));
        when(bookingRepository.findAll(any(Predicate.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDto> bookings = bookingService.getBookingsByOwner("PAST", 1L, 0, 10);

        assertThat(bookings.size()).isEqualTo(1);
    }

    @Test
    public void testGetBookingsByOwnerFutureState() {
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));
        when(bookingRepository.findAll(any(Predicate.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDto> bookings = bookingService.getBookingsByOwner("FUTURE", 1L, 0, 10);

        assertThat(bookings.size()).isEqualTo(1);
    }

    @Test
    public void testGetBookingsByOwnerWaitingState() {
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));
        when(bookingRepository.findAll(any(Predicate.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDto> bookings = bookingService.getBookingsByOwner("WAITING", 1L, 0, 10);

        assertThat(bookings.size()).isEqualTo(1);
    }

    @Test
    public void testGetBookingsByOwnerRejectedState() {
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));
        when(bookingRepository.findAll(any(Predicate.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDto> bookings = bookingService.getBookingsByOwner("REJECTED", 1L, 0, 10);

        assertThat(bookings.size()).isEqualTo(1);
    }
}