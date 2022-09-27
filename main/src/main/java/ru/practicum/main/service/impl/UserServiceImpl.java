package ru.practicum.main.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main.exception.UserNotFoundException;
import ru.practicum.main.model.User;
import ru.practicum.main.model.dto.UserDto;
import ru.practicum.main.repository.UserRepository;
import ru.practicum.main.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public UserDto createUser(UserDto userDto) {
        User user = mapper.map(userDto, User.class);
        User userSave = userRepository.save(user);
        return mapper.map(userSave, UserDto.class);
    }

    @Override
    public void delete(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        userRepository.delete(user);
    }

    @Override
    public List<UserDto> findUsers(int from, int size, Integer[] ids) {
        int page = from / size;
        PageRequest pageRequest = PageRequest.of(page, size);
        if (ids != null) {
            List<User> usersByIds = findUsersForIds(ids);
            return usersByIds.stream()
                    .map(user -> mapper.map(user, UserDto.class))
                    .collect(Collectors.toList());
        }
        Page<User> users = userRepository.findAll(pageRequest);
        return users.stream()
                .map(user -> mapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findUsersForIds(Integer[] ids) {
        return userRepository.findAllById(Arrays.asList(ids));
    }
}
