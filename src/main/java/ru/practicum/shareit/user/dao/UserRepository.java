package ru.practicum.shareit.user.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository {
    private final List<User> users = new ArrayList<>();
    private final Logger log = LoggerFactory.getLogger(UserRepository.class);
    private final AtomicLong nextId = new AtomicLong(1L);

    public User save(User user) {
        if (users.stream()
                .anyMatch(u -> Objects.equals(u.getId(), user.getId()))) {
            throw new IllegalArgumentException("Пользователь с id {" + user.getId()+ "} уже существует." );
        } else if ( users.stream()
                .anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует: " + user.getEmail());
        }
        try {
            if (user.getId() == null) { // Если ID не задан, присваиваем новый
                user.setId(nextId.getAndIncrement());
            }
            users.add(user); // Добавление нового пользователя
            System.out.println(users + "СОЗДАН");
            return user;
        } catch (Exception e) {
            // Общий блок для обработки других исключений
            log.error("Произошла ошибка при сохранении пользователя: {}", e.getMessage());
            throw e; // Перебрасываем исключение, чтобы оно могло быть обработано выше по стеку вызовов
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

