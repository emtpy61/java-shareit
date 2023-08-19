package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapperImpl;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
class ItemRequestServiceTest {
    private static Booking booking;
    private static User user;
    private static User booker;
    private static Item item;
    private static ItemRequest itemRequest;
    private static CreateItemRequestDto createItemRequestDto;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;
    @Autowired
    private ItemRequestMapperImpl itemRequestMapper;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(itemRequestService, "itemRequestMapper", itemRequestMapper);
        user = new User(1L, "User", "email@email.com");
        booker = new User(2L, "User2", "email2@email.com");
        item = new Item(1L, "Example item", "Example item", true, user, null,
                null, null);
        booking = new Booking(1L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), item, booker,
                BookingStatus.WAITING);
        createItemRequestDto = new CreateItemRequestDto("Item request description");
        itemRequest = new ItemRequest(1L, "Item request description", user, LocalDateTime.now(), null);
    }

    @Test
    public void testCreateAndGetBooking() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        ItemRequestDto createdItemRequest = itemRequestService.createItemRequest(createItemRequestDto, user.getId());
        ItemRequestDto retrievedItemRequest = itemRequestService.getItemRequestById(createdItemRequest.getId(),
                itemRequest.getId());
        assertThat(retrievedItemRequest).isEqualTo(createdItemRequest);
    }

    @Test
    public void testGetUserItemRequests() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(anyLong()))
                .thenReturn(List.of(itemRequest));

        List<ItemRequestDto> userItemRequests = itemRequestService.getUserItemRequests(user.getId());
        assertThat(userItemRequests.size()).isEqualTo(1);
        assertThat(userItemRequests.get(0).getDescription()).isEqualTo(itemRequest.getDescription());
    }

    @Test
    public void testGetOtherUsersItemRequests() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findByRequesterIdIsNot(anyLong(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(itemRequest)));

        List<ItemRequestDto> otherUsersItemRequests = itemRequestService.getOtherUsersItemRequests(user.getId(), 0, 10);
        assertThat(otherUsersItemRequests.size()).isEqualTo(1);
        assertThat(otherUsersItemRequests.get(0).getDescription()).isEqualTo(itemRequest.getDescription());
    }
}