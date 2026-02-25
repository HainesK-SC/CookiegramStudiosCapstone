package com.cookiegramstudios.cookiegram.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link Customer} entity.
 * <p>
 * Provides database access methods for customer profile operations.
 * Extends JpaRepository to inherit standard CRUD operations and adds
 * custom query methods for common customer lookup scenarios.
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-02-24
 * @version 1.0
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Finds a customer profile by email address.
     * <p>
     * Email is used as the primary customer identifier for order placement
     * and communication purposes.
     * </p>
     *
     * @param email email address to search for
     * @return Optional containing the matching customer profile, or empty if not found
     */
    Optional<Customer> findByEmail(String email);

    /**
     * Checks if a customer profile exists with the given email address.
     * <p>
     * Useful for validation before creating new customer profiles.
     * </p>
     *
     * @param email email address to check
     * @return true if a profile exists with this email, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Finds all customer profiles created after a specific date.
     * <p>
     * Useful for analytics and tracking new customer acquisition.
     * </p>
     *
     * @param date the cutoff date
     * @return list of customer profiles created after the specified date
     */
    List<Customer> findByCreatedAtAfter(LocalDateTime date);

    /**
     * Finds all customer profiles who have placed orders (lastOrderDate is not null).
     * <p>
     * Useful for identifying active customers vs. registered customers who haven't ordered.
     * </p>
     *
     * @return list of customer profiles with at least one order
     */
    List<Customer> findByLastOrderDateIsNotNull();

    /**
     * Finds all customer profiles ordered by last order date in descending order.
     * <p>
     * Useful for identifying recent customers and customer engagement analytics.
     * </p>
     *
     * @return list of customer profiles sorted by most recent order first
     */
    List<Customer> findAllByOrderByLastOrderDateDesc();
}
