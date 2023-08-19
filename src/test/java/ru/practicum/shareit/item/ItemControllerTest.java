package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    private static CreateItemDto createItemDto;
    private static ItemDto itemDto;
    private static CommentDto commentDto;
    private static List<ItemDto> itemDtoList;
    private static CreateCommentDto createCommentDto;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemService itemService;

    @BeforeEach
    public void setUp() {
        createItemDto = new CreateItemDto("Дрель", "Простая дрель", true, null);
        commentDto = new CommentDto(1L, "comment", "user", "2023-08-19T13:53:07.617615700");
        itemDto = new ItemDto(
                1L,
                "Example item",
                "Example item",
                true,
                123L,
                null,
                null,
                null,
                List.of(commentDto));
        itemDtoList = new ArrayList<>();
        itemDtoList.add(itemDto);
        createCommentDto = new CreateCommentDto("comment", "user");
    }

    @Test
    void testCreateItem() throws Exception {
        Mockito.when(this.itemService.createItem(anyLong(), any(CreateItemDto.class))).thenReturn(itemDto);

        mockMvc.perform(
                        post("/items")
                                .content(objectMapper.writeValueAsString(createItemDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 123L))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemDto)));
    }

    @Test
    void testGetItemById() throws Exception {
        Mockito.when(this.itemService.getItemById(anyLong(), anyLong())).thenReturn(itemDto);

        mockMvc.perform(
                        get("/items/{itemId}", 1L)
                                .header("X-Sharer-User-Id", 123L))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemDto)));
    }

    @Test
    void testGetItemsByOwnerId() throws Exception {
        Mockito.when(this.itemService.getItems(anyLong())).thenReturn(itemDtoList);

        mockMvc.perform(
                        get("/items")
                                .header("X-Sharer-User-Id", 123L))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemDtoList)));
    }

    @Test
    void testUpdateItem() throws Exception {
        Mockito.when(this.itemService.updateItem(anyLong(), anyLong(), any(CreateItemDto.class))).thenReturn(itemDto);

        mockMvc.perform(
                        patch("/items/{itemId}", 1L)
                                .content(objectMapper.writeValueAsString(createItemDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 123L))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemDto)));
    }

    @Test
    void testSearchItems() throws Exception {
        Mockito.when(this.itemService.searchItems(anyString())).thenReturn(itemDtoList);

        mockMvc.perform(
                        get("/items/search")
                                .param("text", "searchText"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemDtoList)));
    }

    @Test
    void testDeleteItem() throws Exception {
        mockMvc.perform(
                        delete("/items/{itemId}", 1L))
                .andExpect(status().isOk());

        verify(itemService, times(1)).deleteItem(anyLong());
    }

    @Test
    void testCreateCommentToItem() throws Exception {
        Mockito.when(this.itemService.addComment(any(CreateCommentDto.class), anyLong(), anyLong()))
                .thenReturn(commentDto);

        mockMvc.perform(
                        post("/items/{itemId}/comment", 1L)
                                .content(objectMapper.writeValueAsString(createCommentDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 123L))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(commentDto)));
    }
}