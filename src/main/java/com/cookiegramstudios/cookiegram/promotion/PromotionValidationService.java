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

    public boolean validatePromotionObject(Promotion promotion) throws InvalidPromotionDataException {
        boolean isValidPromo = false;

        if (promotion == null) {
            throw new InvalidPromotionDataException(
                    "Promotion is empty. Please try again or contact an administrator if the issue persists.");
        }

        logger.debug("Validating Promotion Object ID: {}", promotion.getId());

        if (promotion.getPromoCode() == null || promotion.getPromoCode().trim().strip().isEmpty()) {
            throw new InvalidPromotionDataException("Promotion Code cannot be blank. Please check your values.");
        }

        Promotion promosByPromoCode = promotionRepository.findByPromoCode(promotion.getPromoCode());
        if (promosByPromoCode == null) {
            throw new InvalidPromotionDataException("PROMO CODE not in the system.");
        }

        if (promotion.getDescription() == null || promotion.getDescription().trim().strip().isEmpty()) {
            throw new InvalidPromotionDataException("Promotion description cannot be blank. Please check your values.");
        }

        if (promotion.getPromoType() == null || promotion.getPromoType().toString().trim().strip().isEmpty()) {
            throw new InvalidPromotionDataException(
                    "Promotion Type cannot be blank. Please check your values. Must be either PromotionTypes.FIXED or PromotionTypes.PERCENTAGE.");
        }

        if (promotion.getPromoType() != PromotionTypes.FIXED && promotion.getPromoType() != PromotionTypes.PERCENTAGE) {
            throw new InvalidPromotionDataException(
                    "Invalid Promotion Type. Please check your values. Must be either PromotionTypes.FIXED or PromotionTypes.PERCENTAGE.");
        }

        if (promotion.getPromoValue() == 0 || Double.isNaN(promotion.getPromoValue())) {
            if (Double.isNaN(promotion.getPromoValue())) {
                logger.debug("Promotion Object ID: {}. Attribute: promoValue has value of NaN.", promotion.getId());
            }
            throw new InvalidPromotionDataException(
                    "Promotion value cannot be 0. The value must be a positive number greater than one.");
        }

        if (promotion.getStartDate() == null) {
            throw new InvalidPromotionDataException("Promotion start date is empty. Please check your values.");
        }

        if (promotion.getEndDate() == null) {
            throw new InvalidPromotionDataException("Promotion end date is empty. Please check your values.");
        }

        if (promotion.getIsActive() != true || promotion.getIsActive() != false) {
            throw new InvalidPromotionDataException(
                    "Promotion's active status cannot be blank. Please check your values.");
        }

        return isValidPromo;
    }

    public void validatePromotionForUpdate(long id, Promotion promotion) {
        if (promotionRepository.existsByPromoCodeAndIdNot(promotion.getPromoCode(), id)) {
            throw new InvalidPromotionDataException("PROMO CODE already exists in the system.");
        }
    }

}
