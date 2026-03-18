package com.cookiegramstudios.cookiegram.promotion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cookiegramstudios.cookiegram.common.exceptions.InvalidPromotionDataException;

/**
 * Centralized validation logic for Promotion create/update flows
 * 
 * @author Matthew Samaha
 * @date 2026-03-18
 */
@Service
public class PromotionValidationService {

	private static final Logger logger = LoggerFactory.getLogger(PromotionValidationService.class);

	private final PromotionRepository promotionRepository;

	public PromotionValidationService(PromotionRepository promotionRepository) {
		this.promotionRepository = promotionRepository;
	}

	public void validateForCreate(Promotion promotion) {
		validateCommonFields(promotion);

		if (promotionRepository.findByPromoCode(promotion.getPromoCode()) != null) {
			throw new InvalidPromotionDataException("PROMO CODE already exists in the system.");
		}
	}

	public void validateForUpdate(long id, Promotion promotion) {
		validateCommonFields(promotion);

		if (promotionRepository.existsByPromoCodeAndIdNot(promotion.getPromoCode(), id)) {
			throw new InvalidPromotionDataException("PROMO CODE already exists in the system.");
		}
	}

	private void validateCommonFields(Promotion promotion) {
		if (promotion == null) {
			throw new InvalidPromotionDataException(
					"Promotion is empty. Please try again or contact an administrator if the issue persists.");
		}

		logger.debug("Validating Promotion Object ID: {}", promotion.getId());

		if (promotion.getPromoCode() == null || promotion.getPromoCode().trim().isEmpty()) {
			throw new InvalidPromotionDataException("Promotion Code cannot be blank. Please check your values.");
		}

		if (promotion.getDescription() == null || promotion.getDescription().trim().isEmpty()) {
			throw new InvalidPromotionDataException("Promotion description cannot be blank. Please check your values.");
		}

		if (promotion.getPromoType() == null) {
			throw new InvalidPromotionDataException(
					"Promotion Type cannot be blank. Must be either FIXED or PERCENTAGE.");
		}

		if (promotion.getPromoValue() <= 0 || Double.isNaN(promotion.getPromoValue())) {
			throw new InvalidPromotionDataException("Promotion value must be a positive number greater than zero.");
		}

		if (promotion.getStartDate() == null) {
			throw new InvalidPromotionDataException("Promotion start date is empty. Please check your values.");
		}

		if (promotion.getEndDate() == null) {
			throw new InvalidPromotionDataException("Promotion end date is empty. Please check your values.");
		}

		if (promotion.getEndDate().isBefore(promotion.getStartDate())) {
			throw new InvalidPromotionDataException("Promotion end date cannot be before start date.");
		}

	}

}
