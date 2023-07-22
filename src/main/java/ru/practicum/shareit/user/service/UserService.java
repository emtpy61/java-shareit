package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUserById(Integer userId);

    List<UserDto> getUsers();

    UserDto updateUser(Integer userId, UserDto userDto);

    void deleteUser(Integer userId);
}

