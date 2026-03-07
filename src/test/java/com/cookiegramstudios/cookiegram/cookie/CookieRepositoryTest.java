package com.cookiegramstudios.cookiegram.cookie;

import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest // Starts a specialized, lightweight database for testing JPA components
public class CookieRepositoryTest {
    @Autowired
    private CookieRepository cookieRepository;

    @Test
    void testSaveAndFindByActive() {
        // ARRANGE: Create one active and one inactive cookie
        Cookie activeCookie = new Cookie(null, "Sugar Cookie", "Classic", "url", 2.00, true);
        Cookie inactiveCookie = new Cookie(null, "Old Recipe", "Discontinued", "url", 1.50, false);

}
