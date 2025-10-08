package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public interface UserService {
    // Метод для создания нового пользователя
    UserDto createUser(UserDto userDto);

    // Метод для получения пользователя по идентификатору
    UserDto getUserById(Long id);

    // Метод для обновления пользователя
    UserDto updateUser(UserDto userDto);

    // Метод для удаления пользователя по идентификатору
    void deleteUser(Long id);
}

