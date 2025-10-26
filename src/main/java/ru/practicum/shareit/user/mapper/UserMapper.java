package ru.practicum.shareit.user.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.function.Consumer;

@Slf4j
@Component
public class UserMapper {

    // Метод преобразует объект User в объект UserDto.
    public static UserDto toDto(User user) {
        if (user == null) {
            log.warn("Произошла попытка преобразования пустого объекта User");
            return null;
        }
        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
        log.debug("Объект User успешно преобразован в UserDto");
        return userDto;
    }

    // Метод преобразует объект UserDto в объект User.
    public static User fromDto(UserDto userDto) {
        if (userDto == null) {
            log.warn("Произошла попытка преобразования пустого объекта UserDto");
            throw new IllegalArgumentException("UserDto не должен быть null");
        }
        User user = new User(); // Создаем новый объект User
        try {
            BeanUtils.copyProperties(userDto, user); // Копируем свойства из объекта userDto в user
            log.debug("Объект UserDto успешно преобразован в User");
        } catch (Exception e) {
            log.error("Ошибка преобразования объекта UserDto в User", e);
            throw e; // Перебрасываем исключение, чтобы вызывающая сторона могла его обработать
        }
        return user;
    }


    public void updateFields(User user, UserDto userDto) {
        log.debug("Начало обновления полей для пользователя с использованием UserDto. Имя: {}, Email: {}",
                userDto.getName(), userDto.getEmail());

        try {
            updateIfPresent(user::setName, userDto.getName());
            updateIfPresent(user::setEmail, userDto.getEmail());

            log.debug("Поля пользователя успешно обновлены. Новое имя: {}, новый Email: {}",
                    user.getName(), user.getEmail());
        } catch (Exception e) {
            log.error("Ошибка при обновлении полей пользователя", e);
        }
    }

    private void updateIfPresent(Consumer<String> setter, String value) {
        if (value != null && !value.isBlank()) {
            setter.accept(value);
        }
    }
}
