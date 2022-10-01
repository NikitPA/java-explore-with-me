package ru.practicum.main.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        return new ResponseEntity<>(userService.findUsers(from, size, ids), HttpStatus.OK);
    }

    @PostMapping("/admin/users")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.OK);
    }

    @DeleteMapping("/admin/users/{userId}")
    public void deleteUser(@PathVariable(name = "userId") int userId) {
        userService.delete(userId);
    }

    @PatchMapping("/users/{userId}/subscriptions/{subscriptionId}")
    public ResponseEntity<UserDto> addSubscription(@PathVariable(name = "subscriptionId") Integer subscriptionId,
                                                   @PathVariable(name = "userId") Integer userId) {
        return new ResponseEntity<>(userService.addSubscription(userId, subscriptionId), HttpStatus.OK);
    }

    @PatchMapping("/users/{userId}/subscriptions/disable")
    public ResponseEntity<UserDto> disableSubscriptions(@PathVariable(name = "userId") Integer userId) {
        return new ResponseEntity<>(userService.disableSubscriptions(userId), HttpStatus.OK);
    }

    @PatchMapping("/users/{userId}/subscriptions/allow")
    public ResponseEntity<UserDto> allowSubscriptions(@PathVariable(name = "userId") Integer userId) {
        return new ResponseEntity<>(userService.allowSubscriptions(userId), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/subscriptions")
    public ResponseEntity<List<UserDto>> getAllSubscriptionsUser(@PathVariable(name = "userId") Integer userId) {
        return new ResponseEntity<>(userService.getAllSubscriptionsUser(userId), HttpStatus.OK);
    }

}
