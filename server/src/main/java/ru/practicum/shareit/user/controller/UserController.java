package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDtoRequestCreate;
import ru.practicum.shareit.user.dto.UserDtoRequestUpdate;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.service.UserService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserDtoResponse create(@Valid @RequestBody UserDtoRequestCreate user) {
        log.info("POST: запрос на создание пользователя: {}", user);
        return userService.create(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    public UserDtoResponse update(@PathVariable Long id, @Valid @RequestBody UserDtoRequestUpdate user) {
        log.info("PATCH: запрос на обновление пользователя с ID: {}, данные: {}", id, user);
        return userService.update(id, user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("DELETE: запрос на удаление пользователя с ID: {}", id);
        userService.delete(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public UserDtoResponse get(@PathVariable Long id) {
        log.info("GET: запрос на получение пользователя с ID: {}", id);
        return userService.get(id);
    }
}
