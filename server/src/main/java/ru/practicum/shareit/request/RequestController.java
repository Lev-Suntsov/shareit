package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestServiceImpl service;
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public RequestDto create(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody RequestDto request
    ) {
        return service.add(userId, request);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<RequestDtoForGet> getAllRequestByUserId(@RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return service.get(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{requestId}")
    public RequestDtoForGet getRequestById(
            @PathVariable long requestId
    ) {
        return service.getById(requestId);
    }

}
