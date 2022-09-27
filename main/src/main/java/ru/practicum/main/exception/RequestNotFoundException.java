package ru.practicum.main.exception;

import java.text.MessageFormat;

public class RequestNotFoundException extends RuntimeException {

    public RequestNotFoundException(int reqId) {
        super(MessageFormat.format("Request {0} not found.", reqId));
    }

}
