package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CreateItemRequestDtoTest {
    @Autowired
    private JacksonTester<CreateItemRequestDto> jacksonTester;

    @Test
    void testSerialize() throws Exception {
        CreateItemRequestDto createItemRequestDto = new CreateItemRequestDto("Description");

        JsonContent<CreateItemRequestDto> createItemRequestDtoJsonContent = jacksonTester.write(createItemRequestDto);

        assertThat(createItemRequestDtoJsonContent).hasJsonPath("$.description");

        assertThat(createItemRequestDtoJsonContent).extractingJsonPathStringValue("$.description")
                .isEqualTo("Description");
    }
}