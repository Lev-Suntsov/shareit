package ru.practicum.shareit.item;


public record ItemDtoRequestUpdate(
        String name,
        String description,
        Boolean available,
        ItemDtoCreate request
) {
}
