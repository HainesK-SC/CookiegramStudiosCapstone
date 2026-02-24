package com.cookiegramstudios.cookiegram.common.exceptions;

/**
 * Exception thrown when a user is not found.
 * @author Matthew Samaha
 * @date 2026-02-24
 * @version 1.0
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
