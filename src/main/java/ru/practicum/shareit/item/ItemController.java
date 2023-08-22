package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
            @NotNull @RequestBody @Valid CreateItemDto createItemDto) {
        log.info(">>>POST запрос на создание item: {}", createItemDto);
        return itemService.createItem(userId, createItemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId) {
        log.info(">>>GET запрос на получение item с id: {}", itemId);
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getItemsByOwnerId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info(">>>GET запрос на получение item с id пользователя: {}", userId);
        return itemService.getItems(userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable Long itemId,
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestBody CreateItemDto createItemDto) {
        log.info(">>>PATCH запрос на изменение item с id: {}", itemId);
        return itemService.updateItem(itemId, ownerId, createItemDto);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        log.info(">>>GET запрос на поиск по тексту: {}", text);
        return itemService.searchItems(text.toLowerCase());
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable Long itemId) {
        log.info(">>>DELETE запрос на удаление item с id: {}", itemId);
        itemService.deleteItem(itemId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createCommentToItem(
            @PathVariable Long itemId,
            @RequestHeader("X-Sharer-User-Id") Long bookerId,
            @RequestBody @Valid CreateCommentDto createCommentDto) {
        log.info(">>>POST запрос на добаление комментария к item с id: {}", itemId);
        return itemService.addComment(createCommentDto, bookerId, itemId);
    }
}
