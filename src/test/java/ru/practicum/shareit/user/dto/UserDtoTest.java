package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoTest {

    @Autowired
    private JacksonTester<UserDto> jacksonTester;

    @Test
    void testSerialize() throws Exception {
        UserDto userDto = new UserDto(1L, "User", "email@email.com");

        JsonContent<UserDto> UserDtoJsonContent = jacksonTester.write(userDto);

        assertThat(UserDtoJsonContent).hasJsonPath("$.id");
        assertThat(UserDtoJsonContent).hasJsonPath("$.name");
        assertThat(UserDtoJsonContent).hasJsonPath("$.email");

        assertThat(UserDtoJsonContent).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(UserDtoJsonContent).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(UserDtoJsonContent).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
    }
}