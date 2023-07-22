package ru.practicum.shareit.common.ecxeption;

public class AccessDenyException extends RuntimeException {
    public AccessDenyException(String message) {
        super(message);
    }
}