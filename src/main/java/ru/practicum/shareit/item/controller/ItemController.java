package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    public static final String USER_HEADER = "X-Sharer-User-Id"; // X-Sharer-User-Id
    private final ItemServiceImpl itemService;


    @PostMapping
    public ResponseEntity<ItemDto> addItem(@RequestHeader(USER_HEADER)
                                            Long userId, @Valid @RequestBody ItemDto itemDto) {
        log.info("POST: добавление предмета {}. Добавляет пользователь с id {{}}", itemDto.toString(), userId);
        return ResponseEntity.ok(itemService.addItem(userId, itemDto));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem(@RequestHeader(USER_HEADER) Long userId,
                                              @RequestBody ItemDto itemDto,
                                              @PathVariable Long itemId) {
        log.info("PATCH: обновление предмета {}. Id предмета {{}}. Пользователем с id {{}}.",
                itemDto.toString(), itemId, userId);
        return ResponseEntity.ok(itemService.updateItem(userId, itemDto, itemId));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> getItem(@RequestHeader(USER_HEADER) Long userId, @PathVariable Long itemId) {
        log.info("GET: пользователь с id {{}}, хочет получить информацию о предмете с id {{}}", userId, itemId);
        return ResponseEntity.ok(itemService.getItem(userId, itemId));
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getItemsForUser(@RequestParam(USER_HEADER) Long userId) {
        log.info("GET: запрос для получения списка вещей определенного пользователя по его id {{}}", userId);
        return ResponseEntity.ok(itemService.getItemsForUser(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> searchItems(@RequestHeader Long userId, @RequestParam String query) {
        log.info("GET: запрос на поиск предмета");
        return ResponseEntity.ok(itemService.searchItems(userId, query));
    }

}
