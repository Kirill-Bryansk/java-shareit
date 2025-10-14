package ru.practicum.shareit.user.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> userEmails = new HashSet<>();
    private final AtomicLong nextId = new AtomicLong(1L);

    public User save(User user) {
        if (users.containsKey(user.getId())) {
            throw new IllegalArgumentException("Пользователь с id {" + user.getId() + "} уже существует.");
        }
        checkEmailUniqueness(user.getEmail());

        if (user.getId() == null) {
            user.setId(nextId.getAndIncrement());
        }

        users.put(user.getId(), user);
        userEmails.add(user.getEmail());

        return user;
    }

    public User updateUser(User user) {
        User existingUser = getUserById(user.getId());

        String existingEmail = existingUser.getEmail();
        if (!existingEmail.equals(user.getEmail())) {
            checkEmailUniqueness(user.getEmail()); // проверка email
            existingUser.setEmail(user.getEmail());
            userEmails.remove(existingEmail); // Удаляем старый email
            userEmails.add(user.getEmail());  // Добавляем новый email
        }

        existingUser.setName(user.getName());
        users.replace(user.getId(), existingUser);
        return existingUser;
    }

    public User getUserById(Long id) {
        User existingUser = users.get(id);
        if (existingUser == null) {
            throw new NotFoundException("Пользователь с id {" + id + "} не найден.");
        }
        return existingUser;
    }

    public void deleteUser(Long id) {
        users.remove(id);
    }

    private void checkEmailUniqueness(String email) {
        if (userEmails.contains(email)) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует: " + email);
        }
    }

    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }
}

