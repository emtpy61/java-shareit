package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testFindAllByItemId() {

        User user = new User(null, "User", "email@email.com");
        entityManager.persist(user);
        Item item = new Item(null, "Example item", "Example item", true, user, null,
                null, new ArrayList<>());
        Comment comment1 = new Comment(null, "Excellent item!", item, user, LocalDateTime.now());
        Comment comment2 = new Comment(null, "Excellent item2!", item, user, LocalDateTime.now());
        item.addComment(comment1);
        item.addComment(comment2);
        entityManager.persist(item);

        List<Comment> comments = commentRepository.findAllByItemId(item.getId());

        assertEquals(2, comments.size());

        Comment retrievedComment1 = comments.get(0);
        assertEquals("Excellent item!", retrievedComment1.getText());

        Comment retrievedComment2 = comments.get(1);
        assertEquals("Excellent item2!", retrievedComment2.getText());
    }
}