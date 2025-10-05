package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

public interface UserService {
    // Метод для создания нового пользователя
    User createUser(User user);

    // Метод для получения пользователя по идентификатору
    User getUserById(Long id);

    // Метод для обновления пользователя
    User updateUser(User user);

    // Метод для удаления пользователя по идентификатору
    void deleteUser(Long id);
}

