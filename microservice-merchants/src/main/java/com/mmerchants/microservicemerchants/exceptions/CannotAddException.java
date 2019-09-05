package com.mmerchants.microservicemerchants.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CannotAddException extends RuntimeException {
    /**
     * <p>Exception if a query returns "can't add new merchant"</p>
     * @param s
     */
    public CannotAddException(String s) {
        super(s);
    }
}
