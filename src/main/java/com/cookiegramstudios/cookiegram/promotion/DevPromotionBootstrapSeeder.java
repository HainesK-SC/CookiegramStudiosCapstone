package com.cookiegramstudios.cookiegram.promotion;


import java.math.BigDecimal;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DevPromotionBootstrapSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DevPromotionBootstrapSeeder.class);

    private final PromotionRepository promotionRepository;

    public DevPromotionBootstrapSeeder(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Override
    public void run(String... args) {

        logger.info("Bootstrapping promotion seed data...");

        createPromotionIfMissing(
                "SPRING5",
                "Seasonal spring discount",
                PromotionTypes.FIXED,
                5.00,
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2027, 5, 31),
                true
        );

        createPromotionIfMissing(
                "SWEET25",
                "Discount off entire order",
                PromotionTypes.PERCENTAGE,
                25.00,
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 6, 30),
                true
        );

        createPromotionIfMissing(
                "BDAY10",
                "Birthday celebration discount",
                PromotionTypes.FIXED,
                10.00,
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31),
                false
        );

        createPromotionIfMissing(
                "FREESHIP",
                "Free shipping on any order",
                PromotionTypes.FIXED,
                10.00,
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2027, 7, 1),
                true
        );

        createPromotionIfMissing(
                "HOLIDAY15",
                "Holiday season savings",
                PromotionTypes.PERCENTAGE,
                15.00,
                LocalDate.of(2026, 11, 1),
                LocalDate.of(2026, 12, 31),
                false
        );
    }

    private void createPromotionIfMissing(
            String promoCode,
            String description,
            PromotionTypes type,
            double value,
            LocalDate startDate,
            LocalDate endDate,
            boolean active) {

        if (promotionRepository.findByPromoCode(promoCode) != null) {
            logger.info("Promotion already exists. Skipping bootstrap: {}", promoCode);
            return;
        }

        Promotion promotion = new Promotion();
        promotion.setPromoCode(promoCode);
        promotion.setDescription(description);
        promotion.setPromoType(type);
        promotion.setPromoValue(value);
        promotion.setStartDate(startDate);
        promotion.setEndDate(endDate);
        promotion.setIsActive(active);

        promotionRepository.save(promotion);

        logger.info("Bootstrapped promotion: {}", promoCode);
    }
}