package ru.practicum.shareit.booking.contoller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequestCreate;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingService bookingService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BookingDtoResponse create(
            @RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId,
            @Valid @RequestBody BookingDtoRequestCreate booking
    ) {
        log.info("POST: запрос на создание бронирования от пользователя с ID: {}, данные: {}", userId, booking);
        return bookingService.create(userId, booking);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{bookingId}")
    public BookingDtoResponse approve(
            @RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId,
            @PathVariable @Positive Long bookingId,
            @RequestParam("approved") Boolean approved
    ) {
        log.info("PATCH: запрос на утверждение бронирования с ID: {}, от пользователя с ID: {}, статус: {}", bookingId, userId, approved);
        return bookingService.approve(userId, bookingId, approved);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{bookingId}")
    public BookingDtoResponse get(@RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId, @PathVariable @Positive Long bookingId) {
        log.info("GET: запрос на получение бронирования с ID: {} от пользователя с ID: {}", bookingId, userId);
        return bookingService.get(userId, bookingId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<BookingDtoResponse> getAllBookingCurrentUser(
            @RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId,
            @RequestParam(name = "state", defaultValue = "ALL") BookingState state
    ) {
        log.info("GET: запрос на получение всех бронирований пользователя с ID: {}, состояние: {}", userId, state);
        return bookingService.getAllBookingCurrentUser(userId, state);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/owner")
    public List<BookingDtoResponse> getAllBookingAllItemCurrentUser(
            @RequestHeader(value = "X-Sharer-User-Id") @Positive Long userId,
            @RequestParam(name = "state", defaultValue = "ALL") BookingState state
    ) {
        log.info("GET: запрос на получение всех бронирований всех предметов пользователя с ID: {}, состояние: {}", userId, state);
        return bookingService.getAllBookingAllItemCurrentUser(userId, state);
    }
}
