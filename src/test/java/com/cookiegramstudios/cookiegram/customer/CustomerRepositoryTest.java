package com.cookiegramstudios.cookiegram.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

@DataJpaTest
class CustomerRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private CustomerRepository customerRepository;

	private Customer activeRecent;
	private Customer activeOlder;
	private Customer inactive;

	@BeforeEach
	void setUp() {
		activeRecent = persistCustomer("recent@example.com", LocalDateTime.now().minusDays(2),
				LocalDateTime.now().minusHours(3));

		activeOlder = persistCustomer("older@example.com", LocalDateTime.now().minusDays(20),
				LocalDateTime.now().minusDays(5));

		inactive = persistCustomer("inactive@example.com", LocalDateTime.now().minusDays(1), null);
	}

	@Test
	void findByEmail_whenExists_returnsOptionalPresent() {
		Optional<Customer> result = customerRepository.findByEmail("recent@example.com");

		assertTrue(result.isPresent());
		assertEquals(activeRecent.getId(), result.get().getId());
	}

	@Test
	void findByEmail_whenMissing_returnsOptionalEmpty() {
		Optional<Customer> result = customerRepository.findByEmail("missing@example.com");

		assertTrue(result.isEmpty());
	}

	@Test
	void existsByEmail_whenExists_returnsTrue() {
		assertTrue(customerRepository.existsByEmail("older@example.com"));
	}

	@Test
	void existsByEmail_whenMissing_returnsFalse() {
		assertFalse(customerRepository.existsByEmail("none@example.com"));
	}

	@Test
	void findByCreatedAtAfter_whenNoMatches_returnsEmptyList() {
		LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

		List<Customer> results = customerRepository.findByCreatedAtAfter(futureDate);

		assertNotNull(results);
		assertTrue(results.isEmpty());
	}

	@Test
	void findByLastOrderDateIsNotNull_returnsOnlyActiveCustomers() {
		List<Customer> results = customerRepository.findByLastOrderDateIsNotNull();

		assertEquals(2, results.size());
		assertTrue(results.stream().allMatch(c -> c.getLastOrderDate() != null));
		assertTrue(results.stream().anyMatch(c -> "recent@example.com".equals(c.getEmail())));
		assertTrue(results.stream().anyMatch(c -> "older@example.com".equals(c.getEmail())));
	}

	@Test
	void findByLastOrderDateIsNotNull_whenNoneActive_returnsEmptyList() {
		entityManager.clear();
		customerRepository.deleteAll();

		persistCustomer("inactive1@example.com", LocalDateTime.now().minusDays(3), null);
		persistCustomer("inactive2@example.com", LocalDateTime.now().minusDays(10), null);

		List<Customer> results = customerRepository.findByLastOrderDateIsNotNull();

		assertNotNull(results);
		assertTrue(results.isEmpty());
	}

	@Test
	void findAllByOrderByLastOrderDateDesc_returnsSortedByMostRecentFirst() {
		List<Customer> results = customerRepository.findAllByOrderByLastOrderDateDesc();

		assertEquals(3, results.size());

		int recentIndex = indexOfEmail(results, "recent@example.com");
		int olderIndex = indexOfEmail(results, "older@example.com");

		assertTrue(recentIndex >= 0);
		assertTrue(olderIndex >= 0);
		assertTrue(recentIndex < olderIndex);
	}

	@Test
	void findAllByOrderByLastOrderDateDesc_includesCustomersWithNullLastOrderDate() {
		List<Customer> results = customerRepository.findAllByOrderByLastOrderDateDesc();

		assertEquals(3, results.size());
		assertTrue(results.stream().anyMatch(c -> "inactive@example.com".equals(c.getEmail())));
	}

	private Customer persistCustomer(String email, LocalDateTime createdAt, LocalDateTime lastOrderDate) {
		Customer customer = new Customer();
		customer.setEmail(email);
		customer.setCreatedAt(createdAt);
		customer.setLastOrderDate(lastOrderDate);

		entityManager.persist(customer);
		entityManager.flush();
		return customer;
	}

	private int indexOfEmail(List<Customer> customers, String email) {
		for (int i = 0; i < customers.size(); i++) {
			if (email.equals(customers.get(i).getEmail())) {
				return i;
			}
		}
		return -1;
	}
}