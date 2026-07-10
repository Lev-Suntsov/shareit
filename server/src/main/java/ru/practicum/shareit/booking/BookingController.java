package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {
    private final BookingServiceImpl service;

    @PostMapping
    public BookingDtoOut save(@RequestBody BookingDtoIn dto,
                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        dto.setBookerId(userId);
        return service.save(dto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOut saveApproved(@PathVariable Long bookingId,
                                     @RequestParam boolean approved,
                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        return service.approveBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoOut getBooking(@PathVariable Long bookingId) {
        return service.getBooking(bookingId);
    }

    @GetMapping
    public List<BookingDtoOut> getBookingByUser(@RequestParam(defaultValue = "ALL") BookingState state,
                                               @RequestHeader("X-Sharer-User-Id") Long userId) {
        return service.getBookingsByUser(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDtoOut> getBookingsByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @RequestParam(defaultValue = "ALL") BookingState state) {
        return service.getBookingsByOwner(userId, state);
    }
}