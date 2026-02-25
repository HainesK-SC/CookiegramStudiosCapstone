package com.cookiegramstudios.cookiegram.common.exceptions;

/**
 * Exception thrown when an invalid Promotion object is being used.
 * This will check if the overall object is null and return a meaningful message.
 * This will also verify and validate member variables, as well to ensure data is valid.
 * 
 * @author Kyle Haines
 * @date 2026-02-2025
 */

public class InvalidPromotionDataException extends RuntimeException {
	public InvalidPromotionDataException(String message) {
		super(message);
	}

}
