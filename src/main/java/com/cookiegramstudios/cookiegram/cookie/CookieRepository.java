package com.cookiegramstudios.cookiegram.cookie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Cookie entity representing a product available in the system.
 *
 * @name Nguyen Anh Khoa Tran
 * @date 2026-02-28
 * @version 1.0
 */
@Repository
public interface CookieRepository extends JpaRepository<Cookie, Long>{
    // Finds all cookies by their active status
    List<Cookie> findByActive(boolean active);

    // Finds cookies containing a specific name (case-insensitive)
    List<Cookie> findByNameContainingIgnoreCase(String name);

    // Finds cookies within a specific price range
    List<Cookie> findByBasePriceBetween(double minPrice, double maxPrice);
}
