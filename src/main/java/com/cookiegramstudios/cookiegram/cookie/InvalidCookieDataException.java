package com.cookiegramstudios.cookiegram.cookie;

public class InvalidCookieDataException extends RuntimeException {

    public InvalidCookieDataException(String message) {
        super(message);
    }

    public InvalidCookieDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
