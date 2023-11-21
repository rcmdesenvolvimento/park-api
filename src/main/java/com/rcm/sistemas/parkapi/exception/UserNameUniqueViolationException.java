package com.rcm.sistemas.parkapi.exception;

public class UserNameUniqueViolationException extends RuntimeException {
    public UserNameUniqueViolationException(String messge) {
        super(messge);
    }
}
