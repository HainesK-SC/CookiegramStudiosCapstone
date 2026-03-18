package com.cookiegramstudios.cookiegram.promotion;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link Promotion} entity.
 * <p>
 * Provides CRUD operations and custom queries. Extends {@link JpaRepository}
 * </p>
 *
 * @author Kyle Haines
 * @date 2026-02-22
 * @version 1.0
 */

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

	Promotion findByPromoCode(String promoCode);

	List<Promotion> findByPromoType(PromotionTypes promoType);

	List<Promotion> findByPromoValue(double promoValue);

	List<Promotion> findByStartDate(LocalDate startDate);

	List<Promotion> findByEndDate(LocalDate endDate);

	List<Promotion> findByIsActive(boolean isActive);

	boolean existsByPromoCodeAndIdNot(String promoCode, long id);
}
