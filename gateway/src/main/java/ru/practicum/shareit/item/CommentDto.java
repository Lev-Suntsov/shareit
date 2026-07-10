package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class CommentDto {
    private Long id;
    @NotBlank(message = "текст комментария не может быть пустым")
    @NotNull(message = "текст комментария не может быть пустым")
    private String text;

    private String authorName;

    private Timestamp created;
}
