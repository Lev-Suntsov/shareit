package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Exception.DublicateException;
import ru.practicum.shareit.Exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<UserDto> getAllUsers() {
        return repository.findAll().stream().map(UserMapper::mapToUserDto).toList();
    }

    @Override
    @Transactional
    public UserDto saveUser(UserDto user) {
        List<User> users = repository.findAll();

        for(User u: users){
            if (u.getEmail().equals(user.getEmail())) {
                throw new DublicateException("данный email уже зарегистрирован");
            }
        }
        return UserMapper.mapToUserDto(repository.save(UserMapper.mapToUser(user)));
    }

    @Override
    @Transactional
    public UserDto updateUser(Long userId, UserDto userDto) {
        User existingUser = repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        if (userDto.getName() != null) {
            existingUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            existingUser.setEmail(userDto.getEmail());
        }

        User updatedUser = repository.save(existingUser);
        return UserMapper.mapToUserDto(updatedUser);
    }

    @Override
    public UserDto findUserById(Long userId) {
        User user = repository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public void deleteUser(Long userId) {
        repository.delete(repository.findUserById(userId));
    }

    @Override
    public List<UserDto> findAllById(List<Long> ids) {
        return repository.findAllById(ids).stream().map(UserMapper::mapToUserDto).toList();
    }
}
