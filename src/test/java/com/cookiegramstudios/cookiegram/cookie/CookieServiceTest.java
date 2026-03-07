package com.cookiegramstudios.cookiegram.cookie;


import org.mockito.InjectMocks;
import org.mockito.Mock;

public class CookieServiceTest {
    @Mock // Creates a "fake" version of the respository so we don't need a real database
    private CookieRepository cookieRepository;

    @InjectMocks // Automatically injects the mocked repository into our service
    private CookieService cookieService;
}
