package ru.practicum.shareit.item;

import java.util.List;
import java.util.Set;

public interface ItemService {

    ItemDto addNewItem(long userId, ItemDto item);

    void deleteItem(long itemId);

    ItemDto updateItem(Long userId, long itemId, ItemDto item);

    public ItemDto getItem(long itemId, long ownerId);

    List<ItemDto> search(String text);

    CommentDto addComment(Long userId, Long itemId, CommentDto comment);

    List<ItemDto> getItemById(Long userId, Long itemId);

    List<ItemDto> getAllItemsByOwner(Long ownerId);

    List<ItemDto> findAllByIds(Set<Long> ids);
}
