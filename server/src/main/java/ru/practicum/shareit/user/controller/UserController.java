package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
/*import ru.practicum.shareit.user.dto.UserDtoCreate;
import ru.practicum.shareit.user.dto.UserDtoUpdate;*/
import ru.practicum.shareit.user.service.UserService;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
        log.info("POST: запрос на создание пользователя: {}", userDto);
        return userService.createUser(userDto);
    }

   /* @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @Valid @RequestBody UserDtoUpdate updateUserDto) {
        log.info("PATCH: запрос на обновление пользователя: {}", updateUserDto);
        return userService.updateUser(id, updateUserDto);
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
    }*/
}


