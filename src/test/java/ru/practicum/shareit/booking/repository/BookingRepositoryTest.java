package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Test
    public void testHasCompletedBookingsForItem() {
        User booker = new User(1L, "User", "email@email.com");
        ItemRequest itemRequest = new ItemRequest(1L, "Description", booker, LocalDateTime.now(), null);
        Item item = new Item(1L, "Example item", "Example item", true, booker, itemRequest,
                null, null);

        userRepository.save(booker);
        itemRepository.save(item);

        Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now().minusHours(2));
        booking.setEnd(LocalDateTime.now().minusHours(1));

        bookingRepository.save(booking);

        boolean result = bookingRepository.hasCompletedBookingsForItem(
                booker, item, BookingStatus.APPROVED, LocalDateTime.now());

        assertThat(result).isTrue();
    }
}