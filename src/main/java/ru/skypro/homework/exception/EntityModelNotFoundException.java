package ru.skypro.homework.exception;

public class EntityModelNotFoundException extends RuntimeException {
    public EntityModelNotFoundException() {
    }

    public EntityModelNotFoundException(String message) {
        super(message);
    }
}
