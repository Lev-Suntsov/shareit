package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;

import java.util.List;

public interface BookingService {
    BookingDtoOut save(BookingDtoIn dto);

    BookingDtoOut approveBooking(Long bookingId, Long userId, boolean approved);

    BookingDtoOut getBooking(Long bookingId);

    List<BookingDtoOut> getBookingsByUser(Long userId, BookingState state);

    List<BookingDtoOut> getBookingsByOwner(Long userId, BookingState state);
}
