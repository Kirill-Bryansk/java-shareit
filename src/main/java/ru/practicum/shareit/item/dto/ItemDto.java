package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.request.model.Request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import ru.practicum.shareit.validation.Creation;
import ru.practicum.shareit.validation.Update;

@Data
public class ItemDto {

    private Long id;

    @NotEmpty(message = "Имя item не может быть пустым", groups = {Creation.class})
    @Size(max = 50, message = "Имя item должно содержать не более 50 символов", groups = {Creation.class, Update.class})
    private String name;

    @NotEmpty(message = "Описание item не может быть пустым", groups = {Creation.class})
    @Size(max = 255, message = "Описание должно содержать не более 255 символов",
            groups = {Creation.class, Update.class})
    private String description;

    // Статус о том, доступна или нет вещь для аренды
    @NotNull(message = "Статус item должен быть указан", groups = {Creation.class})
    private Boolean available;

    private Request request;
}

