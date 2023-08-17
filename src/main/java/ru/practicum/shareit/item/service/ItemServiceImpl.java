package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.common.ecxeption.AccessDenyException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.practicum.shareit.common.ecxeption.NotFoundException.notFoundException;

@Service
@Slf4j
@AllArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ItemDto createItem(Long ownerId, ItemDto itemDto) {
        getUser(ownerId);
        itemDto.setOwnerId(ownerId);
        itemDto.setId(
                itemRepository.save(itemMapper.itemDtoToItem(itemDto))
                        .getId());
        return itemDto;
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        Item item = getItem(itemId);

        return itemMapper.itemToItemDto(item, userId);
    }

    @Override
    public List<ItemDto> getItems(Long userId) {
        List<Item> items = itemRepository.findAllByOwnerId(userId);
        return itemMapper.itemsToItemDtos(items, userId);
    }

    @Transactional
    @Override
    public ItemDto updateItem(Long itemId, Long userId, ItemDto itemDto) {
        Item item = getItem(itemId);
        if (!Objects.equals(item.getOwner().getId(), userId)) {
            String message = String.format("Вещь %s не принадлежит полюзователю %s", itemId, userId);
            throw new AccessDenyException(message);
        }
        itemRepository.updateItemFields(itemMapper.itemDtoToItem(itemDto), userId, itemId);
        return getItemById(itemId, userId);
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        List<Item> items = itemRepository.searchItemByNameOrDescription(text);
        return itemMapper.itemsToItemDtos(items, null);
    }

    @Transactional
    @Override
    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Override
    public CommentDto addComment(CreateCommentDto createCommentDto, Long userId, Long itemId) {
        User user = getUser(userId);
        Item item = getItem(itemId);
        if (!bookingRepository.hasCompletedBookingsForItem(
                user,
                item,
                BookingStatus.APPROVED,
                LocalDateTime.now())) {
            throw new IllegalArgumentException("Нельзя добавить комментарий. Пользователь не арендовал вещь.");
        }

        Comment comment = commentMapper.createCommentDtotoComment(createCommentDto);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        item.addComment(comment);
        return commentMapper.commentToCommentDto(commentRepository.save(comment));
    }

    private Item getItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(notFoundException("Вещь с id = {0} не найдена.", itemId));
    }

    private User getUser(Long ownerId) {
        return userRepository.findById(ownerId)
                .orElseThrow(notFoundException("Пользователь с id = {0} не найден.", ownerId));
    }
}
