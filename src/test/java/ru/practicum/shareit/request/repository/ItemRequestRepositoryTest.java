package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRequestRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Test
    void findAllByRequesterIdOrderByCreatedDesc() {
        User requester = new User( null,"User", "email@email.com");
        entityManager.persist(requester);
        ItemRequest itemRequest = new ItemRequest(null, "Description1", requester, LocalDateTime.now().minusMinutes(1),
                null);
        entityManager.persist(itemRequest);

        List<ItemRequest> requests = itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(requester.getId());

        assertEquals(1, requests.size());

        ItemRequest request = requests.get(0);
        assertEquals("Description1", request.getDescription());
    }

    @Test
    void findAByRequesterIdIsNot() {
        User requester = new User(null, "User", "email@email.com");
        entityManager.persist(requester);
        User requester2 = new User(null, "User2", "email2@email.com");
        entityManager.persist(requester2);
        ItemRequest itemRequest1 = new ItemRequest(null, "Description1", requester, LocalDateTime.now().minusMinutes(1),
                null);
        entityManager.persist(itemRequest1);
        ItemRequest itemRequest2 = new ItemRequest(null, "Description2", requester2, LocalDateTime.now().minusMinutes(2),
                null);
        entityManager.persist(itemRequest2);

        Pageable page = PageRequest.of(0, 10, Sort.by("created").descending());
        List<ItemRequest> requests = itemRequestRepository.findByRequesterIdIsNot(requester.getId(), page)
                .getContent();

        assertEquals(1, requests.size());

        ItemRequest request1 = requests.get(0);
        assertEquals("Description2", request1.getDescription());
    }
}