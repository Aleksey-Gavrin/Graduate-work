package ru.skypro.homework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityModelNotFoundException extends RuntimeException {

    public EntityModelNotFoundException(String message) {
        super(message);
    }
}
