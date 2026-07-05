package ru.practicum.shareit.item;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingDtoOut;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
public class ItemDto {
    private Long id;
    private Long userId;
    private String name;
    private String description;
    private Boolean available;
    private BookingDtoOut lastBooking;
    private BookingDtoOut nextBooking;
    private List<CommentDto> comments = new ArrayList<>();
}
