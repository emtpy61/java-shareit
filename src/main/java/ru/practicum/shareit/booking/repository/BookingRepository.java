package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long>, QuerydslPredicateExecutor<Booking> {

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
            "FROM Booking b " +
            "WHERE b.item = :item " +
            "AND b.status = :status " +
            "AND b.end <= :currentDateTime " +
            "AND b.booker = :booker " +
            "AND b.start <= :currentDateTime")
    boolean hasCompletedBookingsForItem(
            User booker,
            Item item,
            BookingStatus status,
            LocalDateTime currentDateTime
    );

}
