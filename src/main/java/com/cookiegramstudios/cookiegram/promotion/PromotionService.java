package com.cookiegramstudios.cookiegram.promotion;

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
	 * Find by ID (primary key) value.
	 * @param id: long - The primary key ID value of the promotion.
	 * @return promotionById: Promotion - A Promotion object with the corresponding ID.
	 */
	@Transactional
	public Optional<Promotion> getById(long id) {
		return promotionRepository.findById(id);
	}
}
