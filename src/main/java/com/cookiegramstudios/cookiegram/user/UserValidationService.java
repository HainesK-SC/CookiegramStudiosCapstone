package com.cookiegramstudios.cookiegram.user;


import com.cookiegramstudios.cookiegram.common.exceptions.InvalidUserDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * Centralized validator for {@link User} create/update operations.
 * <p>
 * This service encapsulates input validation rules used by {@link UserService}
 * so business orchestration and validation concerns remain separated.
 * </p>
 *
 * <p><b>Validation coverage:</b></p>
 * <ul>
 *     <li>Create: email format, password presence/length, first name, last name, role</li>
 *     <li>Update: email format, first name, last name</li>
 * </ul>
 *
 * <p>
 * Validation failures throw {@link com.cookiegramstudios.cookiegram.common.exceptions.InvalidUserDataException}
 * with user-facing messages suitable for controller/service error handling.
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-03-18
 * @version 1.0
 */
@Service
public class UserValidationService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserValidationService.class);

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

}
