package ru.practicum.shareit.user.mapper;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

@Component
public class UserMapper {
    private final Logger log = LoggerFactory.getLogger(UserMapper.class);

    // Метод преобразует объект User в объект UserDto.
    public UserDto toDto(User user) {
        if (user == null) {
            log.warn("Произошла попытка преобразования пустого объекта User");
            return null;
        }
        UserDto userDto = new UserDto(); // Создаем новый объект UserDto
        try {
            BeanUtils.copyProperties(user, userDto); // Копируем свойства из объекта user в userDto
            log.info("Объект User успешно преобразован в UserDto");
        } catch (Exception e) {
            log.error("Ошибка преобразования объекта User в UserDto", e);
        }
        return userDto;
    }

    // Метод преобразует объект UserDto в объект User.
    public User fromDto(UserDto userDto) {
        if (userDto == null) {
            log.warn("Произошла попытка преобразования пустого объекта UserDto");
            return null;
        }
        User user = new User(); // Создаем новый объект User
        try {
            BeanUtils.copyProperties(userDto, user); // Копируем свойства из объекта userDto в user
            log.info("Объект UserDto успешно преобразован в User");
        } catch (Exception e) {
            log.error("Ошибка преобразования объекта UserDto в User", e);
        }
        return user;
    }
}
