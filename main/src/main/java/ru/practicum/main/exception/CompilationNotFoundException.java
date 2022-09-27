package ru.practicum.main.exception;

import java.text.MessageFormat;

public class CompilationNotFoundException extends RuntimeException {

    public CompilationNotFoundException(int compId) {
        super(MessageFormat.format("Compilation {0} not found.", compId));
    }

}
