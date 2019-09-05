package com.musers.microserviceusers.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    /**
     * <p>Exception if a query returns "not found"</p>
     * @param s
     */
    public NotFoundException(String s) {
        super(s);
    }
}
