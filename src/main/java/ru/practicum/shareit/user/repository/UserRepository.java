package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.common.repository.GenericRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserRepository extends GenericRepository<User> {
    boolean emailIsExists(User user);

    Optional<User> findByEmail(String email);
}
