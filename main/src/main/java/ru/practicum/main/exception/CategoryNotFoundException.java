package ru.practicum.main.exception;

import java.text.MessageFormat;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(int catId) {
        super(MessageFormat.format("Category {0} not found.", catId));
    }
}
