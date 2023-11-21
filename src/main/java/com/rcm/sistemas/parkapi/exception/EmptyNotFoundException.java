package com.rcm.sistemas.parkapi.exception;

public class EmptyNotFoundException extends RuntimeException {
    public EmptyNotFoundException(String messages) {
        super(messages);
    }
}
