package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemForItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring",
        uses = {})
public interface ItemRequestMapper {
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "requester", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    ItemRequest createItemRequestToItemrequest(CreateItemRequestDto createItemRequestDto);

    ItemRequestDto itemRequestToItemRequestDto(ItemRequest itemRequest);

    List<ItemRequestDto> itemRequestsToItemRequestDtos(List<ItemRequest> itemRequests);

    @Mapping(target = "requestId", source = "item.request.id")
    ItemForItemRequestDto itemToItemForItemRequestDto(Item item);

    default String localDateTimeToString(LocalDateTime localDateTime) {
        return DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                .format(localDateTime);
    }
}
