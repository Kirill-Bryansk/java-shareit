package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.CustomDataAccessException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Optional;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private final UserDto userDto = new UserDto(1L, "Антон", "anton@mail.ru");

    @BeforeEach
    void initMocks() {
        when(userMapper.fromUser(any(UserDto.class)))
                .thenReturn(new User(1L, "Антон", "anton@mail.ru"));
    }

    @Test
    @DisplayName("Проверка создания нового пользователя")
    void createUserTest() {
        when(userRepository.save(any(User.class)))
                .thenReturn(new User(userDto.id(), userDto.name(), userDto.email()));
        when(userMapper.toUserDto(any(User.class))).thenReturn(userDto);
        UserDto userCreate = userService.createUser(userDto);
        assertThat(userCreate, equalTo(userDto));
    }

    @Test
    @DisplayName("Проверка выбрасывания ConflictException при создании пользователя с существующим email")
    void createUser_EmailAlreadyExists_ConflictException() {
        when(userRepository.existsByEmail(userMapper.fromUser(userDto).getEmail())).thenReturn(true);
        assertThrows(ConflictException.class, () -> userService.createUser(userDto));
    }

    @Test
    @DisplayName("Проверка обработки неизвестного исключения при создании пользователя")
    void createUser_UnknownExceptionTest() {
        doThrow(new RuntimeException("Неизвестная ошибка"))
                .when(userRepository).save(any(User.class));
        try {
            userService.createUser(userDto);
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), equalTo("Запрос не обработан." +
                                               " На сервере произошла неизвестная ошибка."));
            assertThat(e, instanceOf(RuntimeException.class));
        }
    }

    @Test
    @DisplayName("Проверка обработки DataAccessException при работе с таблицей users")
    void handleDataAccessExceptionTest() {
        doThrow(new CustomDataAccessException("Произошла ошибка при работе с таблицей users."))
                .when(userRepository).save(any(User.class));
        try {
            userService.createUser(userDto);
        } catch (DataAccessException e) {
            assertThat(e.getMessage(), equalTo("Запрос не обработан." +
                                               " На сервере произошла ошибка работы базы данных."));
            assertThat(e, instanceOf(CustomDataAccessException.class));
        }
    }
}



