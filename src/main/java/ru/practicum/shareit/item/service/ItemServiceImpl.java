package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.ecxeption.AccessDenyException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.shareit.common.ecxeption.NotFoundException.notFoundException;

@Service
@Slf4j
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public ItemDto createItem(Integer ownerId, ItemDto itemDto) {
        Item item = ItemMapper.fromItemDto(itemDto)
                .toBuilder()
                .owner(UserMapper.fromUserDto(userService.getUserById(ownerId)))
                .build();
        item = itemRepository.save(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto getItemById(Integer itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(notFoundException("Вещь с id = {0} не найдена.", itemId));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getItemsByOwnerId(Integer ownerId) {
        return itemRepository.findAll().stream()
                .filter(item -> Objects.equals(item.getOwner().getId(), ownerId))
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItems() {
        return itemRepository.findAll().stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto updateItem(Integer itemId, Integer ownerId, ItemDto itemDto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(notFoundException("Вещь с id = {0} не найдена.", itemId));
        if (!Objects.equals(item.getOwner().getId(), ownerId)) {
            String message = String.format("Вещь %s не принадлежит полюзователю %s", itemId, ownerId);
            throw new AccessDenyException(message);
        }
        item.setName(itemDto.getName() != null ? itemDto.getName() : item.getName());
        item.setDescription(itemDto.getDescription() != null ? itemDto.getDescription() : item.getDescription());
        item.setAvailable(itemDto.getAvailable() != null ? itemDto.getAvailable() : item.getAvailable());
        itemRepository.save(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRepository.findAll().stream()
                .filter(item -> item.getName().toLowerCase().contains(text) ||
                        item.getDescription().toLowerCase().contains(text))
                .filter(Item::getAvailable)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteItem(Integer itemId) {
        itemRepository.deleteById(itemId);
    }
}
