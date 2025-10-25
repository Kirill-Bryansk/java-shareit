package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        User user = userMapper.fromDto(userDto);
        User savedUser = userRepository.save(user);
        log.debug("Пользователь: {} успешно создан.", savedUser);
        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional
    public UserDto getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("Пользователь с id {" + id + "} не существует");
        } else {
            log.debug("Пользователь: {} успешно получен.", optionalUser);
            return userMapper.toDto(optionalUser.get());
        }
    }

    @Override
    @Transactional
    public UserDto updateUser(Long userId, UserDto userDto) {
        log.debug("Начало обновления пользователя с ID: {}", userId);
        User updateUser = userMapper.fromDto(getUserById(userId));

        if (userRepository.existsByEmail(userDto.getEmail())) {
            log.error("Email {} уже используется другим пользователем.", userDto.getEmail());
            throw new IllegalArgumentException("Email переданный для обновления уже используется другим пользователем.");
        }

        userMapper.updateFields(updateUser, userDto);
        log.debug("Пользователь с ID: {} успешно обновлен.", userId);
        return userMapper.toDto(updateUser);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        log.debug("Пользователь id: {} успешно удален: ", id);
    }
}
