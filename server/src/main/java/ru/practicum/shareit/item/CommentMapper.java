package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommentMapper {

    public static CommentDto toDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }

    public static Comment toEntity(CommentDto dto, Item item, User author) {
        Comment comment = new Comment();
        comment.setId(dto.getId());
        comment.setText(dto.getText());
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(dto.getCreated());
        return comment;
    }
}
