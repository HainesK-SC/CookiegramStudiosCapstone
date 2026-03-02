package com.cookiegramstudios.cookiegram.promotion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
//import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.cookiegramstudios.cookiegram.common.exceptions.InvalidPromotionDataException;

class PromotionServiceTest {

    private PromotionRepository promotionRepository;
    private PromotionService promotionService;

    @BeforeEach
    void setUp() {
        promotionRepository = mock(PromotionRepository.class);
        promotionService = new PromotionService(promotionRepository);
    }

    @Test
    @DisplayName("getAllPromotions should return all promotions from repository")
    void getAllPromotions_returnsAll() {
        Promotion p1 = buildPromotion("SAVE10");
        Promotion p2 = buildPromotion("FIVEBUCKS");

        when(promotionRepository.findAll()).thenReturn(List.of(p1, p2));

        List<Promotion> results = promotionService.getAllPromotions();

        assertThat(results).hasSize(2);
        verify(promotionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getById should return promotion when found")
    void getById_whenFound_returnsPromotion() {
        Promotion existing = buildPromotion("SAVE10");
        existing.setId(1L);

        when(promotionRepository.findById(1L)).thenReturn(Optional.of(existing));

        Promotion result = promotionService.getById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getPromoCode()).isEqualTo("SAVE10");
        verify(promotionRepository).findById(1L);
    }

    @Test
    @DisplayName("getById should throw when not found")
    void getById_whenNotFound_throws() {
        when(promotionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> promotionService.getById(99L))
                .isInstanceOf(InvalidPromotionDataException.class);

        verify(promotionRepository).findById(99L);
    }

    @Test
    @DisplayName("getByPromoCode should return a promotion")
    void getByPromoCode_returnsPromotion() {
        Promotion existing = buildPromotion("SAVE10");
        when(promotionRepository.findByPromoCode("SAVE10")).thenReturn(existing);

        Promotion result = promotionService.getByPromoCode("SAVE10");

        assertThat(result).isNotNull();
        assertThat(result.getPromoCode()).isEqualTo("SAVE10");
        verify(promotionRepository).findByPromoCode("SAVE10");
    }

    @Test
    @DisplayName("getByPromoType should return promotions of that type")
    void getByPromoType_returnsList() {
        Promotion p1 = buildPromotion("SAVE10");
        p1.setPromoType(PromotionTypes.PERCENTAGE);

        when(promotionRepository.findByPromoType(PromotionTypes.PERCENTAGE)).thenReturn(List.of(p1));

        List<Promotion> result = promotionService.getByPromoType(PromotionTypes.PERCENTAGE);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPromoType()).isEqualTo(PromotionTypes.PERCENTAGE);
        verify(promotionRepository).findByPromoType(PromotionTypes.PERCENTAGE);
    }

    @Test
    @DisplayName("getByPromoValue should return promotions by value")
    void getByPromoValue_returnsList() {
        Promotion p1 = buildPromotion("FIVEBUCKS");
        p1.setPromoValue(5.0);

        when(promotionRepository.findByPromoValue(5.0)).thenReturn(List.of(p1));

        List<Promotion> result = promotionService.getByPromoValue(5.0);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPromoValue()).isEqualTo(5.0);
        verify(promotionRepository).findByPromoValue(5.0);
    }

    @Test
    @DisplayName("getByStartDate should return promotions by start date")
    void getByStartDate_returnsList() {
        LocalDate start = LocalDate.of(2026, 1, 1);
        Promotion p1 = buildPromotion("SAVE10");
        p1.setStartDate(start);

        when(promotionRepository.findByStartDate(start)).thenReturn(List.of(p1));

        List<Promotion> result = promotionService.getByStartDate(start);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStartDate()).isEqualTo(start);
        verify(promotionRepository).findByStartDate(start);
    }

    @Test
    @DisplayName("getByIsActive should return promotions filtered by active flag")
    void getByIsActive_returnsList() {
        Promotion active = buildPromotion("SAVE10");
        active.setIsActive(true);

        when(promotionRepository.findByIsActive(true)).thenReturn(List.of(active));

        List<Promotion> result = promotionService.getByIsActive(true);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIsActive()).isTrue();
        verify(promotionRepository).findByIsActive(true);
    }

    @Test
    @DisplayName("updatePromotion should copy fields onto existing promotion and save")
    void updatePromotion_copiesFieldsAndSaves() {
        Promotion existing = buildPromotion("OLD");
        existing.setId(10L);

        Promotion incoming = buildPromotion("NEW");
        incoming.setDescription("Updated");
        incoming.setPromoType(PromotionTypes.PERCENTAGE);
        incoming.setPromoValue(15.0);
        incoming.setStartDate(LocalDate.of(2026, 2, 1));
        incoming.setEndDate(LocalDate.of(2026, 3, 1));
        incoming.setIsActive(false);

        when(promotionRepository.findById(10L)).thenReturn(Optional.of(existing));
        when(promotionRepository.findByPromoCode(anyString())).thenReturn(null);
        when(promotionRepository.save(any(Promotion.class))).thenAnswer(inv -> inv.getArgument(0));

        Promotion updated = promotionService.updatePromotion(10L, incoming);

        ArgumentCaptor<Promotion> captor = ArgumentCaptor.forClass(Promotion.class);
        verify(promotionRepository).save(captor.capture());

        Promotion saved = captor.getValue();
        assertThat(saved.getId()).isEqualTo(10L);
        assertThat(saved.getPromoCode()).isEqualTo("NEW");
        assertThat(saved.getDescription()).isEqualTo("Updated");
        assertThat(saved.getPromoType()).isEqualTo(PromotionTypes.PERCENTAGE);
        assertThat(saved.getPromoValue()).isEqualTo(15.0);
        assertThat(saved.getStartDate()).isEqualTo(LocalDate.of(2026, 2, 1));
        assertThat(saved.getEndDate()).isEqualTo(LocalDate.of(2026, 3, 1));
        assertThat(saved.getIsActive()).isFalse();

        assertThat(updated.getPromoCode()).isEqualTo("NEW");
    }

    @Test
    @DisplayName("deletePromotion should call deleteById when promotion exists")
    void deletePromotion_whenExists_deletes() {
        Promotion existing = buildPromotion("SAVE10");
        existing.setId(7L);

        when(promotionRepository.findById(7L)).thenReturn(Optional.of(existing));

        promotionService.deletePromotion(7L);

        verify(promotionRepository).deleteById(7L);
    }

    // ---- Helpers ----

    private Promotion buildPromotion(String promoCode) {
        Promotion p = new Promotion();
        p.setPromoCode(promoCode);
        p.setDescription("Test promo");
        p.setPromoType(PromotionTypes.FIXED);
        p.setPromoValue(5.0);
        p.setStartDate(LocalDate.of(2026, 1, 1));
        p.setEndDate(LocalDate.of(2026, 12, 31));
        p.setIsActive(true);
        return p;
    }
}