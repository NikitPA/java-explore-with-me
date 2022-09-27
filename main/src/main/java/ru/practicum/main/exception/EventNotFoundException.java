package ru.practicum.main.exception;

import java.text.MessageFormat;

public class EventNotFoundException extends RuntimeException {

    public EventNotFoundException(int eventId) {
        super(MessageFormat.format("Event {0} not found.", eventId));
    }

}
