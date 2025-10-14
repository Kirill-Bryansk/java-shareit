package ru.practicum.shareit.item.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final Map<Long, List<Item>> items = new HashMap<>();
    private final AtomicLong nextId = new AtomicLong(1L);

    public Item saveItem(Item item) {
        item.setId(nextId.getAndIncrement());
        if (items.containsKey(item.getOwner())) {
            items.get(item.getOwner()).add(item);
        } else {
            List<Item> ownerItemsList = new ArrayList<>();
            ownerItemsList.add(item);
            items.put(item.getOwner(), ownerItemsList);
        }
        return item;
    }

    public Item updateItem(Item item) {
        List<Item> userItems = items.get(item.getOwner());
        userItems = userItems.stream()
                .filter(existingItem -> !existingItem.getId().equals(item.getId()))
                .collect(Collectors.toList());
        userItems.add(item);
        return item;
    }

    public Item findItemById(Long itemId) {
        return items.values().stream()
                .flatMap(Collection::stream)
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Item с указанным ID {" + itemId + "} не найдена"));
    }

    public void checkOwnerOfItem(Long userId, Long itemId) {
        Item ownerItem = findItemById(itemId);
        Long owner = ownerItem.getOwner();
        if (!owner.equals(userId)) {
            throw new IllegalArgumentException(String.format("Пользователь с id %s не является владельцем вещи id %s.",
                    userId, itemId));
        }
    }

    public Item getItem(Long itemId) {
        Item item = findItemById(itemId);
        if (item == null) {
            throw new NotFoundException("Item с указанным ID " + itemId + " не найден");
        }
        return item;
    }

    public List<Item> getAllItemsFromUser(Long userId) {
        List<Item> allItems = items.get(userId);
        if (allItems == null) {
            throw new NotFoundException("Пользователь с ID " + userId + " не имеет связанных элементов или не найден");
        }
        return allItems;
    }

    public List<Item> getAllItems() {
        return items.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
