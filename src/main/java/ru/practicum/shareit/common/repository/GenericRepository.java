package ru.practicum.shareit.common.repository;

import java.util.List;
import java.util.Optional;

public interface GenericRepository<T> {
    T save(T entity);

    Optional<T> findById(Integer id);

    boolean existsById(Integer id);

    List<T> findAll();

    List<T> findAllById(Iterable<Integer> ids);

    void deleteById(Integer id);

    void delete(T entity);

    void deleteAll();
}
