package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static ru.practicum.shareit.common.ecxeption.NotFoundException.notFoundException;

@Service
@Slf4j
@AllArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userRepository.save(userMapper.userDtotoUser(userDto));
        return userMapper.userToUserDto(user);
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(notFoundException("Пользователь с id = {0} не найден.", userId));
        return userMapper.userToUserDto(user);
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.usersToUserDtos(users);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        userRepository.updateUserFields(userMapper.userDtotoUser(userDto), userId);
        return userMapper.userToUserDto(userRepository.findById(userId)
                .orElseThrow(notFoundException("Пользователь с id = {0} не найден.", userId)));
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
