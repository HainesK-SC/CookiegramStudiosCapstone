package com.cookiegramstudios.cookiegram.promotion;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cookiegramstudios.cookiegram.common.exceptions.InvalidPromotionDataException;

/**
 * The service layer for Promotion oriented business logic.
 * <p>
 * This class provides data access methods for the application to use and
 * consume. This class acts as an intermediary layer between the controller(s)
 * and the {@link PromotionRepository}. All business logic and validation for
 * Promotion related data should be defined within this class.
 * </p>
 *
 * @author Kyle Haines
 * @author Matthew Samaha
 * @date 2026-03-18
 * @version 2.0
 */

@Service
public class PromotionService {
	private static final Logger logger = LoggerFactory.getLogger(PromotionService.class);

	private final PromotionRepository promotionRepository;
	private final PromotionValidationService promotionValidationService;

	public PromotionService(PromotionRepository promotionRepository,
			PromotionValidationService promotionValidationService) {
		this.promotionRepository = promotionRepository;
		this.promotionValidationService = promotionValidationService;
	}

	@Transactional(readOnly = true)
	public List<Promotion> getAllPromotions() {
		logger.info("Retrieving all promotions...");
		return promotionRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Promotion getById(long id) {
		logger.info("Retrieving Promotion with ID: {}", id);
		return requirePromotionById(id);
	}

	@Transactional(readOnly = true)
	public Promotion getByPromoCode(String promoCode) {
		return promotionRepository.findByPromoCode(promoCode);
	}

	@Transactional(readOnly = true)
	public List<Promotion> getByPromoType(PromotionTypes promoType) {
		return promotionRepository.findByPromoType(promoType);
	}

	@Transactional(readOnly = true)
	public List<Promotion> getByPromoValue(double promoValue) {
		return promotionRepository.findByPromoValue(promoValue);
	}

	@Transactional(readOnly = true)
	public List<Promotion> getByStartDate(LocalDate startDate) {
		return promotionRepository.findByStartDate(startDate);
	}

	@Transactional(readOnly = true)
	public List<Promotion> getByIsActive(boolean isActive) {
		return promotionRepository.findByIsActive(isActive);
	}

	public Promotion createPromotion(Promotion promotion) {
		promotionValidationService.validateForCreate(promotion);

		Promotion savedPromotion = promotionRepository.save(promotion);
		logger.info("Promotion added. ID: {} PROMO CODE: {}", savedPromotion.getId(), savedPromotion.getPromoCode());
		return savedPromotion;
	}

	public Promotion updatePromotion(long id, Promotion incoming) {
		Promotion existing = requirePromotionById(id);

		promotionValidationService.validateForUpdate(id, incoming);

		existing.setPromoCode(incoming.getPromoCode());
		existing.setDescription(incoming.getDescription());
		existing.setPromoType(incoming.getPromoType());
		existing.setPromoValue(incoming.getPromoValue());
		existing.setStartDate(incoming.getStartDate());
		existing.setEndDate(incoming.getEndDate());
		existing.setIsActive(incoming.getIsActive());

		return promotionRepository.save(existing);
	}

	public void deletePromotion(long id) throws InvalidPromotionDataException {
		requirePromotionById(id);
		promotionRepository.deleteById(id);
	}

	private Promotion requirePromotionById(long id) {
		return promotionRepository.findById(id)
				.orElseThrow(() -> new InvalidPromotionDataException("Promotion not found."));
	}
}