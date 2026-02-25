package com.cookiegramstudios.cookiegram.cookie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

public interface CookieRepository {
    // Finds all cookies by their active status
    List<Cookie> findByActive(boolean active);

    // Finds cookies containing a specific name (case-insensitive)
    List<Cookie> findByNameContainingIgnoreCase(String name);

    // Finds cookies within a specific price range
    List<Cookie> findByBasePriceBetween(double minPrice, double maxPrice);
}
