package ru.practicum.shareit.item.dto;


public record ItemDtoRequestCreate(
        String name,
        String description,
        Boolean available,
        Long requestId
) {
}
