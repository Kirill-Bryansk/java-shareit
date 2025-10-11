package ru.practicum.shareit.item.mapper;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;


@Component
public class ItemMapper {

    private static final Logger log = LoggerFactory.getLogger(ItemMapper.class);

    // Item в ItemDto
    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        try {
            itemDto.setId(item.getId());
            itemDto.setName(item.getName());
            itemDto.setDescription(item.getDescription());
            itemDto.setAvailable(item.getAvailable());
            if (item.getRequest() != null) {
                itemDto.setRequest(item.getRequest());
            }
            log.debug("Объект Item успешно преобразован в ItemDto {}", itemDto);
        } catch (Exception e) {
            log.error("Ошибка преобразования объекта Item в ItemDto", e);
        }

        return itemDto;
    }

    // из ItemDto в Item
    public static Item fromItemDto(ItemDto itemDto) {
        Item item = new Item();
        try {
            BeanUtils.copyProperties(itemDto, item); // Копируем свойства
            log.debug("Объект ItemDto успешно преобразован в Item");
        } catch (Exception e) {
            log.error("Ошибка преобразования объекта ItemDto в Item", e);
        }
        return item;
    }

    public static void updateFields(Item itemUpdate, Item item) {
        try {
            if (Objects.isNull(itemUpdate.getAvailable())) {
                itemUpdate.setAvailable(item.getAvailable());
            }
            if (Objects.isNull(itemUpdate.getDescription())) {
                itemUpdate.setDescription(item.getDescription());
            }
            if (Objects.isNull(itemUpdate.getName())) {
                itemUpdate.setName(item.getName());
            }
            itemUpdate.setId(item.getId());
            itemUpdate.setRequest(item.getRequest());
            itemUpdate.setOwner(item.getOwner());
        } catch (Exception e) {
            log.error("Ошибка при обновлении свойств Item", e);
        }
    }
}
