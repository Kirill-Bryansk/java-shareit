package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserServiceImpl userService;
    private final ItemRepository itemRepository;
    private final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);

    @Override
    public ItemDto addItem(Long userId, ItemDto itemDto) {
        UserDto user = userService.getUserById(userId);
        Item item = ItemMapper.fromItemDto(itemDto);
        item.setOwner(user.getId());
        log.debug("Сохраненная вещь пользователя СЕРВИС addItem {}", item);
        return ItemMapper.toItemDto(itemRepository.saveItem(item));
    }

    @Override
    public ItemDto updateItem(Long userId, ItemDto itemDto, Long itemId) {
        log.debug("Параметры обновления  item : {}", itemDto);

        UserDto user = userService.getUserById(userId);
        Item item = itemRepository.findItemById(itemId);
        itemRepository.checkOwnerOfItem(user.getId(), itemId);
        Item itemUpdate = ItemMapper.fromItemDto(itemDto);
        ItemMapper.updateFields(itemUpdate, item);

        log.debug("Обновленный item {}", itemUpdate);
        return ItemMapper.toItemDto(itemRepository.updateItem(itemUpdate));
    }

    @Override
    public ItemDto getItem(Long userId, Long itemId) {
        return ItemMapper.toItemDto(itemRepository.getItem(itemId));
    }

    @Override
    public List<ItemDto> getItemsForUser(Long userId) {
        List<Item> allItemsFromUser = itemRepository.getAllItemsFromUser(userId);
        return allItemsFromUser.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(Long userId, String text) {
        if (text == null || text.isEmpty()) {
            return Collections.emptyList();
        }
        List<Item> allItems = itemRepository.getAllItems();
        log.debug("Список всех items {}", allItems);
        log.debug("Название предмета в запросе для поиска {}", text);
        List<Item> foundItems = allItems.stream()
                .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase()))
                        && item.getAvailable())
                .collect(Collectors.toList());
        log.debug("Список найденных items по запросу item {}", foundItems);
        return foundItems.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
