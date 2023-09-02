package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingRequestDtoTest {

    @Autowired
    private JacksonTester<BookingRequestDto> jacksonTester;

    @Test
    void testSerialize() throws Exception {
        BookingRequestDto bookingRequestDto = new BookingRequestDto(1L,
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));

        JsonContent<BookingRequestDto> bookingRequestDtoJsonContent = jacksonTester.write(bookingRequestDto);

        assertThat(bookingRequestDtoJsonContent).hasJsonPath("$.itemId");
        assertThat(bookingRequestDtoJsonContent).hasJsonPath("$.start");
        assertThat(bookingRequestDtoJsonContent).hasJsonPath("$.end");

        assertThat(bookingRequestDtoJsonContent).extractingJsonPathNumberValue("$.itemId")
                .isEqualTo(1);
        assertThat(bookingRequestDtoJsonContent).hasJsonPathValue("$.start");
        assertThat(bookingRequestDtoJsonContent).hasJsonPathValue("$.end");
    }
}