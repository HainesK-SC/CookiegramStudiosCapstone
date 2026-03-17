package com.cookiegramstudios.cookiegram.recipient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link Recipient} entity.
 * <p>
 * Provides database access methods for recipient operations.
 * Extends JpaRepository to inherit standard CRUD operations and adds
 * custom query methods for recipient lookup and management.
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-03-17
 * @version 1.2
 */
@Repository
public interface RecipientRepository extends JpaRepository<Recipient, Long> {

	 /**
     * Finds all recipients by first name (case-sensitive).
     */
    List<Recipient> findByFirstName(String firstName);

    /**
     * Finds all recipients by first name (case-insensitive).
     */
    List<Recipient> findByFirstNameIgnoreCase(String firstName);

    /**
     * Finds all recipients in a specific city.
     */
    List<Recipient> findByCity(String city);

    /**
     * Finds all recipients in a specific postal code.
     */
    List<Recipient> findByPostalCode(String postalCode);

    /**
     * Finds all recipients with special instructions.
     */
    List<Recipient> findBySpecialInstructionsIsNotNull();

    /**
     * Finds all recipients by country.
     */
    List<Recipient> findByCountry(String country);

    /**
     * Searches for recipients by partial first name match (case-insensitive).
     */
    @Query("SELECT r FROM Recipient r WHERE LOWER(r.firstName) LIKE LOWER(CONCAT('%', :namePattern, '%'))")
    List<Recipient> searchByFirstNameContaining(@Param("namePattern") String namePattern);
}
