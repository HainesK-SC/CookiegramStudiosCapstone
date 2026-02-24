package com.cookiegramstudios.cookiegram.common.exceptions;

/**
 * Exception thrown when a user already exists.
 * @author Matthew Samaha
 * @date 2026-02-24
 * @version 1.0
 */
public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
