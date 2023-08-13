package ru.practicum.shareit.common.ecxeption;

public class ItemNotAvailbaleException extends RuntimeException {
    public ItemNotAvailbaleException(String message) {
        super(message);
    }
}