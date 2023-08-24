package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(Long userId, CreateItemDto createItemDto);

    ItemDto getItemById(Long itemId, Long userId);

    List<ItemDto> getItems(Long userId);

    ItemDto updateItem(Long itemId, Long userId, CreateItemDto createItemDto);

    List<ItemDto> searchItems(String text);

    void deleteItem(Long itemId);

    CommentDto addComment(CreateCommentDto createCommentDto, Long userId, Long itemId);
}
