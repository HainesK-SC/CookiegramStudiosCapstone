package com.cookiegramstudios.cookiegram.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link User} entity.
 * <p>
 *     Provides CRUD operations and custom queries.
 *     Extends {@link JpaRepository}
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-02-18
 * @version 1.1
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

}
