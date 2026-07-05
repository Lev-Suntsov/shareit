package ru.practicum.shareit.request;

import java.util.List;

public interface RequestService {
    List<RequestDtoForGet> get(Long userId);
    List<RequestDtoForGet> getOtherUsersRequests(Long userId);
    RequestDtoForGet getById(Long requestId);
    RequestDto add(Long userId, RequestDto dto);
}
