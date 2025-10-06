package ru.practicum.shareit.user.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private final List<User> users = new ArrayList<>();
    private final Logger log = LoggerFactory.getLogger(UserRepository.class);

    public User save(User user) {
        try {
            Optional<User> existingUser = users.stream()
                    .filter(u -> u.getId().equals(user.getId()))
                    .findFirst();

            if (existingUser.isPresent()) {
                int index = users.indexOf(existingUser.get());
                users.set(index, user); // Обновляет существующего пользователя
                System.out.println(users + "ОБНОВЛЕН");
            } else {
                users.add(user); // Добавление нового пользователя
                System.out.println(users+ "СОЗДАН");
            }
            return user;
        } catch (Exception e) {
            log.error("Произошла ошибка при сохранении пользователя: {}", e.getMessage());
            return null;
        }
    }



    public List<User> getAll() {
        return users;
    }

    public Optional<User> getUserById(Long id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public User updateUser(User user) {
        try {
            Optional<User> existingUser = getUserById(user.getId());
            if (existingUser.isPresent()) {
                int index = users.indexOf(existingUser.get());
                users.set(index, user);
                return user;
            } else {
                throw new IllegalArgumentException("Пользователь с указанным ID не найден");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Произошла ошибка при обновлении пользователя: " + e.getMessage());
            return null;
        }
    }


    public boolean deleteUser(Long id) {
        Optional<User> userToDelete = getUserById(id);
        if (userToDelete.isPresent()) {
            users.remove(userToDelete.get());
            return true;
        } else {
            return false;
        }
    }
}

