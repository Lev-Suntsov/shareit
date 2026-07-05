package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto saveUser(UserDto user);

    UserDto updateUser(Long userId, UserDto user);

    UserDto findUserById(Long userId);

    void deleteUser(Long userId);

    List<UserDto> findAllById(List<Long> ids);
}
