package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.util.List;

import ru.practicum.shareit.validation.Update;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    public static final String USER_HEADER = "X-Sharer-User-Id";
    private final ItemServiceImpl itemService;

    @PostMapping
    public ItemDtoOut addItem(@RequestHeader(USER_HEADER) Long userId, @Valid @RequestBody ItemDto itemDto) {
        log.info("POST: добавление предмета {}. Добавляет пользователь с id {{}}", itemDto.toString(), userId);
        return itemService.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDtoOut updateItem(@RequestHeader(USER_HEADER) Long userId,
                              @Validated(Update.class) @RequestBody ItemDto itemDto,
                              @PathVariable Long itemId) {
        log.info("PATCH: обновление предмета {}. Id предмета {{}}. Пользователем с id {{}}.",
                itemDto.toString(), itemId, userId);
        return itemService.updateItem(userId, itemDto, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoOut getItem(@RequestHeader(USER_HEADER) Long userId, @PathVariable Long itemId) {
        log.info("GET: пользователь с id {{}}, хочет получить информацию о предмете с id {{}}", userId, itemId);
        return itemService.getItem(userId, itemId);
    }

    @GetMapping
    public List<ItemDtoOut> getItemsForUser(@RequestHeader(USER_HEADER) Long userId) {
        log.info("GET: запрос для получения списка вещей определенного пользователя по его id {{}}", userId);
        return itemService.getItemsForUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDtoOut> searchItems(@RequestHeader(USER_HEADER) Long userId,
                                     @RequestParam(name = "text") String text) {
        log.info("GET: запрос на поиск предмета");
        return itemService.searchItems(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoOut createComment(@RequestHeader(USER_HEADER) Long userId,
                                       @Validated @RequestBody CommentDto commentDto,
                                       @PathVariable Long itemId) {
        log.info("POST: запрос на создание комментария к предмету с id {}", itemId);
        return itemService.createComment(userId, commentDto, itemId);
    }
}