package ru.practicum.main.service;

import ru.practicum.main.model.User;
import ru.practicum.main.model.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);

    void delete(int userId);

    List<UserDto> findUsers(int from, int size, Integer[] ids);

    List<User> findUsersForIds(Integer[] ids);
}
