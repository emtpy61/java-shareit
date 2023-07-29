package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.common.repository.GenericRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends GenericRepository<Item> {
    List<Item> searchItems(String text);
}
