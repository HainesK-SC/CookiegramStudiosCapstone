package com.cookiegramstudios.cookiegram.promotion;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link Promotion} entity.
 * <p>
 *     Provides CRUD operations and custom queries.
 *     Extends {@link JpaRepository}
 * </p>
 *
 * @author Kyle Haines
 * @date 2026-02-22
 * @version 1.0
 */

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
	/**
     * Find a Promotion by promoCode
     * @param the unique promotion code
     * @return Promotion object of the given promotion code
     */
	Promotion findByPromoCode(String promoCode);
	
	/**
     * Find a Promotion by promoType ($ or %, dollar, percentage)
     * @param promoType: String - DOLLAR or PERCENT - ENUMURATION
     * @return promotions: List - Promotion object(s) of the given promotion type
     */
	List<Promotion> findByPromoType(PromotionTypes promoType);
	
	/**
     * Find a Promotion by promoValue (The dollar figure of the discount)
     * @param promoValue: double - Dollar value of the discount offered by the promotion
     * @return promotions: List - Promotion object(s) of the given promotion type
     */
	List<Promotion> findByPromoValue(double promoValue);
	
	/**
     * Find a Promotion by the start date
     * @param startDate: LocalDateTime - The date and time the promotion started.
     * @return promotions: List - Promotion object(s) of the given promotion type
     */
	List<Promotion> findByStartDate(LocalDateTime startDate);
	
	/**
     * Find a Promotion by the end date
     * @param endDate: LocalDateTime - The date and time the promotion started.
     * @return promotions: List - Promotion object(s) of the given promotion type
     */
	List<Promotion> findByEndDate(LocalDateTime endDate);
	
	/**
     * Find all active promotions
     * @param isActive: boolean - True (ACTIVE) / False (INACTIVE)
     * @return promotions: List - Promotion object(s) of the given promotion type
     */
	List<Promotion> findByIsActive(boolean isActive);
}
