package ru.practicum.shareit.user.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import ru.practicum.shareit.user.validation.Creation;
import ru.practicum.shareit.user.validation.Update;

@Data
@Builder
public class UserDto {
    //@NotNull(message = "UserDto ID не может быть пустым")
    private Long id;

    @NotEmpty(message = "UserDto name не должно быть пустым", groups = {Creation.class, Update.class})
    @Size(max = 50, min = 0, message = "UserDto name должно содержать не более 50 символов", groups = {Creation.class, Update.class})
    private String name;

    @Email(message = "UserDto формат email некорректен", groups = {Creation.class, Update.class})
    @NotEmpty(message = "UserDto email не может быть пустым", groups = Creation.class)
    private String email;
}



