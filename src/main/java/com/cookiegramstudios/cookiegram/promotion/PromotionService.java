package com.cookiegramstudios.cookiegram.promotion;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The service layer for Promotion oriented business logic.
 * <p>
 * This class provides data access methods for the application
 * to use and consume. This class acts as an intermediary layer
 * between the controller(s) and the {@link PromotionRepository}. 
 * All business logic and validation for Promotion related data 
 * should be defined within this class.
 * </p>
 *
 * @author Kyle Haines
 * @date 2026-02-24
 * @version 1.0
 */

@Service
public class PromotionService {
	private static final Logger logger = LoggerFactory.getLogger(PromotionService.class);

	private final PromotionRepository promotionRepository;
	
	// Constructor used to inject instance of PromotionRepository
	public PromotionService(PromotionRepository promoRepo) {
		this.promotionRepository = promoRepo;
	}
	
	/**
	 * Get Promotion by ID (primary key) value.
	 * @param id: long - The primary key ID value of the promotion.
	 * @return promotionById: Promotion - A Promotion object with the corresponding ID.
	 */
	@Transactional
	public Optional<Promotion> getById(long id) {
		return promotionRepository.findById(id);
	}
	
	/**
	 * Get Promotion(s) by promotion type (FIXED or PERCENTAGE).
	 * @param promoType: PromotionTypes - A value from the PromotionTypes enumeration (FIXED or PERCENTAGE).
	 * @return List<Promotion> - Promotion object(s) that match the given type.
	 */
	@Transactional
	public List<Promotion> getByPromoType(PromotionTypes promoType) {
		return promotionRepository.findByPromoType(promoType);
	}
	
	/**
	 * Find Promotion(s) by promotion value.
	 * This value will be represented by a dollar figure
	 * or by a percentage figure. Percentages are shown as
	 * whole numbers (not decimals).
	 * @param promoValue: double - The decimal figure.
	 * @return promotions: List<Promotion> - Promotion object(s) that match
	 * the given value.
	 */
	@Transactional
	public List<Promotion> getByPromoValue(double promoValue) {
		return promotionRepository.findByPromoValue(promoValue);
	}
	
	/**
	 * Find Promotion(s) by their start date
	 * @param startDate: LocalDate - The date and time the promotion started.
	 * @return promotions: List<Promotion> - Promotion object(s) that started on the given date.
	 */
	@Transactional
	public List<Promotion> getByStartDate(LocalDate startDate) {
		return promotionRepository.findByStartDate(startDate);
	}
	
	/**
	 * Find promotion(s) by whether they are currently active or not.
	 * @param isActive: boolean - Whether the isActive attribute is true or false.
	 * @return promotions: List<Promotion> - Promotion object(s) that are currently active.
	 */
	@Transactional
	public List<Promotion> getByIsActive(boolean isActive) {
		return promotionRepository.findByIsActive(isActive);
	}
	
	/**
	 * TO DOs for myself:
	 * - Add Promotion object validation
	 * - Add create, update, and delete methods
	 * - Create promotion related exceptions 
	 * - Add Exception handling
	 */
}
