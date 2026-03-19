package com.cookiegramstudios.cookiegram.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

	@Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        persistUser(
                "employee1@example.com",
                "password123",
                UserRole.EMPLOYEE,
                "Emp",
                "One"
        );
    }

    @Test
    void findByEmail_whenExists_returnsUser() {
        User found = userRepository.findByEmail("employee1@example.com");

        assertNotNull(found);
        assertEquals("employee1@example.com", found.getEmail());
    }

    @Test
    void findByEmail_whenMissing_returnsNull() {
        User found = userRepository.findByEmail("missing@example.com");

        assertNull(found);
    }

    @Test
    void findByRole_whenExists_returnsUser() {
        User found = userRepository.findByRole(UserRole.EMPLOYEE);

        assertNotNull(found);
        assertEquals(UserRole.EMPLOYEE, found.getRole());
    }

    @Test
    void findByRole_whenNoUsersWithRole_returnsNull() {
        User found = userRepository.findByRole(UserRole.ADMIN);

        assertNull(found);
    }

    @Test
    void findAllByRole_whenSingleMatch_returnsOne() {
        List<User> users = userRepository.findAllByRole(UserRole.EMPLOYEE);

        assertEquals(1, users.size());
        assertEquals("employee1@example.com", users.get(0).getEmail());
    }

    @Test
    void findAllByRole_whenMultipleMatches_returnsOnlyMatchingRole() {
        persistUser(
                "employee2@example.com",
                "password123",
                UserRole.EMPLOYEE,
                "Emp",
                "Two"
        );
        persistUser(
                "admin1@example.com",
                "password123",
                UserRole.ADMIN,
                "Admin",
                "One"
        );

        List<User> employees = userRepository.findAllByRole(UserRole.EMPLOYEE);
        List<User> admins = userRepository.findAllByRole(UserRole.ADMIN);

        assertEquals(2, employees.size());
        assertTrue(employees.stream().allMatch(u -> u.getRole() == UserRole.EMPLOYEE));

        assertEquals(1, admins.size());
        assertEquals("admin1@example.com", admins.get(0).getEmail());
    }

    @Test
    void findAllByRole_whenNoMatches_returnsEmptyList() {
        List<User> admins = userRepository.findAllByRole(UserRole.ADMIN);

        assertNotNull(admins);
        assertTrue(admins.isEmpty());
    }

    @Test
    void existsByEmail_whenExists_returnsTrue() {
        assertTrue(userRepository.existsByEmail("employee1@example.com"));
    }

    @Test
    void existsByEmail_whenMissing_returnsFalse() {
        assertFalse(userRepository.existsByEmail("nonexistent@example.com"));
    }

    @Test
    void existsByEmail_isCaseSensitiveByDefault() {
        // Documents current JPA/H2 behavior for exact-match lookup.
        assertFalse(userRepository.existsByEmail("EMPLOYEE1@example.com"));
    }

    private User persistUser(
            String email,
            String password,
            UserRole role,
            String firstName,
            String lastName
    ) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setCreatedAt(LocalDateTime.now());

        entityManager.persist(user);
        entityManager.flush();

        return user;
    }
}
