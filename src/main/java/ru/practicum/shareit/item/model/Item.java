package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {
    // Уникальный идентификатор вещи
    private long id;

    // Краткое название вещи
    private String name;

    // Развёрнутое описание вещи
    private String description;

    // Статус о том, доступна или нет вещь для аренды
    private boolean availableForRent;

    // Владелец вещи
    private User owner;

    // Если вещь была создана по запросу другого пользователя,
    // то в этом поле будет храниться ссылка на соответствующий запрос
    private Request request;
}


