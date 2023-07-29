package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.common.repository.GenericRepositoryImpl;
import ru.practicum.shareit.user.model.User;

import java.util.Objects;
import java.util.Optional;

@Component
public class UserRepositoryImpl extends GenericRepositoryImpl<User> implements UserRepository {

    @Override
    public boolean emailIsExists(User user) {
        return entities.values().stream()
                .map(User::getEmail)
                .anyMatch(email -> Objects.equals(email, user.getEmail()));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return entities.values().stream()
                .filter(user -> Objects.equals(email, user.getEmail()))
                .findFirst();
    }
}
