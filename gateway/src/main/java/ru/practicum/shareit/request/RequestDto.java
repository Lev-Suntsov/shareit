package ru.practicum.shareit.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RequestDto {
    private Long id;
    @NotBlank(message = "Описание должно быть указанно")
    private String description;

    @NotNull(message = "id вещи должен быть указан")
    private Long itemId;

    @NotNull(message = "id пользователя необходимо указать")
    private Long userId;

    @NotNull
    private LocalDateTime created;
}
