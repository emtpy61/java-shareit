package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void testFindAllByItemId() {

        User user = new User(1L, "User", "email@email.com");
        user = userRepository.save(user);
        Item item = new Item(1L, "Example item", "Example item", true, user, null,
                null, new ArrayList<>());
        Comment comment1 = new Comment(1L, "Excellent item!", item, user, LocalDateTime.now());
        Comment comment2 = new Comment(2L, "Excellent item2!", item, user, LocalDateTime.now());

        item.addComment(comment1);
        item.addComment(comment2);

        item = itemRepository.save(item);

        List<Comment> comments = commentRepository.findAllByItemId(item.getId());

        assertEquals(2, comments.size());

        Comment retrievedComment1 = comments.get(0);
        assertEquals("Excellent item!", retrievedComment1.getText());

        Comment retrievedComment2 = comments.get(1);
        assertEquals("Excellent item2!", retrievedComment2.getText());
    }
}