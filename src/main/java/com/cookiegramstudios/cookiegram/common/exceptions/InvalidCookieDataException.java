package com.cookiegramstudios.cookiegram.common.exceptions;

/**
 * Custom runtime exception thrown when Cookie data validation fails.
 *
 * @name Nguyen Anh Khoa Tran
 * @date 2026-02-28
 * @version 1.0
 */
public class InvalidCookieDataException extends RuntimeException {

    public InvalidCookieDataException(String message) {
        super(message);
    }

    public InvalidCookieDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
