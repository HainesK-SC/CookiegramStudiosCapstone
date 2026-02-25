package com.cookiegramstudios.cookiegram.common.exceptions;

public class InvalidCookieDataException extends RuntimeException {

    public InvalidCookieDataException(String message) {
        super(message);
    }

    public InvalidCookieDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
