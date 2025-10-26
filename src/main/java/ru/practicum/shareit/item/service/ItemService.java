package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;

import java.util.List;

public interface ItemService {
     ItemDtoOut addItem(Long userId, ItemDto itemDto);

     ItemDtoOut updateItem(Long userId, ItemDto itemDto, Long itemId);

     ItemDtoOut getItem(Long userId, Long itemId);

     List<ItemDtoOut> getItemsForUser(Long userId);

     List<ItemDtoOut> searchItems(Long userId, String text);

     CommentDtoOut createComment(Long userId, CommentDto commentDto, Long itemId);
}
