package ru.practicum.shareit.request.dto;


import java.time.LocalDateTime;

public record ItemRequestDtoRequestCreate(
        String description,
        LocalDateTime created
) {
}