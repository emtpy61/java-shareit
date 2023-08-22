package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
public class ItemRequestControllerTest {

    private static CreateItemRequestDto createItemRequestDto;
    private static ItemRequestDto itemRequestDto;
    private static List<ItemRequestDto> itemRequestDtoList;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemRequestService itemRequestService;

    @BeforeEach
    public void setUp() {
        createItemRequestDto = new CreateItemRequestDto();
        createItemRequestDto.setDescription("Example description");

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("Example description");
        itemRequestDto.setItems(null);
        itemRequestDto.setCreated("2023-08-19T13:53:07.617615700");

        itemRequestDtoList = new ArrayList<>();
        itemRequestDtoList.add(itemRequestDto);
    }

    @Test
    void testCreateItemRequest() throws Exception {
        Mockito.when(this.itemRequestService.createItemRequest(any(CreateItemRequestDto.class), anyLong()))
                .thenReturn(itemRequestDto);

        mockMvc.perform(
                        post("/requests")
                                .content(objectMapper.writeValueAsString(createItemRequestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 123L))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemRequestDto)));
    }

    @Test
    void testGetUserItemRequests() throws Exception {
        Mockito.when(this.itemRequestService.getUserItemRequests(anyLong())).thenReturn(itemRequestDtoList);

        mockMvc.perform(
                        get("/requests")
                                .header("X-Sharer-User-Id", 123L))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemRequestDtoList)));
    }

    @Test
    void testGetOtherUsersItemRequests() throws Exception {
        Mockito.when(this.itemRequestService.getOtherUsersItemRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(itemRequestDtoList);

        mockMvc.perform(
                        get("/requests/all")
                                .param("from", "0")
                                .param("size", "10")
                                .header("X-Sharer-User-Id", 123L))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemRequestDtoList)));
    }

    @Test
    void testGetItemRequestById() throws Exception {
        Mockito.when(this.itemRequestService.getItemRequestById(anyLong(), anyLong())).thenReturn(itemRequestDto);

        mockMvc.perform(
                        get("/requests/{requestId}", 1L)
                                .header("X-Sharer-User-Id", 123L))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemRequestDto)));
    }
}