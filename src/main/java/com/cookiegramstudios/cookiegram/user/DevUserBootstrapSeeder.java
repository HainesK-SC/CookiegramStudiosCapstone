package com.cookiegramstudios.cookiegram.user;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Seeds optional development login users from environment variables.
 * <p>
 * This component creates initial admin and employee users during application startup
 * when explicitly enabled via {@code app.bootstrap.users.enabled}. Credentials are
 * sourced from environment variables to avoid storing plaintext passwords in
 * source-controlled SQL files.
 * </p>
 * <p>
 * The seeder runs only when enabled and skips users that already exist in the database.
 * Designed for development and testing environments only.
 * </p>
 * <p>
 * <b>Required Environment Variables (when enabled):</b>
 * <ul>
 *   <li>{@code app.bootstrap.users.admin.email}</li>
 *   <li>{@code app.bootstrap.users.admin.password}</li>
 *   <li>{@code app.bootstrap.users.employee.email}</li>
 *   <li>{@code app.bootstrap.users.employee.password}</li>
 * </ul>
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-02-27
 * @version 1.0
 */
@Profile("dev")
@Component
public class DevUserBootstrapSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DevUserBootstrapSeeder.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Environment environment;

    @Value("${app.bootstrap.users.enabled:false}")
    private boolean bootstrapEnabled;

    @Value("${app.bootstrap.users.admin.email:}")
    private String adminEmail;

    @Value("${app.bootstrap.users.admin.password:}")
    private String adminPassword;

    @Value("${app.bootstrap.users.employee.email:}")
    private String employeeEmail;

    @Value("${app.bootstrap.users.employee.password:}")
    private String employeePassword;

    public DevUserBootstrapSeeder(UserRepository userRepository,
                                  PasswordEncoder passwordEncoder,
                                  Environment environment) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.environment = environment;
    }

    @Override
    public void run(String... args) {
        if (!bootstrapEnabled) {
            logger.info("Dev user bootstrap is disabled. Active profiles: {}", String.join(",", environment.getActiveProfiles()));
            return;
        }

        logger.info("Dev user bootstrap enabled. Admin email: {}, Employee email: {}", adminEmail, employeeEmail);

        if (isBlank(adminEmail) || isBlank(adminPassword) || isBlank(employeeEmail) || isBlank(employeePassword)) {
            logger.warn("User bootstrap enabled but required env vars are missing. Skipping bootstrap.");
            return;
        }

        createUserIfMissing(adminEmail, adminPassword, UserRole.ADMIN, "Dev", "Admin");
        createUserIfMissing(employeeEmail, employeePassword, UserRole.EMPLOYEE, "Dev", "Employee");
    }

    private void createUserIfMissing(String email, String rawPassword, UserRole role, String firstName, String lastName) {
        if (userRepository.existsByEmail(email)) {
            logger.info("Bootstrap skipped for existing {} user: {}", role, email);
            return;
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
        logger.info("Bootstrapped {} user: {}", role, email);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}