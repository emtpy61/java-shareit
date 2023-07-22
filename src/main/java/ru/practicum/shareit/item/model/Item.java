package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.common.repository.GenericEntity;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Data
public class Item extends GenericEntity {
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private Boolean available;
    private User owner;
    private ItemRequest request;
}
