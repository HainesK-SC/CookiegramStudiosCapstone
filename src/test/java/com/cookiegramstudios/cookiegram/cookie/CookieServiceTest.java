package com.cookiegramstudios.cookiegram.cookie;


import org.mockito.Mock;

public class CookieServiceTest {
    @Mock // Creates a "fake" version of the respository so we don't need a real database
    private CookieRepository cookieRepository;
}
