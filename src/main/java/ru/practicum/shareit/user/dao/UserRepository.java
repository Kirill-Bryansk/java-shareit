package ru.practicum.shareit.user.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> userEmails = new HashSet<>();
    private final Logger log = LoggerFactory.getLogger(UserRepository.class);
    private final AtomicLong nextId = new AtomicLong(1L);

    public User save(User user) {
        if (users.containsKey(user.getId())) {
            throw new IllegalArgumentException("Пользователь с id {" + user.getId()+ "} уже существует." );
        } else if (userEmails.contains(user.getEmail())) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует: " + user.getEmail());
        }
        try {
            if (user.getId() == null) { // Если ID не задан, присваиваем новый
                user.setId(nextId.getAndIncrement());
                if (users.containsKey(user.getId())) {
                    throw new IllegalArgumentException("Пользователь с id {" + user.getId() + "} уже существует.");
                }
            }
            users.put(user.getId(), user); // Добавление нового пользователя
            userEmails.add(user.getEmail());
            System.out.println(users + "СОЗДАН");
            System.out.println(userEmails + "------");
            return user;
        } catch (Exception e) {
            // Общий блок для обработки других исключений
            log.error("Произошла ошибка при сохранении пользователя: {}", e.getMessage());
            throw e; // Перебрасываем исключение, чтобы оно могло быть обработано выше по стеку вызовов
        }
    }






    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    public User getUserById(Long id) {
        User existingUser = users.get(id);
        if (existingUser == null) {
            throw new IllegalArgumentException("Пользователь с id {" + user.getId() + "} не найден.");
        }
        return users.getOrDefault(id, null);
    }
    // хотел оптимизировать обновление. Пролема в выводимом сообщении. Получается исключение одно из трех а
    //вывод пользователь по id не найден. Надо развести эти ошибки, не знаю что лучше и правильнее NotFound а не
    // IllegalArgumentException. Но в поиск пользователя передается только id а я хочу что бы в сообщении выводился id
    // того кого не нашли

    public User updateUser(User user) {
        try {
            User existingUser = getUserById(user.getId());
            if (!(existingUser.equals(user)) && !(userEmails.contains(user.getEmail()))) {
                User updateUser = new User();
                updateUser.setId(user.getId());
                System.out.println("Id обновляемого " + user.getId());
                System.out.println(user.getEmail() + "333333333");
                System.out.println(user.getEmail());
                updateUser.setEmail(user.getEmail());
                System.out.println("//////////" + userEmails);
                updateUser.setName(user.getName());
                userEmails.remove(updateUser.getEmail());
                System.out.println(user + "ОБНОВЛЕН");
                users.replace(user.getId(), updateUser);
                System.out.println(userEmails + "------");
                return user;
            } else {
                throw new IllegalArgumentException("Пользователь с id {" + user.getId() + "} не найден.");
            }
        } catch (Exception e) {
            // Общий блок для обработки других исключений
            log.error("Произошла ошибка при обновлении пользователя: {}", e.getMessage());
            throw e; // Перебрасываем исключение, чтобы оно могло быть обработано выше по стеку вызовов
        }
    }


    public boolean deleteUser(Long id) {
       /* User userToDelete = getUserById(id);
        if (userToDelete.isPresent()) {
            users.remove(userToDelete.get());
            return true;
        } else {
            return false;
        }*/
        return false;
    }
}

