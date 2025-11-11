package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDtoRequestCreate;
import ru.practicum.shareit.comment.dto.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDtoRequestCreate;
import ru.practicum.shareit.item.dto.ItemDtoRequestUpdate;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.dto.ItemDtoResponseWithBookings;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ItemDtoResponse create(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Validated @RequestBody ItemDtoRequestCreate item
    ) {
        log.info("POST: запрос на создание item от пользователя с ID: {}, данные: {}", userId, item);
        return itemService.create(userId, item);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{itemId}")
    public ItemDtoResponse update(
            @PathVariable Long itemId,
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Validated @RequestBody ItemDtoRequestUpdate item
    ) {
        log.info("PATCH: запрос на обновление item с ID: {}, данные: {}, пользователь с ID: {}", itemId, item, userId);
        return itemService.update(itemId, userId, item);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{itemId}")
    public void delete(@PathVariable Long itemId) {
        log.info("DELETE: запрос на удаление item с ID: {}", itemId);
        itemService.delete(itemId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{itemId}")
    public ItemDtoResponseWithBookings getById(
            @RequestHeader("X-Sharer-User-Id") Long requesterId,
            @PathVariable Long itemId
    ) {
        log.info("GET: запрос на получение item с ID: {} от пользователя с ID: {}", itemId, requesterId);
        return itemService.getById(requesterId, itemId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ItemDtoResponse> getByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("GET: запрос на получение списка items для пользователя с ID: {}", userId);
        return itemService.getByUserId(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    public List<ItemDtoResponse> search(@RequestParam String text) {
        log.info("GET: запрос на поиск items по тексту: {}", text);
        if (!StringUtils.hasText(text)) {
            return List.of();
        }
        return itemService.search(text);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{itemId}/comment")
    public CommentDtoResponse addComment(
            @RequestHeader("X-Sharer-User-Id") @Positive Long userId,
            @PathVariable @Positive Long itemId,
            @Valid @RequestBody CommentDtoRequestCreate comment
    ) {
        log.info("POST: запрос на добавление комментария к item с ID: {}, от пользователя с ID: {}, данные: {}", itemId, userId, comment);
        return itemService.createComment(userId, itemId, comment);
    }
}
