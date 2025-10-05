package ru.practicum.shareit.user.mapper;

import org.springframework.beans.BeanUtils;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

/**
 * Класс UserMapper предназначен для преобразования объектов User в UserDto и наоборот.
 */
public class UserMapper {
    /**
     * Метод преобразует объект User в объект UserDto.
     *
     * @param user объект User, который нужно преобразовать
     * @return преобразованный объект UserDto или null, если входной параметр был null
     */
    public static UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        UserDto userDto = new UserDto(); // Создаем новый объект UserDto
        BeanUtils.copyProperties(user, userDto); // Копируем свойства из объекта user в userDto
        return userDto;
    }

    /**
     * Метод преобразует объект UserDto в объект User.
     *
     * @param userDto объект UserDto, который нужно преобразовать
     * @return преобразованный объект User или null, если входной параметр был null
     */
    public static User fromDto(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        User user = new User(); // Создаем новый объект User
        BeanUtils.copyProperties(userDto, user); // Копируем свойства из объекта userDto в user
        return user;
    }
}