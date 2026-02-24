package com.cookiegramstudios.cookiegram.common.exceptions;

/**
 * Exception thrown when invalid user data is provided.
 * @author Matthew Samaha
 * @date 2026-02-24
 * @version 1.0
 */
public class InvalidUserDataException extends RuntimeException {

    public InvalidUserDataException(String message) {
        super(message);
    }
}
