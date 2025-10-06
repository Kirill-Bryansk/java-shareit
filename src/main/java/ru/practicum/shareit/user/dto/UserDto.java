package ru.practicum.shareit.user.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    @NotNull(message = "ID не может быть пустым")
    private Long id;

    @NotEmpty(message = "Имя не может быть пустым")
    @Size(max = 50, min = 0, message = "Имя должно содержать не более 50 символов")
    private String name;

    @Email(message = "Формат email некорректен")
    @NotEmpty(message = "Email не может быть пустым")
    private String email;
}


