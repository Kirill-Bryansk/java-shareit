package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
     ItemDto addItem(Long userId, ItemDto itemDto);

     ItemDto updateItem(Long userId, ItemDto itemDto, Long itemId);

     ItemDto getItem(Long userId, Long itemId);

     List<ItemDto> getItemsForUser(Long userId);

     List<ItemDto> searchItems(Long userId, String query);
}
