package ru.practicum.main.exception;

import java.text.MessageFormat;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(int userId) {
        super(MessageFormat.format("User {0} not found", userId));
    }

}
