package com.cookiegramstudios.cookiegram.cookie;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // Starts a specialized, lightweight database for testing JPA components
public class CookieRepositoryTest {
    @Autowired
    private CookieRepository cookieRepository;

    @Test
    void testSaveAndFindByActive() {
        // ARRANGE: Create one active and one inactive cookie
        Cookie activeCookie = new Cookie(null, "Sugar Cookie", "Classic", "url", 2.00, true);
        Cookie inactiveCookie = new Cookie(null, "Old Recipe", "Discontinued", "url", 1.50, false);

        cookieRepository.save(activeCookie);
        cookieRepository.save(inactiveCookie);

        // ACT: Retrieve only active cookies from the database
        List<Cookie> activeList = cookieRepository.findByActive(true);

        // ASSERT: Check that the filtering logic works correctly
        assertEquals(1, activeList.size(), "Should only find 1 active cookie");
        assertEquals("Sugar Cookie", activeList.get(0).getName());
    }
}
