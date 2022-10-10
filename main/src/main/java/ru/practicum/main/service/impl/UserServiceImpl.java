package ru.practicum.main.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.model.User;
import ru.practicum.main.model.dto.UserDto;
import ru.practicum.main.repository.UserRepository;
import ru.practicum.main.service.UserService;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Transactional
    public UserDto createUser(UserDto userDto) {
        User user = mapper.map(userDto, User.class);
        User userSave = userRepository.save(user);
        return mapper.map(userSave, UserDto.class);
    }

    @Transactional
    @Override
    public void delete(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("User {0} not found", userId)));
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

    @Transactional
    @Override
    public void addSubscription(int userId, int subscriptionId) {
        if (userId == subscriptionId) {
            throw new IllegalArgumentException("Сan't subscribe to yourself.");
        }
        User subscription = userRepository.findById(subscriptionId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("User {0} not found", userId)));
        if (subscription.isSubscriptionAllowed()) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException(MessageFormat.format("User {0} not found", userId)));
            user.addFriend(subscription);
            userRepository.save(user);
        }
        throw new IllegalArgumentException("The user has forbidden to subscribe to himself");
    }

    @Transactional
    @Override
    public UserDto disableSubscriptions(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("User {0} not found", userId)));
        if (user.isSubscriptionAllowed()) {
            user.setSubscriptionAllowed(false);
            User saveUser = userRepository.save(user);
            return mapper.map(saveUser, UserDto.class);
        }
        throw new IllegalArgumentException("The user's subscriptions are already forbidden");
    }

    @Transactional
    @Override
    public UserDto allowSubscriptions(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("User {0} not found", userId)));
        if (!user.isSubscriptionAllowed()) {
            user.setSubscriptionAllowed(true);
            User saveUser = userRepository.save(user);
            return mapper.map(saveUser, UserDto.class);
        }
        throw new IllegalArgumentException("The user already has subscriptions allowed");
    }

    @Override
    public List<UserDto> getAllSubscriptionsUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("User {0} not found", userId)));
        Set<User> subscriptions = user.getSubscriptions();
        return subscriptions.stream()
                .map(userSub -> mapper.map(userSub, UserDto.class))
                .collect(Collectors.toList());
    }

}
