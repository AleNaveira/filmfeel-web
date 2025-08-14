package com.FilmFeel.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmailAlreadyUsedException extends ResponseStatusException {
    public EmailAlreadyUsedException(String email) {
        super(HttpStatus.CONFLICT, "Ese email ya está registrado: " + email);
    }
}
