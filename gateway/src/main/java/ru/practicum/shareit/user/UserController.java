package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private UserClient client;

    @PostMapping
    public ResponseEntity<Object> saveNewUser(@Valid @RequestBody UserDto user) {
        return client.create(user);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@Positive @PathVariable("userId") long userId, @RequestBody UserDto user) {
        return client.update(userId, user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@Positive @PathVariable("userId") long userId) {
        return client.getById(userId);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserById(@Positive @PathVariable("userId") long userId) {
        return client.delete(userId);
    }
}
