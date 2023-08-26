package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createItemRequest(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody @Valid CreateItemRequestDto createItemRequestDto) {
        log.info(">>>POST запрос на создание request: {}", createItemRequestDto);
        return itemRequestClient.createItemRequest(createItemRequestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserItemRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info(">>>GET запрос запрос на получение request от user с id: {}", userId);
        return itemRequestClient.getUserItemRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getOtherUsersItemRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info(">>>GET запрос запрос на получение всех request.");
        return itemRequestClient.getOtherUsersItemRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable(name = "requestId") Long requestId) {
        log.info(">>>GET запрос запрос на получение request с id: {}", requestId);
        return itemRequestClient.getItemRequestById(userId, requestId);
    }
}
