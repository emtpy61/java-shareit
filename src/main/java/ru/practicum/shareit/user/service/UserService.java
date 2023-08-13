package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUserById(Long userId);

    List<UserDto> getUsers();

    UserDto updateUser(Long userId, UserDto userDto);

    void deleteUser(Long userId);
}

