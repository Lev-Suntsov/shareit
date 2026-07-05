package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    static ItemDto mapToItemDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setUserId(item.getUserId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.isAvailable());
        dto.setComments(item.getComments().stream().map(CommentMapper::toDto).toList());
        return dto;
    }

    static Item mapToItem(ItemDto dto, UserDto user) {
        Item item = new Item();
        item.setId(dto.getId());
        item.setAvailable(dto.getAvailable());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setUserId(dto.getUserId());
        item.setComments(dto.getComments().stream().map(commentDto ->  CommentMapper.toEntity(commentDto, ItemMapper.mapToItem(dto, user),
                UserMapper.mapToUser(user))).toList());
        return item;
    }
}
