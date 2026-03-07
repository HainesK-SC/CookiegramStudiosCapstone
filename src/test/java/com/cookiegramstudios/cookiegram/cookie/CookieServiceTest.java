package com.cookiegramstudios.cookiegram.cookie;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CookieServiceTest {
    @Mock // Creates a "fake" version of the respository so we don't need a real database
    private CookieRepository cookieRepository;

    @InjectMocks // Automatically injects the mocked repository into our service
    private CookieService cookieService;

    @Test
    void testGetAllCookies_Success() {
        // ARRANGE: Setup the "mock" data we expect the repository to return
        Cookie mockCookie = new Cookie(1L, "Snickerdoodle", "Cinnamon sugar", "url", 3.00, true);
        when(cookieRepository.findAll()).thenReturn(List.of(mockCookie));

        // ACT: Call the actual method in the Service
        List<Cookie> result = cookieService.getAllCookies();

        // ASSERT: Verify the results match our expectations
        assertEquals(1, result.size(), "Should return exactly one cookie");
        assertEquals("Snickerdoodle", result.get(0).getName());

        // Ensure the repository method was actually called exactly once
        verify(cookieRepository, times(1)).findAll();
    }
}
