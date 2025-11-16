package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

public record BookingDtoRequestCreate(
        Long itemId,
        LocalDateTime start,
        LocalDateTime end
) {
}
