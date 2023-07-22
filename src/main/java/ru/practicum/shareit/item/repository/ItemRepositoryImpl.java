package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.common.repository.GenericRepositoryImpl;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemRepositoryImpl extends GenericRepositoryImpl<Item> implements ItemRepository {

}
