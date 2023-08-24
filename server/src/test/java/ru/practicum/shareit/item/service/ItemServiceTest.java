package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.common.ecxeption.AccessDenyException;
import ru.practicum.shareit.common.ecxeption.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapperImpl;
import ru.practicum.shareit.item.mapper.ItemMapperImpl;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
class ItemServiceTest {
    private static Booking booking;
    private static User user;
    private static User booker;
    private static Item item;
    private static Comment comment;
    private static Item itemUpdated;
    private static ItemRequest itemRequest;
    private static CreateCommentDto createCommentDto;
    private static CreateItemDto updatedItemDto;
    private static CreateItemDto createItemDto;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private ItemServiceImpl itemService;
    @Autowired
    private ItemMapperImpl itemMapper;
    @Autowired
    private CommentMapperImpl commentMapper;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(itemService, "itemMapper", itemMapper);
        ReflectionTestUtils.setField(itemService, "commentMapper", commentMapper);

        user = new User(1L, "User", "email@email.com");
        itemRequest = new ItemRequest(1L, "Description", user, LocalDateTime.now(), null);
        item = new Item(1L, "Example item", "Example item", true, user, itemRequest,
                null, new ArrayList<>());
        itemUpdated = new Item(1L, "Updated Item", "Updated Description", true, user, null,
                null, new ArrayList<>());

        booker = new User(2L, "User2", "email2@email.com");
        booking = new Booking(1L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), item, booker,
                BookingStatus.WAITING);
        createItemDto = new CreateItemDto("Example item", "Example item", true, null);
        updatedItemDto = new CreateItemDto("Updated Item", "Updated Description", true, null);
        createCommentDto = new CreateCommentDto("Excellent item!", "User");
        comment = new Comment(1L, "Excellent item!", item, user, LocalDateTime.now());
    }

    @Test
    void testCreateItem() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto createdItem = itemService.createItem(user.getId(), createItemDto);

        assertThat(createdItem.getName()).isEqualTo(createItemDto.getName());
        assertThat(createdItem.getDescription()).isEqualTo(createItemDto.getDescription());
    }

    @Test
    void testCreateItemWithInvalidUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> itemService.createItem(user.getId(), createItemDto))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void testCreateItemForRequest() {
        createItemDto.setRequestId(1L);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        ItemDto createdItem = itemService.createItem(user.getId(), createItemDto);

        assertThat(createdItem.getName()).isEqualTo(createItemDto.getName());
        assertThat(createdItem.getDescription()).isEqualTo(createItemDto.getDescription());
        assertThat(createdItem.getRequestId()).isEqualTo(itemRequest.getId());
    }

    @Test
    void testGetItemById() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        ItemDto retrievedItem = itemService.getItemById(item.getId(), user.getId());

        assertThat(retrievedItem.getName()).isEqualTo(item.getName());
        assertThat(retrievedItem.getDescription()).isEqualTo(item.getDescription());
    }

    @Test
    void testGetItems() {
        List<Item> items = new ArrayList<>();
        items.add(item);
        when(itemRepository.findAllByOwnerId(eq(user.getId()))).thenReturn(items);

        List<ItemDto> retrievedItems = itemService.getItems(user.getId());

        assertThat(retrievedItems.size()).isEqualTo(1);
        assertThat(retrievedItems.get(0).getName()).isEqualTo(item.getName());
    }

    @Test
    void testUpdateItem() {
        when(itemRepository.findById(eq(item.getId()))).thenReturn(Optional.of(itemUpdated));

        ItemDto updatedItem = itemService.updateItem(item.getId(), user.getId(), updatedItemDto);

        assertThat(updatedItem.getName()).isEqualTo(updatedItemDto.getName());
        assertThat(updatedItem.getDescription()).isEqualTo(updatedItemDto.getDescription());
    }

    @Test
    void testUpdateItemWithNonOwnerUser() {
        when(itemRepository.findById(eq(item.getId()))).thenReturn(Optional.of(itemUpdated));

        assertThatThrownBy(() -> itemService.updateItem(item.getId(), 123L, updatedItemDto))
                .isInstanceOf(AccessDenyException.class);
    }

    @Test
    void testSearchItems() {
        when(itemRepository.searchItemByNameOrDescription(anyString())).thenReturn(List.of(item));

        List<ItemDto> retrievedItems = itemService.searchItems("text");

        assertThat(retrievedItems.size()).isEqualTo(1);
        assertThat(retrievedItems.get(0).getName()).isEqualTo(item.getName());
    }

    @Test
    void testSearchItemsTextIsBlank() {

        List<ItemDto> retrievedItems = itemService.searchItems("");
        assertThat(retrievedItems.size()).isEqualTo(0);
    }


    @Test
    void testAddComment() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(eq(item.getId()))).thenReturn(Optional.of(item));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(bookingRepository.hasCompletedBookingsForItem(any(), any(), any(), any())).thenReturn(true);

        CommentDto addedComment = itemService.addComment(createCommentDto, user.getId(), item.getId());

        assertThat(addedComment.getText()).isEqualTo(createCommentDto.getText());
    }

    @Test
    void testAddCommentWithoutBooking() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(eq(item.getId()))).thenReturn(Optional.of(item));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(bookingRepository.hasCompletedBookingsForItem(any(), any(), any(), any())).thenReturn(false);

        assertThatThrownBy(() -> itemService.addComment(createCommentDto, user.getId(), item.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testAddCommentWithInvalidUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemService.addComment(createCommentDto, user.getId(), item.getId()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void testDeleteItem() {
        itemService.deleteItem(1L);
        verify(itemRepository, times(1)).deleteById(anyLong());
    }
}