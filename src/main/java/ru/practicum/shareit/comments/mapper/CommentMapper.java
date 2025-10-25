package ru.practicum.shareit.comments.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.comments.dto.CommentDto;
import ru.practicum.shareit.comments.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Slf4j
@Component
public class CommentMapper {

    public CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        try {
            commentDto.setId(comment.getId());
            commentDto.setText(comment.getText());
            if (comment.getItem() != null) {
                commentDto.setItemId(comment.getItem().getId());
            }
            if (comment.getAuthor() != null) {
                commentDto.setAuthorName(comment.getAuthor().getName());
            }
            commentDto.setCreated(comment.getCreated());
            log.debug("Объект Comment успешно преобразован в CommentDto {}", commentDto);
        } catch (Exception e) {
            log.error("Ошибка преобразования объекта Comment в CommentDto", e);
        }

        return commentDto;
    }

    public static Comment toComment(CommentDto commentDto) {
        Comment comment = new Comment();
        try {
            comment.setId(commentDto.getId());
            comment.setText(commentDto.getText());
            if (commentDto.getItemId() != null) {
                Item item = new Item();
                item.setId(commentDto.getItemId());
                comment.setItem(item);
            }
            if (commentDto.getAuthorName() != null) {
                User author = new User();
                author.setName(commentDto.getAuthorName());
                comment.setAuthor(author);
            }
            comment.setCreated(commentDto.getCreated());
            log.debug("Объект CommentDto успешно преобразован в Comment {}", comment);
        } catch (Exception e) {
            log.error("Ошибка преобразования объекта CommentDto в Comment", e);
        }

        return comment;
    }
}
