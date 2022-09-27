package ru.practicum.main.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.main.exception.*;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class ErrorHandlingControllerAdvice {

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError onMissingRequestHeaderException(MissingRequestHeaderException e) {
        return new ApiError(
                List.of(e.getMessage()),
                "Validation error.",
                "Validation error.",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError onUserNotFoundException(UserNotFoundException e) {
        return new ApiError(
                List.of(e.getMessage()),
                "User not found.",
                "For the requested operation the conditions are not met.",
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError onCategoryNotFoundException(CategoryNotFoundException e) {
        return new ApiError(
                List.of(e.getMessage()),
                "Category not found.",
                "For the requested operation the conditions are not met.",
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(CompilationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError onCompilationNotFoundException(CompilationNotFoundException e) {
        return new ApiError(
                List.of(e.getMessage()),
                "Compilation not found.",
                "For the requested operation the conditions are not met.",
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(EventNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError onEventNotFoundException(EventNotFoundException e) {
        return new ApiError(
                List.of(e.getMessage()),
                "Event not found.",
                "For the requested operation the conditions are not met.",
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(RequestNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError onRequestNotFoundException(RequestNotFoundException e) {
        return new ApiError(
                List.of(e.getMessage()),
                "Request not found.",
                "For the requested operation the conditions are not met.",
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError onIllegalArgumentException(IllegalArgumentException e) {
        return new ApiError(
                List.of(e.getMessage()),
                "BadRequest",
                "BadRequest.",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
    }

}
