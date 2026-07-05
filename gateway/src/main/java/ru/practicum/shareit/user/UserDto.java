package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank(message = "email не должен быть пустым")
    @Email(message = "Некорректный email")
    private String email;
    @NotBlank(message = "имя не должно быть пустым")
    private String name;
}
