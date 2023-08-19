package ru.practicum.shareit.user;

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
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
public class UserControllerTest {

    private static UserDto userDto;
    private static List<UserDto> userDtoList;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("name");
        userDto.setEmail("email@email.com");

        userDtoList = new ArrayList<>();
        userDtoList.add(userDto);
    }

    @Test
    void testCreateUser() throws Exception {
        Mockito.when(this.userService.createUser(any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(userDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));
    }

    @Test
    void testGetUserById() throws Exception {
        Mockito.when(this.userService.getUserById(anyLong())).thenReturn(userDto);

        mockMvc.perform(
                        get("/users/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));
    }

    @Test
    void testGetAllUsers() throws Exception {
        Mockito.when(this.userService.getUsers()).thenReturn(userDtoList);

        mockMvc.perform(
                        get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDtoList)));
    }

    @Test
    void testUpdateUser() throws Exception {
        Mockito.when(this.userService.updateUser(anyLong(), any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(
                        patch("/users/{userId}", 1L)
                                .content(objectMapper.writeValueAsString(userDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));
    }

    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(
                        delete("/users/{userId}", 1L))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(anyLong());
    }
}