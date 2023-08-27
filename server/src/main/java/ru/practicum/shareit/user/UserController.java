package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
        log.info(">>>POST запрос на создание user: {}", userDto);
        return userService.createUser(userDto);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        log.info(">>>GET запрос на получение user с id: {}", userId);
        return userService.getUserById(userId);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info(">>>GET запрос на получение всех user");
        return userService.getUsers();
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable Long userId,
            @RequestBody UserDto userDto) {
        log.info(">>>PATCH запрос на изменение user с id: {}", userId);
        return userService.updateUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info(">>>DELETE запрос на удаление user с id: {}", userId);
        userService.deleteUser(userId);
    }
}
