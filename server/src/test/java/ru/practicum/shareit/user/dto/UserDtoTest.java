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

        JsonContent<UserDto> userDtoJsonContent = jacksonTester.write(userDto);

        assertThat(userDtoJsonContent).hasJsonPath("$.id");
        assertThat(userDtoJsonContent).hasJsonPath("$.name");
        assertThat(userDtoJsonContent).hasJsonPath("$.email");

        assertThat(userDtoJsonContent).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(userDtoJsonContent).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(userDtoJsonContent).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
    }
}