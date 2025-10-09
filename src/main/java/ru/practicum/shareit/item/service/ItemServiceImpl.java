package ru.practicum.shareit.item.service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService{

    @Override
    public ItemDto addItem(Long userId, ItemDto itemDto) {
            return null;
    }

    @Override
    public ItemDto updateItem(Long userId, ItemDto itemDto, Long itemId) {
        return null;
    }

    @Override
    public ItemDto getItem(Long userId, Long itemId) {
        return null;
    }

    @Override
    public List<ItemDto> getItemsForUser(Long userId) {
        return List.of();
    }

    @Override
    public List<ItemDto> searchItems(Long userId, String query) {
        return List.of();
    }
}
