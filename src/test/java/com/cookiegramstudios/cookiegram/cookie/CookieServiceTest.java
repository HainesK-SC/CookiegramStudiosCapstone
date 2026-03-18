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
	@Mock 
	private CookieRepository cookieRepository;

	@InjectMocks 
	private CookieService cookieService;

	@Test
	void testGetAllCookies_Success() {
		Cookie mockCookie = new Cookie(1L, "Snickerdoodle", "Cinnamon sugar", "url", 3.00, true);
		when(cookieRepository.findAll()).thenReturn(List.of(mockCookie));

		List<Cookie> result = cookieService.getAllCookies();

		assertEquals(1, result.size(), "Should return exactly one cookie");
		assertEquals("Snickerdoodle", result.get(0).getName());

		verify(cookieRepository, times(1)).findAll();
	}
}
