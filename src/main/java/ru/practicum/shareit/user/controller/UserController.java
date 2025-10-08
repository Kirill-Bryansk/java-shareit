package ru.practicum.shareit.user.controller;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.validation.Creation;

@RestController
@RequestMapping(path = "/users")
public class  UserController {

    private final UserService userService;
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Validated(Creation.class) @RequestBody UserDto userDto) {
        log.info("Поступление запроса на создание пользователя: {}", userDto);
        UserDto createdUser = userService.createUser(userDto);
        return ResponseEntity.ok(createdUser);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        log.info("Поступление запроса на обновление пользователя: {}", userDto);
        userDto.setId(id);
        UserDto updateUserDto = userService.updateUser(userDto);
        return ResponseEntity.ok(updateUserDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        log.info("Поступление запроса на получение пользователя: {}", id);
        UserDto getUserDto = userService.getUserById(id);
        return ResponseEntity.ok(getUserDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.info("Поступление запроса на удаление пользователя: {}", id);
        userService.deleteUser(id);
    }
}

