package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void init() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    void findAllByOwnerId() {
        User user = new User(1L, "User", "email@email.com");
        user = userRepository.save(user);
        Item item = new Item(1L, "Example item", "Example item", true, user, null,
                null, new ArrayList<>());
        Item item2 = new Item(2L, "Example item2", "Example item2", true, user, null,
                null, new ArrayList<>());
        itemRepository.save(item);
        itemRepository.save(item2);

        List<Item> items = itemRepository.findAllByOwnerId(user.getId());

        assertEquals(2, items.size());

        Item retrievedItem1 = items.get(0);
        assertEquals("Example item", retrievedItem1.getName());

        Item retrievedItem2 = items.get(1);
        assertEquals("Example item2", retrievedItem2.getName());
    }

    @Test
    void searchItemByNameOrDescription() {
        User user = new User(1L, "User", "email@email.com");
        user = userRepository.save(user);
        Item item = new Item(1L, "Example item", "Example item", true, user, null,
                null, new ArrayList<>());
        Item item2 = new Item(2L, "Example item2", "Example item2", true, user, null,
                null, new ArrayList<>());

        itemRepository.save(item);
        itemRepository.save(item2);

        List<Item> items = itemRepository.searchItemByNameOrDescription("item");

        assertEquals(2, items.size());

        Item retrievedItem1 = items.get(0);
        assertEquals("Example item", retrievedItem1.getName());

        Item retrievedItem2 = items.get(1);
        assertEquals("Example item2", retrievedItem2.getName());
    }

    @Test
    void updateItemFields() {

        User user = new User(1L, "User", "email@email.com");
        user = userRepository.save(user);
        ItemRequest itemRequest = new ItemRequest(1L, "Description", user, LocalDateTime.now(), null);
        Item item = new Item(1L, "Example item", "Example item", true, user, null,
                null, new ArrayList<>());
        Item itemUpdateName = new Item(1L, "Update item", "Example item", true, user, null,
                null, new ArrayList<>());
        Item itemUpdateDescription = new Item(1L, "Example item", "Update item", true, user, null,
                null, new ArrayList<>());
        Item itemUpdateAvailable = new Item(1L, "Example item", "Example item", false, user, null,
                null, new ArrayList<>());

        item = itemRepository.save(item);

        itemRepository.updateItemFields(itemUpdateName, user.getId(), item.getId());
        Item retrievedItem = itemRepository.findById(item.getId()).get();
        assertEquals("Update item", retrievedItem.getName());

        itemRepository.updateItemFields(itemUpdateDescription, user.getId(), item.getId());
        retrievedItem = itemRepository.findById(item.getId()).get();
        assertEquals("Update item", retrievedItem.getDescription());

        itemRepository.updateItemFields(itemUpdateAvailable, user.getId(), item.getId());
        retrievedItem = itemRepository.findById(item.getId()).get();
        assertEquals(false, retrievedItem.getAvailable());
    }
}