package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
/*import ru.practicum.shareit.user.dto.UserDtoCreate;
import ru.practicum.shareit.user.dto.UserDtoUpdate;*/

public interface UserService {
    UserDto createUser(UserDto userDtoCreate);

    UserDto getUserById(Long id);

    /*UserDto updateUser(Long id, UserDtoUpdate updateUserDto);

    void deleteUser(Long id);*/
}

