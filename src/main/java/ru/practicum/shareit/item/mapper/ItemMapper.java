package ru.practicum.shareit.item.mapper;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
            itemDto.setAvailableForRent(item.isAvailableForRent());
            if (item.getRequest() != null) {
                itemDto.setRequest(item.getRequest());
            }
            log.info("Объект Item успешно преобразован в ItemDto");
        } catch (Exception e) {
            log.error("Ошибка преобразования объекта Item в ItemDto", e);
        }

        return itemDto;
    }

    // ItemDto в Item
    public static Item fromItemDto(ItemDto itemDto) {
        Item item = new Item();
        try {
            BeanUtils.copyProperties(itemDto, item); // Копируем свойства
            log.info("Объект ItemDto успешно преобразован в Item");
        } catch (Exception e) {
            log.error("Ошибка преобразования объекта ItemDto в Item", e);
        }
        return item;
    }
}
