package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CreateItemDtoTest {

    @Autowired
    private JacksonTester<CreateItemDto> jacksonTester;

    @Test
    void testSerialize() throws Exception {
        CreateItemDto createItemDto = new CreateItemDto("Name", "Description", true, 1L);

        JsonContent<CreateItemDto> createItemDtoJsonContent = jacksonTester.write(createItemDto);

        assertThat(createItemDtoJsonContent).hasJsonPath("$.name");
        assertThat(createItemDtoJsonContent).hasJsonPath("$.description");
        assertThat(createItemDtoJsonContent).hasJsonPath("$.available");
        assertThat(createItemDtoJsonContent).hasJsonPath("$.requestId");

        assertThat(createItemDtoJsonContent).extractingJsonPathStringValue("$.name")
                .isEqualTo("Name");
        assertThat(createItemDtoJsonContent).extractingJsonPathStringValue("$.description")
                .isEqualTo("Description");
        assertThat(createItemDtoJsonContent).extractingJsonPathBooleanValue("$.available")
                .isEqualTo(true);
        assertThat(createItemDtoJsonContent).extractingJsonPathNumberValue("$.requestId")
                .isEqualTo(1);
    }
}