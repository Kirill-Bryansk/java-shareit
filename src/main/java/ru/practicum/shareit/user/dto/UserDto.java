package ru.practicum.shareit.user.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    //@NotNull(message = "UserDto ID не может быть пустым")
    private Long id;

    @NotEmpty(message = "UserDto name не должно быть пустым")
    @Size(max = 50, min = 0, message = "UserDto name должно содержать не более 50 символов")
    private String name;

    @Email(message = "UserDto формат email некорректен")
    @NotEmpty(message = "UserDto email не может быть пустым")
    private String email;
}


