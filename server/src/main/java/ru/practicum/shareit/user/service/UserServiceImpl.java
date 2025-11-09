package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.CustomDataAccessException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
/*import ru.practicum.shareit.user.dto.UserDtoCreate;
import ru.practicum.shareit.user.dto.UserDtoUpdate;*/
import ru.practicum.shareit.user.dao.*;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.email())) {
            log.warn("Пользователь: {} не может быть создан. Email уже существует у другого пользователя", userDto);
            throw new ConflictException("Email: " + userDto.email() + " уже существует у другого пользователя");
        }
        try {
            User user = userMapper.fromUser(userDto);
            User savedUser = userRepository.save(user);
            log.info("Пользователь: {} успешно создан.", savedUser);
            return userMapper.toUserDto(savedUser);
        } catch (DataAccessException e) {
            log.error("Произошла ошибка при работе с таблицей users. {}", e.getMessage(), e);
            throw new CustomDataAccessException("Запрос не обработан. На сервере произошла ошибка работы базы данных.");
        } catch (Exception e) {
            log.error("Неизвестная ошибка: {}", e.getMessage(), e);
            throw new RuntimeException("Запрос не обработан. На сервере произошла неизвестная ошибка.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id {" + id + "} не существует"));

        log.debug("Пользователь: {} успешно получен.", user);
        return userMapper.toUserDto(user);
    }
/*
    @Override
    public UserDto updateUser(Long userId, UserDtoUpdate updateUserDto) {
        log.debug("Начало обновления пользователя с ID: {}", userId);
        User fetchedUser = userMapper.fromUser(getUserById(userId));

        try {
            userMapper.userDtoUpdate(updateUserDto, fetchedUser);
            userRepository.save(fetchedUser);
            log.debug("Пользователь с ID: {} успешно обновлен.", userId);
            return userMapper.toUserDto(fetchedUser);
        } catch (DataIntegrityViolationException e) {
            log.error("Ошибка при обновлении пользователя: {}", e.getMessage());
            throw new ConflictException("Email уже используется другим пользователем.");
        }
    }

    @Override
    public void deleteUser(Long id) {
        log.debug("Удаление пользователя с id: {} ", id);
        User userToDelete = userMapper.fromUser(getUserById(id));
        userRepository.deleteById(userToDelete.getId());
        log.debug("Пользователь с id: {} успешно удален.", id);
    }*/
}
