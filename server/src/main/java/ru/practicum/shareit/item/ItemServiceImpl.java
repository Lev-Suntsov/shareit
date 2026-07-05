package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Exception.NotFoundException;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.user.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository repository;
    private final UserServiceImpl userService;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ItemDto addNewItem(long userId, ItemDto itemDto) {
        userService.findUserById(userId);
        itemDto.setUserId(userId);

        Item item = ItemMapper.mapToItem(itemDto, userService.findUserById(userId));
        return ItemMapper.mapToItemDto(repository.save(item));
    }

    @Transactional
    @Override
    public void deleteItem(long itemId) {
        repository.delete(repository.findById(itemId).orElseThrow());
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long userId, long itemId, ItemDto item) {
        Item existingItem = repository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        if (!existingItem.getUserId().equals(userId)) {
            throw new NotFoundException("Пользователь не является владельцем вещи");
        }

        if (item.getName() != null) {
            existingItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            existingItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            existingItem.setAvailable(item.getAvailable());
        }

        Item updatedItem = repository.save(existingItem);
        return ItemMapper.mapToItemDto(updatedItem);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto getItem(long itemId, long requesterId) {
        Item item = repository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        List<CommentDto> comments = commentRepository.findAllByItem_IdOrderByCreatedDesc(itemId)
                .stream()
                .map(CommentMapper::toDto)
                .toList();

        ItemDto dto = ItemMapper.mapToItemDto(item);
        dto.setComments(comments);

        Timestamp now = Timestamp.valueOf(LocalDateTime.now());

        if (item.getUserId().equals(requesterId)) {
            bookingRepository.findFirstByItemIdAndStartBeforeOrderByStartDesc(itemId, now)
                    .ifPresent(booking -> dto.setLastBooking(BookingMapper.toDtoForOut(booking)));
            // при необходимости сюда можно добавить nextBooking, если есть отдельный метод
        }

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getAllItemsByOwner(Long ownerId) {

        List<Item> items = repository.findAllByUserIdOrderByIdAsc(ownerId);

        if (items.isEmpty()) {
            return List.of();
        }

        List<Long> itemIds = items.stream()
                .map(Item::getId)
                .toList();

        Map<Long, List<CommentDto>> commentsByItemId = commentRepository
                .findAllByItem_IdInOrderByItem_IdAscCreatedDesc(itemIds)
                .stream()
                .collect(Collectors.groupingBy(
                        comment -> comment.getItem().getId(),
                        Collectors.mapping(CommentMapper::toDto, Collectors.toList())
                ));

        List<BookingDtoOut> bookings = bookingRepository
                .findAllByItemIdInAndStatusOrderByStartAsc(itemIds, BookingStatus.APPROVED)
                .stream()
                .map(BookingMapper::toDtoForOut)
                .toList();

        Timestamp now = new Timestamp(System.currentTimeMillis());

        Map<Long, BookingDtoOut> lastBookingByItemId = new HashMap<>();
        Map<Long, BookingDtoOut> nextBookingByItemId = new HashMap<>();

        for (BookingDtoOut booking : bookings) {
            Long itemId = booking.getItem().getId();

            if (!booking.getStart().after(now)) {
                lastBookingByItemId.put(itemId, booking);
            } else {
                nextBookingByItemId.putIfAbsent(itemId, booking);
            }
        }

        return items.stream()
                .map(item -> {
                    ItemDto dto = ItemMapper.mapToItemDto(item);
                    dto.setComments(commentsByItemId.getOrDefault(item.getId(), List.of()));
                    dto.setLastBooking(lastBookingByItemId.get(item.getId()));
                    dto.setNextBooking(nextBookingByItemId.get(item.getId()));
                    return dto;
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> search(String text) {

        if (text == null || text.isBlank()) {
            return List.of();
        }

        return repository.search(text).stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    @Transactional
    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {

        Item item = repository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Timestamp now = Timestamp.valueOf(LocalDateTime.now());

        boolean hasFinishedBooking = bookingRepository.existsByItemIdAndBookerIdAndStatusAndEndBefore(
                itemId,
                userId,
                BookingStatus.APPROVED,
                now
        );

        if (!hasFinishedBooking) {
            throw new IllegalArgumentException("User has no finished booking for this item"); // вернётся 400
        }

        item.setId(itemId);
        Comment savedComment = commentRepository.save(new Comment(
                null,
                commentDto.getText(),
                item,
                author,
                now
        ));
        item.getComments().add(savedComment);

        return CommentMapper.toDto(savedComment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getItemById(Long userId, Long itemId) {
        Item item = repository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        return repository.findById(itemId)
                .stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public List<ItemDto> findAllByIds(Set<Long> ids) {
        return  repository.findAllById(ids).stream().map(ItemMapper::mapToItemDto).toList();
    }
}
