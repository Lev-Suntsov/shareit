package ru.practicum.shareit.request;

import ru.practicum.shareit.item.ItemDto;

import java.util.ArrayList;

public interface RequestMapper {
    static RequestDto mapToRequestDto(Request request){
        RequestDto dto = new RequestDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setItemId(request.getItemId());
        dto.setUserId(request.getUserId());
        dto.setCreated(request.getCreated());
        return dto;
    }

    static Request mapToRequest(RequestDto dto){
        Request request = new Request();
        request.setId(dto.getId());
        request.setItemId(dto.getItemId());
        request.setDescription(dto.getDescription());
        request.setUserId(dto.getUserId());
        request.setCreated(dto.getCreated());
        return request;
    }

    static RequestDtoForGet mapToRequestDtoForGet(Request request, ItemDto item){
        RequestDtoForGet dto = new RequestDtoForGet();
        dto.setId(request.getId());
        dto.setCreated(request.getCreated());
        ArrayList<ItemDto> itemDtos = new ArrayList<>();
        itemDtos.add(item);
        if(item != null) {
            dto.setItemName(item.getName());
            dto.setItems(itemDtos);
        }
        dto.setDescription(request.getDescription());
        dto.setUserId(request.getUserId());
        return dto;
    }
}
