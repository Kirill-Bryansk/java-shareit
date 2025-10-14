package ru.practicum.shareit.user.controller;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validation.Creation;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto createUser(@Validated(Creation.class) @RequestBody UserDto userDto) {
        log.info("POST: запрос на создание пользователя: {}", userDto);
        return userService.createUser(userDto);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        log.info("PATCH: запрос на обновление пользователя: {}", userDto);
        userDto.setId(id);
        return userService.updateUser(userDto);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        log.info("GET: запрос на получение пользователя: {}", id);
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.info("DELETE: запрос на удаление пользователя: {}", id);
        userService.deleteUser(id);
    }
}


