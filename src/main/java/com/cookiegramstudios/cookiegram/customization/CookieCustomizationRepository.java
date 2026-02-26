package com.cookiegramstudios.cookiegram.customization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CookieCustomizationRepository extends JpaRepository<CookieCustomization, Long> {
}
