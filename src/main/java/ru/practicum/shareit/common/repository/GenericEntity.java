package ru.practicum.shareit.common.repository;

import lombok.Data;
import lombok.experimental.SuperBuilder;


@SuperBuilder(toBuilder = true)
@Data
public class GenericEntity {
    public Integer id;
}
