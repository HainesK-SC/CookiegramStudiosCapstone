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
	 * Get all Promotion(s) objects.
	 */
	@Transactional
	public List<Promotion> getAllPromotions() {
		logger.info("Retrieving all promotions...");
		List<Promotion> promotions = promotionRepository.findAll();
		logger.info("All promotions retrieved!");
		return promotions;
	}
	
	/**
	 * Get Promotion by ID (primary key) value.
	 * @param id: long - The primary key ID value of the promotion.
	 * @return promotionById: Promotion - A Promotion object with the corresponding ID.
	 */
	@Transactional
	public Promotion getById(long id) {
		logger.info("Retrieving Promotion with ID:, {}", id);
		
		Promotion promoOptional = promotionRepository.findById(id)
				.orElseThrow(() -> new InvalidPromotionDataException("Promotion not found."));
		
		// returns the contents of the promoOptional - a Promotion object
		return promoOptional;
				
	}
	
	/**
	 * Get Promotion by promotional code. Codes are unique, so only one Promotion
	 * object should ever be returned.
	 * @param promoCode: String - The unique promotional code of the Promotion (e.g. SPRING25)
	 * @return promotion: Promotion - A Promotion object with the corresponding promoCode value.
	 */
	@Transactional
	public Promotion getByPromoCode(String promoCode) {
		Promotion promotion = promotionRepository.findByPromoCode(promoCode);
		return promotion;
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
	
	// VALIDATIONS
	
	/**
	 * Validates that any given user object is valid.
	 * This checks if the overall object is null, as well as validating and verifying member variable
	 * variables.
	 * 
	 * @param promotion: Promotion - The Promotion object to verify.
	 * @return isValid: boolean - A truthy value whether the Promotion object is valid or not.
	 * @throws InvalidPromotionDataException: RuntimeException - An exception that indicates to the user the
	 * error or discrepancy related to the Promotion object they are working with.
	 */
	private boolean validatePromotionObject(Promotion promotion) throws InvalidPromotionDataException{
		boolean isValidPromo = false;
		
		// Checking overall Promotion object
		if(promotion == null) {
			throw new InvalidPromotionDataException("Promotion is empty. Please try again or contact an administrator if the issue persists.");
		}
		
		logger.debug("Invalid Promotion Object, Object ID: ", promotion.id);
		
		// Validate promoCode
		if(promotion.promoCode == null || promotion.promoCode.trim().strip().isEmpty()) {
			throw new InvalidPromotionDataException("Promotion Code cannot be blank. Please check your values.");
		}
		
		// Validate that the promoCode doesn't already exist in the system
		Promotion promosByPromoCode = getByPromoCode(promotion.promoCode);
		if(promosByPromoCode == null) {
			throw new InvalidPromotionDataException("PROMO CODE not in the system.");
		}
		
		// Validate description
		if(promotion.description == null || promotion.description.trim().strip().isEmpty()) {
			throw new InvalidPromotionDataException("Promotion description cannot be blank. Please check your values.");
		}
		
		// Validate promoType is not blank
		if( promotion.promoType == null || promotion.promoType.toString().trim().strip().isEmpty()) {
			throw new InvalidPromotionDataException("Promotion Type cannot be blank. Please check your values. Must be either PromotionTypes.FIXED or PromotionTypes.PERCENTAGE.");
		}
		
		// Validate promoType is valid data type and value
		if(promotion.promoType != PromotionTypes.FIXED
				&& promotion.promoType != PromotionTypes.PERCENTAGE) {
			throw new InvalidPromotionDataException("Invalid Promotion Type. Please check your values. Must be either PromotionTypes.FIXED or PromotionTypes.PERCENTAGE.");
		}
		
		// Validate promoValue
		if(promotion.promoValue == 0 || Double.isNaN(promotion.promoValue)) {
			// This will add a debug level log entry if the value is NaN.
			// This really should never happen, but just in case
			if(Double.isNaN(promotion.promoValue)) {
				logger.debug("Promotion Object ID: {}. Attribute: promoValue has value of NaN.", promotion.id);
			}
			throw new InvalidPromotionDataException("Promotion value cannot be 0. The value must be a positive number greater than one.");
		}
		
		// Validate startDate
		if(promotion.startDate == null) {
			throw new InvalidPromotionDataException("Promotion start date is empty. Please check your values.");
		}
		
		// Validate endDate
		if(promotion.endDate == null) {
			throw new InvalidPromotionDataException("Promotion end date is empty. Please check your values.");
		}
		
		// Validate isActive
		if(promotion.isActive != true || promotion.isActive != false) {
			throw new InvalidPromotionDataException("Promotion's active status cannot be blank. Please check your values.");
		}
		
		return isValidPromo;
	}
	
	private void validatePromotionForUpdate(long id, Promotion promotion) {
	    // (your null/blank checks here, fixed for correct ordering)

	    if (promotionRepository.existsByPromoCodeAndIdNot(promotion.getPromoCode(), id)) {
	        throw new InvalidPromotionDataException("PROMO CODE already exists in the system.");
	    }
	}
	
	/**
	 * Method to create a new Promotion object and store it in the database.
	 */
	public Promotion createPromotion(Promotion promotion) {
		// Validating the Promotion object
		validatePromotionObject(promotion);
		
		Promotion savedPromotion = promotionRepository.save(promotion);
		
		logger.info("Promotion added. ID: {} PROMO CODE: {}", savedPromotion.id, savedPromotion.promoCode);
		
		// returning the Promotion object returned by the .save() method
		// documentation recommends this approach, as the object may have been modified
		return savedPromotion;
	}
	
	/**
	 * Method to update an existing Promotion in the system. 
	 */
	@Transactional
	public Promotion updatePromotion(long id, Promotion incoming) {
		
		Promotion existing = getById(id);

	    validatePromotionForUpdate(id, incoming);

	    existing.setPromoCode(incoming.getPromoCode());
	    existing.setDescription(incoming.getDescription());
	    existing.setPromoType(incoming.getPromoType());
	    existing.setPromoValue(incoming.getPromoValue());
	    existing.setStartDate(incoming.getStartDate());
	    existing.setEndDate(incoming.getEndDate());
	    existing.setIsActive(incoming.getIsActive());

	    return promotionRepository.save(existing);
	}
	
	/**
	 * Method to delete a record from the system.
	 */
	public void deletePromotion(long id) throws InvalidPromotionDataException {
		Promotion promotion = getById(id);
		
		if(promotion == null) {
			throw new InvalidPromotionDataException("Promotion not found. Check ID: {} exists in the system.", id);
		}
		
		promotionRepository.deleteById(id);
	}

}