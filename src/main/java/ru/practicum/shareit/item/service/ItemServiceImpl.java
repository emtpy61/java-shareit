package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.ecxeption.AccessDenyException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.shareit.common.ecxeption.NotFoundException.notFoundException;

@Service
@Slf4j
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto createItem(Integer ownerId, ItemDto itemDto) {
        User user = userRepository.findById(ownerId)
                .orElseThrow(notFoundException("Пользователь с id = {0} не найден.", ownerId));
        Item item = ItemMapper.fromItemDto(itemDto, user)
                .toBuilder()
                .owner(user)
                .build();
        item = itemRepository.save(item);
        itemDto.setId(item.getId());
        return itemDto;
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
        return itemRepository.searchItems(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteItem(Integer itemId) {
        itemRepository.deleteById(itemId);
    }
}
