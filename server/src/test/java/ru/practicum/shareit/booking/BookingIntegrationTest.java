package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookingIntegrationTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BookingMapper bookingMapper;

    @Test
    public void givenBooking_whenAdd_thenStatus200andBookingReturned() throws Exception {
        User user = createTestUser();
        User owner = createTestOwner();
        Item item = createTestItem(owner);
        BookingRequestDto bookingRequestDto = new BookingRequestDto(item.getId(),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2));

        BookingDto bookingDto = new BookingDto(
                1L,
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").format(bookingRequestDto.getStart()),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").format(bookingRequestDto.getEnd()),
                "WAITING",
                userMapper.userToUserDto(user),
                bookingMapper.itemToBookingItemDto(item));
        mockMvc.perform(
                        post("/bookings")
                                .content(objectMapper.writeValueAsString(bookingRequestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", user.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDto)));
    }

    private Item createTestItem(User user) {
        Item item = new Item(null, "Item", "Description", true, user, null, null, null);
        return itemRepository.save(item);
    }

    private User createTestUser() {
        User user = new User(null, "User", "user@email.com");
        return userRepository.save(user);
    }

    private User createTestOwner() {
        User user = new User(null, "Owner", "owner@email.com");
        return userRepository.save(user);
    }
}
