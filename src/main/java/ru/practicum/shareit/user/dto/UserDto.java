package ru.practicum.shareit.user.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import ru.practicum.shareit.validation.Creation;
import ru.practicum.shareit.validation.Update;

@Data
@Builder
public class UserDto {

    private Long id;
    @NotEmpty(message = "UserDto name не должно быть пустым", groups = {Creation.class})
    @Size(max = 50, message = "UserDto name должно содержать не более 50 символов",
            groups = {Creation.class, Update.class})
    private String name;

    @Email(message = "UserDto формат email некорректен", groups = {Creation.class})
    @NotEmpty(message = "UserDto email не может быть пустым", groups = {Creation.class})
    private String email;
}



