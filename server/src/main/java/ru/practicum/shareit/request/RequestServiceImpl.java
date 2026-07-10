package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl {
    private final UserServiceImpl userService;
    private final RequestRepository repository;
    private final ItemServiceImpl itemService;

    @Transactional
    public RequestDto add(Long userId, RequestDto dto) {
        UserDto user = userService.findUserById(userId);
        dto.setUserId(userId);
        dto.setCreated(LocalDateTime.now());
        return RequestMapper.mapToRequestDto(repository.save(RequestMapper.mapToRequest(dto)));
    }

    public List<RequestDtoForGet> get(Long userId) {
        userService.findUserById(userId);
        List<Request> requests = repository.findAllByUserIdOrderByCreatedDesc(userId);

        if (requests.isEmpty()) {
            return List.of();
        }

        return getItemDtoAndReturnRequest(requests);
    }

    public List<RequestDtoForGet> getOtherUsersRequests(Long userId) {
        userService.findUserById(userId);

        List<Request> requests = repository
                .findAllByUserIdNotOrderByCreatedDesc(userId);

        if (requests.isEmpty()) {
            return List.of();
        }

        return getItemDtoAndReturnRequest(requests);
    }

    public RequestDtoForGet getById(Long requestId) {
        Request request = repository.findById(requestId).orElseThrow();
        return RequestMapper.mapToRequestDtoForGet(request, itemService.findAllByRequestId(requestId));
    }

    private List<RequestDtoForGet> getItemDtoAndReturnRequest(List<Request> requests) {
        Set<Long> requestIds = requests.stream()
                .map(Request::getId)
                .collect(Collectors.toSet());

        Map<Long, List<ItemDto>> itemsByRequestId = itemService.findAllByRequestIdIn(requestIds).stream()
                .collect(Collectors.groupingBy(
                        ItemDto::getRequestId
                ));

        return requests.stream()
                .map(request -> RequestMapper.mapToRequestDtoForGet(
                        request,
                        itemsByRequestId.getOrDefault(request.getId(), List.of())
                ))
                .toList();
    }
}
