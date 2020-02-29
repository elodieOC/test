package com.mmerchants.microservicemerchants.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadLoginPasswordException extends RuntimeException {
    /**
     * <p>Exception if a query returns "can't log user"</p>
     * @param message
     */
    public BadLoginPasswordException(String message) {
        super(message);
    }
}
