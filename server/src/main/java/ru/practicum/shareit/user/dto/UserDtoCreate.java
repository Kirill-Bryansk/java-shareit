package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


/*public record UserDtoCreate(
        @NotBlank
        @Size(min = 3,max = 50, message = "Ошибка валидации User. Имя должно содержать от 3 до 50 символов")
        String name,
        @NotBlank(message = "Ошибка валидации User. Передано пустой email")
        @Email(message = "Ошибка валидации User. Передан не корректный формат email")
        String email
) {
}*/
