package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class CommentDto {
    private Long id;

    private String text;

    private String authorName;

    private Timestamp created;
}
