package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")

public class ItemController {
    private final ItemServiceImpl itemService;

    public ItemController(ItemServiceImpl itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<ItemDto> get(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAllItemsByOwner(userId);
    }

    @PostMapping
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") Long userId,
                    @RequestBody ItemDto item) {
        return itemService.addNewItem(userId, item);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable(name = "itemId") long itemId) {
        itemService.deleteItem(itemId);
    }

    @PatchMapping("/{itemId}")
    public  ItemDto update(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable(name = "itemId") long itemId,
                        @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable(name = "itemId") long itemId,
                            @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItem(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam("text") String text) {
        return itemService.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long itemId,
                                 @Valid @RequestBody CommentDto commentDto) {
        return itemService.addComment(userId, itemId, commentDto);
    }
}
