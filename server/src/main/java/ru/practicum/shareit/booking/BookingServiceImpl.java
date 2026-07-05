package ru.practicum.shareit.booking;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.Exception.NotFoundException;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserServiceImpl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    private BookingRepository repository;
    private ItemRepository itemRepository;
    private UserServiceImpl userService;
    private ItemServiceImpl itemService;
    private UserRepository userRepository;

    @Override
    @Transactional
    public BookingDtoOut save(BookingDtoIn dto) {

        if (dto.getItemId() == null) {
            throw new IllegalArgumentException("id вещи не может быть пустым");
        }
        if (dto.getStart() == null || dto.getEnd() == null) {
            throw new IllegalArgumentException("Дата начала и конца обязательны");
        }

        if (!dto.getStart().before(dto.getEnd())) {
            throw new IllegalArgumentException("Дата окончания должна быть позже даты начала");
        }

        Item item = itemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь не найдена id = " + dto.getItemId()));
        userService.findUserById(dto.getBookerId());

        if (item.getUserId().equals(dto.getBookerId())) {
            throw new NotFoundException("Вещь не может бронировать владелец id указанного пользователя = " + dto.getBookerId());
        }

        UserDto user = userService.findUserById(dto.getBookerId());

        if (item.getAvailable() == null || !item.getAvailable()) {
            throw new IllegalArgumentException("Вещь недоступна для бронирования");
        }
        Booking booking = BookingMapper.fromDto(dto);
        booking.setItemId(item.getId());
        booking.setItemId(item.getId());
        booking.setBookerId(user.getId());
        booking.setStatus(BookingStatus.WAITING);

        Booking saved = repository.save(booking);

        return BookingMapper.toDtoForOut(saved, ItemMapper.mapToItemDto(item), user);
    }

    @Override
    @Transactional
    public BookingDtoOut approveBooking(Long bookingId, Long userId, boolean approved) {
        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено id = " + bookingId));

        ItemDto item = itemService.getItem(booking.getItemId(), booking.getBookerId());

        if (!item.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Подтверждать бронирование может только владелец вещи ваш id = " + userId);
        }

        if (booking.getStatus() == BookingStatus.APPROVED) {
            throw new ValidationException("Бронирование уже подтверждено");
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        item.setAvailable(true);
        Booking saved = repository.save(booking);

        return BookingMapper.toDtoForOut(
                saved,
                item,
                userService.findUserById(saved.getBookerId())
        );

    }

    @Override
    public BookingDtoOut getBooking(Long bookingId) {
        Booking booking = repository.getById(bookingId);
        return BookingMapper.toDtoForOut(booking, itemService.getItem(booking.getItemId(), booking.getBookerId()), userService.findUserById(booking.getBookerId()));
    }

    @Override
    public List<BookingDtoOut> getBookingsByUser(Long userId, BookingState state) {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        userService.findUserById(userId);
        List<Booking> bookings = switch (state) {
            case ALL -> repository.findAllByBookerId(userId);
            case CURRENT -> repository.findCurrentByBookerId(userId, now);
            case PAST ->  repository.findPastByBookerId(userId, now);
            case FUTURE -> repository.findFutureByBookerId(userId, now);
            case WAITING -> repository.findByBookerIdAndStatus(userId, BookingStatus.WAITING);
            case REJECTED ->  repository.findByBookerIdAndStatus(userId, BookingStatus.REJECTED);
        };


        Set<Long> itemIds = new HashSet<>();
        Set<Long> userIds = new HashSet<>();

        for (Booking b: bookings) {
            itemIds.add(b.getItemId());
            userIds.add(b.getBookerId());
        }

        List<ItemDto> items = itemService.findAllByIds(itemIds);
        List<UserDto> users = userService.findAllById(userIds.stream().toList());
        Map<Long, ItemDto> itemsById = items.stream()
                .collect(Collectors.toMap(
                        ItemDto::getId,
                        Function.identity()
                ));

        Map<Long, UserDto> usersById = users.stream()
                .collect(Collectors.toMap(
                        UserDto::getId,
                        Function.identity()
                ));

        return bookings.stream()
                .map(booking -> BookingMapper.toDtoForOut(
                        booking,
                        itemsById.get(booking.getItemId()),
                        usersById.get(booking.getBookerId())
                ))
                .toList();
    }

    @Override
    public List<BookingDtoOut> getBookingsByOwner(Long userId, BookingState state) {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        userService.findUserById(userId);

        List<Booking> bookings = switch (state) {
            case ALL -> repository.findOwnerBookings(userId);
            case CURRENT -> repository.findCurrentOwnerBookings(userId, now);
            case PAST -> repository.findPastOwnerBookings(userId, now);
            case FUTURE -> repository.findFutureOwnerBookings(userId, now);
            case WAITING -> repository.findWaitingOwnerBookings(userId);
            case REJECTED -> repository.findRejectedOwnerBookings(userId);
        };

        Set<Long> itemIds = new HashSet<>();
        Set<Long> userIds = new HashSet<>();

        for (Booking booking : bookings) {
            itemIds.add(booking.getItemId());
            userIds.add(booking.getBookerId());
        }

        List<ItemDto> items = itemService.findAllByIds(itemIds);
        List<UserDto> users = userService.findAllById(userIds.stream().toList());

        Map<Long, ItemDto> itemsById = items.stream()
                .collect(Collectors.toMap(
                        ItemDto::getId,
                        Function.identity()
                ));

        Map<Long, UserDto> usersById = users.stream()
                .collect(Collectors.toMap(
                        UserDto::getId,
                        Function.identity()
                ));

        return bookings.stream()
                .map(booking -> BookingMapper.toDtoForOut(
                        booking,
                        itemsById.get(booking.getItemId()),
                        usersById.get(booking.getBookerId())
                ))
                .toList();
    }
}
