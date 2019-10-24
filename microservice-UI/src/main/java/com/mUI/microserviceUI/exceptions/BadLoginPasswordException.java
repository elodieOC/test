package com.mUI.microserviceUI.exceptions;

public class BadLoginPasswordException extends RuntimeException {
    public BadLoginPasswordException(String message) {
        super(message);
    }
}
