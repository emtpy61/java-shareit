package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {
    private static BookingRequestDto requestDto;
    private static BookingRequestDto requestDtoInPast;
    private static UserDto booker;
    private static BookingItemDto item;
    private static BookingDto responseDto;
    private static List<BookingDto> responseDtoList;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    @Autowired
    private MockMvc mvc;
    @MockBean
    private BookingService bookingService;

    @BeforeEach
    public void setUp() {
        requestDto = new BookingRequestDto(
                1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2));
        requestDtoInPast = new BookingRequestDto(
                1L,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().minusDays(2));
        booker = new UserDto(1L, "name", "email@email.com");
        item = new BookingItemDto(1L, "Отвертка");
        responseDto = new BookingDto(
                1L,
                requestDto.getStart().toString(),
                requestDto.getEnd().toString(),
                "WAITING", booker, item);
        responseDtoList = new ArrayList<>();
        responseDtoList.add(responseDto);
    }

    @Test
    void testCreateBooking() throws Exception {
        Mockito.when(this.bookingService.createBooking(any(BookingRequestDto.class), anyLong()))
                .thenReturn(responseDto);

        mvc.perform(
                        post("/bookings")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 123L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
        verify(bookingService,times(1)).createBooking(any(),anyLong());
    }

    @Test
    void testCreateBookingWithWrongDate() throws Exception {
        Mockito.when(this.bookingService.createBooking(any(BookingRequestDto.class), anyLong()))
                .thenReturn(responseDto);

        mvc.perform(
                        post("/bookings")
                                .content(objectMapper.writeValueAsString(requestDtoInPast))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 123L))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass()
                        .equals(MethodArgumentNotValidException.class));

    }

    @Test
    public void testUpdateBooking() throws Exception {
        Mockito.when(this.bookingService.updateBooking(anyLong(), anyBoolean(), anyLong())).thenReturn(responseDto);

        when(bookingService.updateBooking(anyLong(), anyBoolean(), anyLong())).thenReturn(responseDto);

        mvc.perform(patch("/bookings/{bookingId}", 1L)
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", 123L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));

        verify(bookingService, times(1)).updateBooking(anyLong(), anyBoolean(), anyLong());
    }

    @Test
    public void testGetBookingById() throws Exception {

        when(bookingService.getBookingById(anyLong(), anyLong())).thenReturn(responseDto);

        mvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 123L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));

        verify(bookingService, times(1)).getBookingById(anyLong(), anyLong());
    }

    @Test
    public void testGetBookingsByBooker() throws Exception {

        when(bookingService.getBookingsByBooker(anyString(), anyLong(), anyInt(), anyInt())).thenReturn(
                responseDtoList);

        mvc.perform(get("/bookings")
                        .param("state", "WAITING")
                        .param("from", "0")
                        .param("size", "10")
                        .header("X-Sharer-User-Id", 123L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDtoList)));

        verify(bookingService, times(1)).getBookingsByBooker(anyString(), anyLong(), anyInt(), anyInt());
    }

    @Test
    public void testGetBookingsByBookerBadFromPosition() throws Exception {

        when(bookingService.getBookingsByBooker(anyString(), anyLong(), anyInt(), anyInt())).thenReturn(
                responseDtoList);

        mvc.perform(get("/bookings")
                        .param("state", "WAITING")
                        .param("from", "-1")
                        .param("size", "10")
                        .header("X-Sharer-User-Id", 123L))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass()
                        .equals(ConstraintViolationException.class));
    }

    @Test
    public void testGetBookingsByBookerBadPageSize() throws Exception {

        when(bookingService.getBookingsByBooker(anyString(), anyLong(), anyInt(), anyInt())).thenReturn(
                responseDtoList);

        mvc.perform(get("/bookings")
                        .param("state", "WAITING")
                        .param("from", "0")
                        .param("size", "-1")
                        .header("X-Sharer-User-Id", 123L))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass()
                        .equals(ConstraintViolationException.class));
    }

    @Test
    public void testGetBookingsByOwner() throws Exception {

        when(bookingService.getBookingsByOwner(anyString(), anyLong(), anyInt(), anyInt())).thenReturn(responseDtoList);

        mvc.perform(get("/bookings/owner")
                        .param("state", "WAITING")
                        .param("from", "0")
                        .param("size", "10")
                        .header("X-Sharer-User-Id", 123L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDtoList)));

        verify(bookingService, times(1)).getBookingsByOwner(anyString(), anyLong(), anyInt(), anyInt());
    }

    @Test
    public void testGetBookingsByOwnerBadFromPosition() throws Exception {

        when(bookingService.getBookingsByOwner(anyString(), anyLong(), anyInt(), anyInt())).thenReturn(responseDtoList);

        mvc.perform(get("/bookings/owner")
                        .param("state", "WAITING")
                        .param("from", "-1")
                        .param("size", "10")
                        .header("X-Sharer-User-Id", 123L))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass()
                        .equals(ConstraintViolationException.class));
    }

    @Test
    public void testGetBookingsByOwnerBadPageSize() throws Exception {

        when(bookingService.getBookingsByOwner(anyString(), anyLong(), anyInt(), anyInt())).thenReturn(responseDtoList);

        mvc.perform(get("/bookings/owner")
                        .param("state", "WAITING")
                        .param("from", "0")
                        .param("size", "-1")
                        .header("X-Sharer-User-Id", 123L))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass()
                        .equals(ConstraintViolationException.class));
    }
}
