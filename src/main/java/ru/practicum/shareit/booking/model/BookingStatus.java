package ru.practicum.shareit.booking.model;

public enum BookingStatus {
    WAITING("Новое бронирование, ожидает одобрения."),
    APPROVED("Бронирование подтверждено владельцем."),
    REJECTED("Бронирование отклонено владельцем."),
    CANCELED("Бронирование отменено создателем.");

    public final String description;

    BookingStatus(String description) {
        this.description = description;
    }
}
