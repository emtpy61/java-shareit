package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createItemRequest(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody CreateItemRequestDto createItemRequestDto) {
        log.info(">>>POST запрос на создание request: {}", createItemRequestDto);
        return itemRequestService.createItemRequest(createItemRequestDto, userId);
    }

    @GetMapping
    public List<ItemRequestDto> getUserItemRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info(">>>GET запрос запрос на получение request от user с id: {}", userId);
        return itemRequestService.getUserItemRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getOtherUsersItemRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam int from,
            @RequestParam int size) {
        log.info(">>>GET запрос запрос на получение всех request.");
        return itemRequestService.getOtherUsersItemRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable(name = "requestId") Long requestId) {
        log.info(">>>GET запрос запрос на получение request с id: {}", requestId);
        return itemRequestService.getItemRequestById(userId, requestId);
    }
}
