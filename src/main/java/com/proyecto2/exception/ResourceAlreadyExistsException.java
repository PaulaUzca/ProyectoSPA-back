package com.proyecto2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.IM_USED)
public class ResourceAlreadyExistsException extends Exception {
    private static final long serialVersionUID = -9055214007896797222L;

    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
