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
 * @date 2026-02-24
 * @version 1.0
 */
@Repository
public interface RecipientRepository extends JpaRepository<Recipient, Long> {

    /**
     * Finds all recipients by name.
     * <p>
     * Case-sensitive search. Useful for finding all orders sent to a specific person.
     * </p>
     *
     * @param name the recipient's name to search for
     * @return list of recipients with matching name
     */
    List<Recipient> findByName(String name);

    /**
     * Finds all recipients by name (case-insensitive).
     * <p>
     * Useful for flexible name-based searches when exact casing is unknown.
     * </p>
     *
     * @param name the recipient's name to search for (case-insensitive)
     * @return list of recipients with matching name
     */
    List<Recipient> findByNameIgnoreCase(String name);

    /**
     * Finds all recipients in a specific city.
     * <p>
     * Useful for delivery route optimization and regional analytics.
     * </p>
     *
     * @param city the city to search for
     * @return list of recipients in the specified city
     */
    List<Recipient> findByCity(String city);

    /**
     * Finds all recipients in a specific postal code.
     * <p>
     * Useful for delivery route optimization and targeted marketing.
     * </p>
     *
     * @param postalCode the postal code to search for
     * @return list of recipients with the specified postal code
     */
    List<Recipient> findByPostalCode(String postalCode);

    /**
     * Finds all recipients with special instructions.
     * <p>
     * Useful for identifying orders requiring extra attention during delivery.
     * </p>
     *
     * @return list of recipients who have special instructions
     */
    List<Recipient> findBySpecialInstructionsIsNotNull();

    /**
     * Finds all recipients by country.
     * <p>
     * Useful for international order management and logistics.
     * </p>
     *
     * @param country the country to search for
     * @return list of recipients in the specified country
     */
    List<Recipient> findByCountry(String country);

    /**
     * Searches for recipients by partial name match (case-insensitive).
     * <p>
     * Useful for autocomplete functionality and flexible search features.
     * </p>
     *
     * @param namePattern the name pattern to search for (use % wildcards)
     * @return list of recipients with names matching the pattern
     */
    @Query("SELECT r FROM Recipient r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :namePattern, '%'))")
    List<Recipient> searchByNameContaining(@Param("namePattern") String namePattern);
}
