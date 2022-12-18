package ru.yandex.practicum.filmorate.exceptions;

public class ValidException extends RuntimeException {

    private String massage;

    public ValidException(String message) {
        super(message);
    }

    public String getMassage() {
        return massage;
    }

}
