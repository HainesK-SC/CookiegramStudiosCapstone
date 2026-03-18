package com.cookiegramstudios.cookiegram.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link Customer} entity.
 * <p>
 * Provides database access methods for customer profile operations. Extends
 * JpaRepository to inherit standard CRUD operations and adds custom query
 * methods for common customer lookup scenarios.
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-02-24
 * @version 1.0
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Optional<Customer> findByEmail(String email);

	boolean existsByEmail(String email);

	List<Customer> findByCreatedAtAfter(LocalDateTime date);

	List<Customer> findByLastOrderDateIsNotNull();

	List<Customer> findAllByOrderByLastOrderDateDesc();
}
