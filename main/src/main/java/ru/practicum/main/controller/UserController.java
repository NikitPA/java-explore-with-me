package ru.practicum.main.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.model.dto.UserDto;
import ru.practicum.main.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/admin/users")
    public ResponseEntity<List<UserDto>> findUsers(@RequestParam(name = "from", defaultValue = "0") int from,
                                                   @RequestParam(name = "size", defaultValue = "10") int size,
                                                   @RequestParam(name = "ids", required = false) Integer[] ids) {
        return ResponseEntity.ok(userService.findUsers(from, size, ids));
    }

    @PostMapping("/admin/users")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.createUser(userDto));
    }

    @DeleteMapping("/admin/users/{userId}")
    public void deleteUser(@PathVariable(name = "userId") int userId) {
        userService.delete(userId);
    }

    @PatchMapping("/users/{userId}/subscriptions/{subscriptionId}")
    public void addSubscription(@PathVariable(name = "subscriptionId") Integer subscriptionId,
                                @PathVariable(name = "userId") Integer userId) {
        userService.addSubscription(userId, subscriptionId);
    }

    @PatchMapping("/users/{userId}/subscriptions/disable")
    public ResponseEntity<UserDto> disableSubscriptions(@PathVariable(name = "userId") Integer userId) {
        return ResponseEntity.ok(userService.disableSubscriptions(userId));
    }

    @PatchMapping("/users/{userId}/subscriptions/allow")
    public ResponseEntity<UserDto> allowSubscriptions(@PathVariable(name = "userId") Integer userId) {
        return ResponseEntity.ok(userService.allowSubscriptions(userId));
    }

    @GetMapping("/users/{userId}/subscriptions")
    public ResponseEntity<List<UserDto>> getAllSubscriptionsUser(@PathVariable(name = "userId") Integer userId) {
        return ResponseEntity.ok(userService.getAllSubscriptionsUser(userId));
    }

}
