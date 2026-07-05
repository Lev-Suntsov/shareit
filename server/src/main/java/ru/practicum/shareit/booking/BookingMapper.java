package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.user.UserDto;

@Mapper(componentModel = "spring")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {

    public static BookingDtoIn toDto(Booking booking) {
        BookingDtoIn dto = new BookingDtoIn();
        dto.setId(booking.getId());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        dto.setStatus(booking.getStatus().name());
        return dto;
    }

    public static Booking fromDto(BookingDtoIn dto) {
        Booking booking = new Booking();
        booking.setId(dto.getId());
        booking.setStart(dto.getStart());
        booking.setEnd(dto.getEnd());
        booking.setItemId(dto.getItemId());
        booking.setBookerId(dto.getBookerId());
        return booking;
    }

    public static BookingDtoOut toDtoForOut(Booking booking, ItemDto itemDto, UserDto userDto) {
        BookingDtoOut dto = new BookingDtoOut();

        dto.setId(booking.getId());
        dto.setEnd(booking.getEnd());
        dto.setStart(booking.getStart());
        dto.setBooker(userDto);
        dto.setItem(itemDto);
        dto.setStatus(booking.getStatus());
        return dto;

    }

    public static BookingDtoOut toDtoForOut(Booking booking) {
        BookingDtoOut dto = new BookingDtoOut();

        dto.setId(booking.getId());
        dto.setEnd(booking.getEnd());
        dto.setStart(booking.getStart());
        dto.setStatus(booking.getStatus());

        return dto;
    }
}