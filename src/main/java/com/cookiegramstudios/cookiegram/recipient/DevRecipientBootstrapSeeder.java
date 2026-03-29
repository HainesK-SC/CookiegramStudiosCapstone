package com.cookiegramstudios.cookiegram.recipient;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DevRecipientBootstrapSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DevRecipientBootstrapSeeder.class);

    private final RecipientRepository recipientRepository;

    public DevRecipientBootstrapSeeder(RecipientRepository recipientRepository) {
        this.recipientRepository = recipientRepository;
    }

    @Override
    public void run(String... args) {
        logger.info("Bootstrapping recipient seed data...");

        createRecipientIfMissing(
                "Alice", "Thompson",
                "123 Maple Street", "Toronto", "M5H 2N2", "Canada",
                "Leave at front door"
        );

        createRecipientIfMissing(
                "Brian", "Nguyen",
                "456 Oak Avenue", "Mississauga", "L5B 3K9", "Canada",
                null
        );
    }

    private void createRecipientIfMissing(
            String firstName, String lastName,
            String street, String city, String postalCode, String country,
            String specialInstructions) {

        List<Recipient> existing = recipientRepository.findByFirstNameIgnoreCase(firstName);
        if (!existing.isEmpty()) {
            logger.info("Recipient already exists. Skipping bootstrap: {} {}", firstName, lastName);
            return;
        }

        Recipient recipient = new Recipient();
        recipient.setName(firstName + " " + lastName);
        recipient.setFirstName(firstName);
        recipient.setLastName(lastName);
        recipient.setStreet(street);
        recipient.setCity(city);
        recipient.setPostalCode(postalCode);
        recipient.setCountry(country);
        recipient.setSpecialInstructions(specialInstructions);

        recipientRepository.save(recipient);
        logger.info("Bootstrapped recipient: {} {}", firstName, lastName);
    }
}