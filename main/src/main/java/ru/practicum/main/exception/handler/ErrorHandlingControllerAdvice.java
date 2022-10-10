package ru.practicum.main.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.main.exception.NotFoundException;

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

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError onNotFoundException(NotFoundException e) {
        return new ApiError(
                List.of(e.getMessage()),
                "Not found.",
                "Not found.",
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

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError onException(Throwable e) {
        return new ApiError(
                List.of(e.getMessage()),
                "INTERNAL_SERVER_ERROR",
                "INTERNAL_SERVER_ERROR.",
                HttpStatus.INTERNAL_SERVER_ERROR,
                LocalDateTime.now()
        );
    }

}
