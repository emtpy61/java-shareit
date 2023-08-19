package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.common.ecxeption.NotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapperImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
class UserServiceTest {

    private static User user;
    private static UserDto userDto;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private UserServiceImpl userService;
    @Autowired
    private UserMapperImpl userMapper;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(userService, "userMapper", userMapper);
        user = new User(1L, "User", "email@email.com");
        userDto = new UserDto(1L, "User", "email@email.com");
    }

    @Test
    public void testCreateAndGetUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        UserDto createdUser = userService.createUser(userDto);
        UserDto retrievedUser = userService.getUserById(createdUser.getId());
        assertThat(retrievedUser).isEqualTo(createdUser);
    }

    @Test
    public void testUpdateUser() {
        userDto.setName("updated");
        userDto.setEmail("updated@email.com");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(userMapper.userDtotoUser(userDto));
        UserDto updatedUser = userService.updateUser(1L, userDto);
        assertThat(updatedUser).isEqualTo(userDto);
    }

    @Test
    public void testUpdatetestUpdateUserWithInvalidUser() {
        userDto.setName("updated");
        userDto.setEmail("updated@email.com");
        when(userRepository.findById(eq(4L))).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(userMapper.userDtotoUser(userDto));

        assertThatThrownBy(() -> userService.updateUser(4L, userDto))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void testGetUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDto> users = userService.getUsers();

        assertThat(users.size()).isEqualTo(1);
    }
}