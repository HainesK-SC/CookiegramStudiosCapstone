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
    List<Cookie> findByActive(boolean active);

    List<Cookie> findByNameContainingIgnoreCase(String name);

    List<Cookie> findByBasePriceBetween(double minPrice, double maxPrice);
}
