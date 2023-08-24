package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerId(long ownerId);

    @Query("SELECT i FROM Item i "
            + "WHERE (LOWER(i.name) LIKE LOWER(CONCAT('%', :text, '%')) "
            + "OR LOWER(i.description) LIKE LOWER(CONCAT('%', :text, '%'))) "
            + "AND i.available = true ")
    List<Item> searchItemByNameOrDescription(String text);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Item i SET " +
            "i.name = CASE WHEN :#{#item.name} IS NOT NULL THEN :#{#item.name} ELSE i.name END, " +
            "i.description = CASE WHEN :#{#item.description} IS NOT NULL THEN :#{#item.description} ELSE i.description END, "
            +
            "i.available = CASE WHEN :#{#item.available} IS NOT NULL THEN :#{#item.available} ELSE i.available END " +
            "WHERE i.id = :itemId AND i.owner.id = :ownerId")
    void updateItemFields(Item item, Long ownerId, Long itemId);
}
