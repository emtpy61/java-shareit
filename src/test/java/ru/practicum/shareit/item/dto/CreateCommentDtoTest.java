package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CreateCommentDtoTest {
    @Autowired
    private JacksonTester<CreateCommentDto> jacksonTester;

    @Test
    void testSerialize() throws Exception {
        CreateCommentDto createCommentDto = new CreateCommentDto("Comment", "Author");

        JsonContent<CreateCommentDto> createCommentDtoJsonContent = jacksonTester.write(createCommentDto);

        assertThat(createCommentDtoJsonContent).hasJsonPath("$.text");
        assertThat(createCommentDtoJsonContent).hasJsonPath("$.authorName");

        assertThat(createCommentDtoJsonContent).extractingJsonPathStringValue("$.text")
                .isEqualTo("Comment");
        assertThat(createCommentDtoJsonContent).extractingJsonPathStringValue("$.authorName")
                .isEqualTo("Author");
    }
}