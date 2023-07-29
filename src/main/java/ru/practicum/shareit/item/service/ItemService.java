package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(Integer ownerId, ItemDto itemDto);

    ItemDto getItemById(Integer itemId);

    List<ItemDto> getItemsByOwnerId(Integer ownerId);

    List<ItemDto> getItems();

    ItemDto updateItem(Integer itemId, Integer ownerId, ItemDto itemDto);

    List<ItemDto> searchItems(String text);

    void deleteItem(Integer itemId);
}
