package ru.practicum.shareit.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestDto {
    private Long id;
    private String description;
    private Long itemId;
    private Long userId;
    private LocalDateTime created;
}
