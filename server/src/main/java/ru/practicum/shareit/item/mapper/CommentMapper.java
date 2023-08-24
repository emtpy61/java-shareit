package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.model.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "item", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "author", ignore = true)
    Comment createCommentDtotoComment(CreateCommentDto createCommentDto);

    @Mapping(target = "authorName", source = "author.name")
    CommentDto commentToCommentDto(Comment comment);

    List<CommentDto> commentsToCommentDtos(List<Comment> comments);

    default LocalDateTime stringToLocalDateTime(String stringTime) {
        return LocalDateTime.parse(stringTime);
    }
}
