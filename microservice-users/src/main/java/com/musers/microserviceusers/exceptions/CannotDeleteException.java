package com.musers.microserviceusers.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CannotDeleteException extends RuntimeException {
    /**
     * <p>Exception if a query returns "can't delete user"</p>
     * @param s
     */
    public CannotDeleteException(String s) {
        super(s);
    }
}
