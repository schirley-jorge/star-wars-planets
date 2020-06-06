package com.b2w.starwarsplanets.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PlanetAlreadyExistException extends RuntimeException {
    public PlanetAlreadyExistException(String message) {
        super(message);
    }
}
