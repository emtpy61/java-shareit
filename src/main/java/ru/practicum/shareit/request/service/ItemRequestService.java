package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto createItemRequest(CreateItemRequestDto createItemRequestDto, Long userId);

    List<ItemRequestDto> getUserItemRequests(Long userId);

    List<ItemRequestDto> getOtherUsersItemRequests(Long userId, int from, int size);

    ItemRequestDto getItemRequestById(Long userId, Long requestId);
}
