package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient client;

    @GetMapping
    public ResponseEntity<Object> get(@Positive @RequestHeader("X-Sharer-User-Id") Long userId) {
        return client.getByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<Object> post(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @Valid @RequestBody ItemDtoCreate item) {
        return client.post(userId, item);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> delete(@Positive @PathVariable(name = "itemId") long itemId)  {
        return client.delete(itemId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@Positive @RequestHeader("X-Sharer-User-Id") long userId,
                                         @Positive @PathVariable(name = "itemId") long itemId,
                                         @Valid @RequestBody ItemDtoRequestUpdate itemDto) {
        return client.update(userId, itemId, itemDto);
    }


    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findById(@Positive @PathVariable(name = "itemId") long itemId,
                            @Positive @RequestHeader("X-Sharer-User-Id") long userId) {
        return client.findById(itemId, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam("text") String text) {
        return client.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable @Positive Long itemId,
                                             @Valid @RequestBody CommentDto commentDto) {
        return client.addComment(userId, itemId, commentDto);
    }
}
