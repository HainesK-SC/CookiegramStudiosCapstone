package com.cookiegramstudios.cookiegram.customer;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DevCustomerBootstrapSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DevCustomerBootstrapSeeder.class);

    private final CustomerRepository customerRepository;

    public DevCustomerBootstrapSeeder(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) {

        logger.info("Bootstrapping dev customer data...");

        createCustomerIfMissing(
                "alice@example.com",
                LocalDateTime.now().minusMonths(2),
                LocalDateTime.now().minusDays(10)
        );

        createCustomerIfMissing(
                "bob@example.com",
                LocalDateTime.now().minusMonths(3),
                LocalDateTime.now().minusDays(20)
        );

        createCustomerIfMissing(
                "carol@example.com",
                LocalDateTime.now().minusMonths(1),
                null
        );
    }

    private void createCustomerIfMissing(
            String email,
            LocalDateTime createdAt,
            LocalDateTime lastOrderDate) {

        if (customerRepository.existsByEmail(email)) {
            logger.info("Customer already exists. Skipping bootstrap: {}", email);
            return;
        }

        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setCreatedAt(createdAt);
        customer.setLastOrderDate(lastOrderDate);

        customerRepository.save(customer);

        logger.info("Bootstrapped customer: {}", email);
    }
}