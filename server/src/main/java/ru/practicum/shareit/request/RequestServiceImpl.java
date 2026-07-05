package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final UserServiceImpl userService;
    private final RequestRepository repository;
    private final ItemServiceImpl itemService;

    @Transactional
    @Override
    public RequestDto add(Long userId, RequestDto dto) {
        UserDto user = userService.findUserById(userId);
        dto.setUserId(userId);
        dto.setCreated(LocalDateTime.now());
        return RequestMapper.mapToRequestDto(repository.save( RequestMapper.mapToRequest(dto)));
    }

    @Override
    public List<RequestDtoForGet> get(Long userId) {
        userService.findUserById(userId);
        List<Request> requests = repository.findAllByUserIdOrderByCreatedDesc(userId);

        if (requests.isEmpty()) {
            return List.of();
        }

        return getItemDtoAndReturnRequest(requests);
    }

    @Override
    public List<RequestDtoForGet> getOtherUsersRequests(Long userId) {
        userService.findUserById(userId);

        List<Request> requests = repository
                .findAllByUserIdNotOrderByCreatedDesc(userId);

        if(requests.isEmpty()) {
            return List.of();
        }

        return getItemDtoAndReturnRequest(requests);
    }

    @Override
    public RequestDtoForGet getById(Long requestId){
        Request request = repository.findById(requestId).orElseThrow();
        ItemDto item = null;
        if (request.getItemId() != null) {
            item = itemService.getItem(request.getItemId(), request.getUserId());
        }

        return RequestMapper.mapToRequestDtoForGet(request, item);
    }

    private  List<RequestDtoForGet> getItemDtoAndReturnRequest(List<Request> requests){
        Set<Long> itemIds = requests.stream().map(Request::getItemId).filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, ItemDto> itemsById = itemService.findAllByIds(itemIds).stream().collect(Collectors.toMap(ItemDto::getId, item -> item));

        return requests.stream().map(
                request -> RequestMapper.mapToRequestDtoForGet(request, itemsById.get(request.getItemId()))
        ).toList();
    }
}
