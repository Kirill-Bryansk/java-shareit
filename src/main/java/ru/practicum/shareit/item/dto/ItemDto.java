package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class ItemDto {
    @NotNull(message = "ID не может быть пустым")
    private Long id;

    @NotEmpty(message = "Имя не может быть пустым")
    @Size(max = 50, message = "Имя должно содержать не более 50 символов")
    private String name;

    @Size(max = 255, message = "Описание должно содержать не более 255 символов")
    private String description;

    // Статус о том, доступна или нет вещь для аренды
    private boolean availableForRent;

    @NotNull(message = "Владелец не может быть пустым")
    private User owner;

    private Request request;
}

