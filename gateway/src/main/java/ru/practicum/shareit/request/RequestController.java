package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RestController
@RequestMapping("/requests")
@AllArgsConstructor
public class RequestController {

    private final RequestClient client;

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody RequestDto dto) {
        return client.save(userId, dto);
    }

    @GetMapping
    public ResponseEntity<Object> get(@NonNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        return client.get(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getUserUserRequest(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return client.getAll(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findRequestById(@PathVariable Long requestId) {
        return client.findById(requestId);
    }
}
