package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
            @NotNull @RequestBody @Valid CreateItemDto createItemDto) {
        log.info(">>>POST запрос на создание item: {}", createItemDto);
        return itemClient.createItem(userId, createItemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId) {
        log.info(">>>GET запрос на получение item с id: {}", itemId);
        return itemClient.getItemById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByOwnerId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info(">>>GET запрос на получение item с id пользователя: {}", userId);
        return itemClient.getItems(userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable Long itemId,
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody CreateItemDto itemRequestDto) {
        log.info(">>>PATCH запрос на изменение item с id: {}", itemId);
        return itemClient.updateItem(itemId, userId, itemRequestDto);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text) {
        log.info(">>>GET запрос на поиск по тексту: {}", text);
        return itemClient.searchItems(text.toLowerCase());
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItem(@PathVariable Long itemId) {
        log.info(">>>DELETE запрос на удаление item с id: {}", itemId);
        return itemClient.deleteItem(itemId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createCommentToItem(
            @PathVariable Long itemId,
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody @Valid CreateCommentDto createCommentDto) {
        log.info(">>>POST запрос на добаление комментария к item с id: {}", itemId);
        return itemClient.addComment(createCommentDto, userId, itemId);
    }
}
