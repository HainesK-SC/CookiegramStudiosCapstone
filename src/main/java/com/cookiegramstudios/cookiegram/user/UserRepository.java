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

    User findByEmail(String email);


    User findByRole(UserRole role);

 
    List<User> findAllByRole(UserRole role);


    boolean existsByEmail(String email);

}
