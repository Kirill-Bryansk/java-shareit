package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserDto createUser(UserDto userDto) {
        User user = userMapper.fromDto(userDto);
        User savedUser = userRepository.save(user);
        log.info("Пользователь: {} успешно создан.", savedUser.toString());
        return userMapper.toDto(savedUser);
    }

    @Override
    public User getUserById(Long id) {
        return null;
    }

    @Override
    public User updateUser(UserDto userDto) {
        User user = userMapper.fromDto(userDto);
        User updateUser = userRepository.updateUser(user);
        log.info("Пользователь успешно обновлен: {}", updateUser.getId());
        return updateUser;
    }

    @Override
    public void deleteUser(Long id) {

    }
}
