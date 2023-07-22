package ru.practicum.shareit.common.repository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class GenericRepositoryImpl<T extends GenericEntity> implements GenericRepository<T> {
    protected final Map<Integer, T> entities = new HashMap<>();
    protected int currentId = 0;

    @Override
    public T save(T entity) {
        if (!entities.containsKey(entity.getId())) {
            entity.setId(++currentId);
        }
        entities.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<T> findById(Integer id) {
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public boolean existsById(Integer id) {
        return entities.containsKey(id);
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(entities.values());
    }

    @Override
    public List<T> findAllById(Iterable<Integer> ids) {
        return StreamSupport.stream(ids.spliterator(), false)
                .map(this::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Integer id) {
        entities.remove(id);
    }

    @Override
    public void delete(T entity) {
        this.deleteById(entity.getId());
    }

    @Override
    public void deleteAll() {
        entities.clear();
    }
}
