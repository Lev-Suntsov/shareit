package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.item.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RequestDtoForGet {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemDto> items;
    private String itemName;
    private Long userId;
}
