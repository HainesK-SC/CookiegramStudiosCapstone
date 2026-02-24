package com.cookiegramstudios.cookiegram.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link User} entity.
 * <p>
 *     Provides CRUD operations and custom queries.
 *     Extends {@link JpaRepository}
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-02-18
 * @version 1.2
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email address
     * @param email the email address to search for
     * @return user with given email address
     */
    User findByEmail(String email);

    /**
     * Finds a user by their assigned role
     * @param role user role to search for
     * @return the first user with the given role
     */
    User findByRole(UserRole role);

    /**
     * Finds all users with a specific role.
     * <p>
     * Unlike {@link #findByRole(UserRole)} which returns a single user,
     * this method returns all users matching the role.
     * </p>
     *
     * @param role the role to filter by
     * @return list of all users with the specified role
     */
    List<User> findAllByRole(UserRole role);

    /**
     * Checks if a user exists with the given email.
     * <p>
     * More efficient than findByEmail when you only need to verify existence.
     * </p>
     *
     * @param email the email to check
     * @return true if a user exists with this email, false otherwise
     */
    boolean existsByEmail(String email);

}
