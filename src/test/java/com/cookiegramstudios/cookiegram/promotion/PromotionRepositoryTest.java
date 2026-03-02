package com.cookiegramstudios.cookiegram.promotion;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
class PromotionRepositoryTest {

    @Autowired
    private PromotionRepository promotionRepository;

    @Test
    @DisplayName("findByPromoCode returns the matching Promotion")
    void findByPromoCode_returnsPromotion() {
        // Arrange
        Promotion p1 = buildPromotion("SAVE10", "Ten percent off", PromotionTypes.PERCENTAGE, 10.0,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31), true);

        Promotion p2 = buildPromotion("FIVEBUCKS", "Five dollars off", PromotionTypes.FIXED, 5.0,
                LocalDate.of(2026, 2, 1), LocalDate.of(2026, 6, 30), false);

        promotionRepository.save(p1);
        promotionRepository.save(p2);

        // Act
        Promotion found = promotionRepository.findByPromoCode("SAVE10");

        // Assert
        assertThat(found).isNotNull();
        assertThat(found.getPromoCode()).isEqualTo("SAVE10");
        assertThat(found.getPromoType()).isEqualTo(PromotionTypes.PERCENTAGE);
        assertThat(found.getPromoValue()).isEqualTo(10.0);
        assertThat(found.getIsActive()).isTrue();
    }

    @Test
    @DisplayName("findByPromoType returns all promotions of that type")
    void findByPromoType_returnsList() {
        // Arrange
        Promotion p1 = buildPromotion("SAVE10", "Ten percent off", PromotionTypes.PERCENTAGE, 10.0,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31), true);

        Promotion p2 = buildPromotion("SAVE15", "Fifteen percent off", PromotionTypes.PERCENTAGE, 15.0,
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 12, 31), true);

        Promotion p3 = buildPromotion("FIVEBUCKS", "Five dollars off", PromotionTypes.FIXED, 5.0,
                LocalDate.of(2026, 2, 1), LocalDate.of(2026, 6, 30), false);

        promotionRepository.saveAll(List.of(p1, p2, p3));

        // Act
        List<Promotion> percentagePromos = promotionRepository.findByPromoType(PromotionTypes.PERCENTAGE);

        // Assert
        assertThat(percentagePromos).hasSize(2);
        assertThat(percentagePromos)
                .allMatch(p -> p.getPromoType() == PromotionTypes.PERCENTAGE);
        assertThat(percentagePromos)
                .extracting(Promotion::getPromoCode)
                .containsExactlyInAnyOrder("SAVE10", "SAVE15");
    }

    @Test
    @DisplayName("findByPromoValue returns all promotions with that value")
    void findByPromoValue_returnsList() {
        // Arrange
        Promotion p1 = buildPromotion("FIVEBUCKS", "Five dollars off", PromotionTypes.FIXED, 5.0,
                LocalDate.of(2026, 2, 1), LocalDate.of(2026, 6, 30), true);

        Promotion p2 = buildPromotion("FIVEOFF2", "Also five dollars off", PromotionTypes.FIXED, 5.0,
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 5, 31), false);

        Promotion p3 = buildPromotion("SAVE10", "Ten percent off", PromotionTypes.PERCENTAGE, 10.0,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31), true);

        promotionRepository.saveAll(List.of(p1, p2, p3));

        // Act
        List<Promotion> promos = promotionRepository.findByPromoValue(5.0);

        // Assert
        assertThat(promos).hasSize(2);
        assertThat(promos).extracting(Promotion::getPromoCode)
                .containsExactlyInAnyOrder("FIVEBUCKS", "FIVEOFF2");
    }

    @Test
    @DisplayName("findByStartDate returns promotions that start on the given date")
    void findByStartDate_returnsList() {
        // Arrange
        LocalDate start = LocalDate.of(2026, 1, 1);

        Promotion p1 = buildPromotion("SAVE10", "Ten percent off", PromotionTypes.PERCENTAGE, 10.0,
                start, LocalDate.of(2026, 12, 31), true);

        Promotion p2 = buildPromotion("NEWYEAR5", "Five dollars off", PromotionTypes.FIXED, 5.0,
                start, LocalDate.of(2026, 1, 31), true);

        Promotion p3 = buildPromotion("FEBSALE", "Feb promo", PromotionTypes.FIXED, 3.0,
                LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2, 28), true);

        promotionRepository.saveAll(List.of(p1, p2, p3));

        // Act
        List<Promotion> results = promotionRepository.findByStartDate(start);

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results).extracting(Promotion::getPromoCode)
                .containsExactlyInAnyOrder("SAVE10", "NEWYEAR5");
    }

    @Test
    @DisplayName("findByEndDate returns promotions that end on the given date")
    void findByEndDate_returnsList() {
        // Arrange
        LocalDate end = LocalDate.of(2026, 12, 31);

        Promotion p1 = buildPromotion("SAVE10", "Ten percent off", PromotionTypes.PERCENTAGE, 10.0,
                LocalDate.of(2026, 1, 1), end, true);

        Promotion p2 = buildPromotion("SAVE15", "Fifteen percent off", PromotionTypes.PERCENTAGE, 15.0,
                LocalDate.of(2026, 3, 1), end, false);

        Promotion p3 = buildPromotion("SPRING", "Spring promo", PromotionTypes.FIXED, 7.0,
                LocalDate.of(2026, 4, 1), LocalDate.of(2026, 5, 31), true);

        promotionRepository.saveAll(List.of(p1, p2, p3));

        // Act
        List<Promotion> results = promotionRepository.findByEndDate(end);

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results).extracting(Promotion::getPromoCode)
                .containsExactlyInAnyOrder("SAVE10", "SAVE15");
    }

    @Test
    @DisplayName("findByIsActive returns active or inactive promotions")
    void findByIsActive_returnsList() {
        // Arrange
        Promotion active1 = buildPromotion("SAVE10", "Ten percent off", PromotionTypes.PERCENTAGE, 10.0,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31), true);

        Promotion active2 = buildPromotion("FIVEBUCKS", "Five dollars off", PromotionTypes.FIXED, 5.0,
                LocalDate.of(2026, 2, 1), LocalDate.of(2026, 6, 30), true);

        Promotion inactive = buildPromotion("OLDPROMO", "Expired promo", PromotionTypes.FIXED, 2.0,
                LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 31), false);

        promotionRepository.saveAll(List.of(active1, active2, inactive));

        // Act
        List<Promotion> activePromos = promotionRepository.findByIsActive(true);
        List<Promotion> inactivePromos = promotionRepository.findByIsActive(false);

        // Assert
        assertThat(activePromos).hasSize(2);
        assertThat(activePromos).allMatch(Promotion::getIsActive);

        assertThat(inactivePromos).hasSize(1);
        assertThat(inactivePromos.get(0).getPromoCode()).isEqualTo("OLDPROMO");
        assertThat(inactivePromos.get(0).getIsActive()).isFalse();
    }

    // --- helpers ---

    private Promotion buildPromotion(
            String promoCode,
            String description,
            PromotionTypes promoType,
            double promoValue,
            LocalDate startDate,
            LocalDate endDate,
            boolean isActive
    ) {
        Promotion p = new Promotion();
        p.setPromoCode(promoCode);
        p.setDescription(description);
        p.setPromoType(promoType);
        p.setPromoValue(promoValue);
        p.setStartDate(startDate);
        p.setEndDate(endDate);
        p.setIsActive(isActive);
        return p;
    }
}