package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.common.ecxeption.NotFoundException.notFoundException;

@Service
@Slf4j
@AllArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestMapper itemRequestMapper;

    @Override
    public ItemRequestDto createItemRequest(CreateItemRequestDto createItemRequestDto, Long userId) {
        User user = getUser(userId);
        ItemRequest itemRequest = itemRequestMapper.createItemRequestToItemrequest(createItemRequestDto);
        itemRequest.setRequester(user);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest = itemRequestRepository.save(itemRequest);
        return itemRequestMapper.itemRequestToItemRequestDto(itemRequest);
    }

    @Override
    public List<ItemRequestDto> getUserItemRequests(Long userId) {
        User user = getUser(userId);
        List<ItemRequest> requests = itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(userId);
        return itemRequestMapper.itemRequestsToItemRequestDtos(requests);
    }

    @Override
    public List<ItemRequestDto> getOtherUsersItemRequests(Long userId, int from, int size) {
        User user = getUser(userId);
        Pageable page = PageRequest.of(from / size, size, Sort.by("created").descending());
        List<ItemRequest> requests = itemRequestRepository.findByRequesterIdIsNot(userId, page).getContent();
        return itemRequestMapper.itemRequestsToItemRequestDtos(requests);
    }

    @Override
    public ItemRequestDto getItemRequestById(Long userId, Long requestId) {
        User user = getUser(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(notFoundException("Запрос с id = {0} не найдена.", requestId));
        return itemRequestMapper.itemRequestToItemRequestDto(itemRequest);
    }

    private Item getItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(notFoundException("Вещь с id = {0} не найдена.", itemId));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(notFoundException("Пользователь с id = {0} не найден.", userId));
    }
}
