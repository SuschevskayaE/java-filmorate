package ru.yandex.practicum.filmorate.exception;

public class DuplicateException extends RuntimeException {
    public DuplicateException(final String message) {
        super(message);
    }
}
