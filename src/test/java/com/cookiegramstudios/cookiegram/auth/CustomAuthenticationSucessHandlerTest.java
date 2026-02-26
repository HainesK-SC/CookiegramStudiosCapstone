package com.cookiegramstudios.cookiegram.auth;

import org.junit.jupiter.api.BeforeEach;


/**
 * This will test the implementation in the CustomAuthenticationSuccessHandler
 */
public class CustomAuthenticationSucessHandlerTest {

    private CustomAuthenticationSuccessHandler successHandler;

    @BeforeEach
    void setUp(){
        successHandler = new CustomAuthenticationSuccessHandler();
    }

}
