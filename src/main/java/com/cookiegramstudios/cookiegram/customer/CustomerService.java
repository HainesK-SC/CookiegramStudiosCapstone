package com.cookiegramstudios.cookiegram.customer;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for customer profile-related business operations.
 * <p>
 * Provides application-facing methods for managing customer profiles and acts as an
 * abstraction between controllers and {@link CustomerRepository}.
 * Handles customer profile creation, retrieval, updates, and analytics operations.
 * </p>
 *
 * @author Matthew Samaha
 * @date 2026-02-24
 * @version 1.0
 */
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Finds a customer profile by its unique ID.
     *
     * @param id the customer profile ID
     * @return Optional containing the customer profile if found, empty otherwise
     */
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    /**
     * Finds a customer profile by email address.
     * <p>
     * Email serves as the primary identifier for customer lookup.
     * </p>
     *
     * @param email the customer's email address
     * @return Optional containing the customer profile if found, empty otherwise
     */
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    /**
     * Checks if a customer profile exists with the given email.
     *
     * @param email the email address to check
     * @return true if a profile exists, false otherwise
     */
    public boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    /**
     * Creates and saves a new customer profile.
     * <p>
     * Validates that the email is not already in use before creating the profile.
     * </p>
     *
     * @param customerProfile the customer profile to create
     * @return the saved customer profile
     * @throws IllegalArgumentException if a profile with the email already exists
     */
    @Transactional
    public Customer createCustomerProfile(Customer customerProfile) {
        if (existsByEmail(customerProfile.getEmail())) {
            throw new IllegalArgumentException("Customer profile already exists with email: " + customerProfile.getEmail());
        }
        return customerRepository.save(customerProfile);
    }

    /**
     * Updates an existing customer profile.
     *
     * @param customerProfile the customer profile with updated information
     * @return the updated customer profile
     * @throws IllegalArgumentException if the profile does not exist
     */
    @Transactional
    public Customer updateCustomerProfile(Customer customerProfile) {
        if (customerProfile.getId() == null || !customerRepository.existsById(customerProfile.getId())) {
            throw new IllegalArgumentException("Customer profile does not exist with ID: " + customerProfile.getId());
        }
        return customerRepository.save(customerProfile);
    }

    /**
     * Updates the last order date for a customer profile.
     * <p>
     * Should be called whenever a customer places a new order.
     * </p>
     *
     * @param customerId the ID of the customer who placed an order
     * @throws IllegalArgumentException if the customer profile does not exist
     */
    @Transactional
    public void updateLastOrderDate(Long customerId) {
        Customer profile = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer profile not found with ID: " + customerId));
        profile.updateLastOrderDate();
        customerRepository.save(profile);
    }

    /**
     * Retrieves all customer profiles.
     *
     * @return list of all customer profiles
     */
    public List<Customer> findAllCustomers() {
        return customerRepository.findAll();
    }

    /**
     * Finds all customer profiles created after a specific date.
     * <p>
     * Useful for tracking new customer acquisition over time.
     * </p>
     *
     * @param date the cutoff date
     * @return list of customer profiles created after the specified date
     */
    public List<Customer> findCustomersCreatedAfter(LocalDateTime date) {
        return customerRepository.findByCreatedAtAfter(date);
    }

    /**
     * Finds all customer profiles who have placed at least one order.
     *
     * @return list of active customer profiles
     */
    public List<Customer> findActiveCustomers() {
        return customerRepository.findByLastOrderDateIsNotNull();
    }

    /**
     * Finds all customer profiles ordered by most recent order first.
     * <p>
     * Useful for customer engagement analytics and identifying recent customers.
     * </p>
     *
     * @return list of customer profiles sorted by last order date (descending)
     */
    public List<Customer> findCustomersByRecentActivity() {
        return customerRepository.findAllByOrderByLastOrderDateDesc();
    }

    /**
     * Deletes a customer profile by ID.
     * <p>
     * Note: Consider implementing soft delete or archiving for data retention.
     * </p>
     *
     * @param id the customer profile ID to delete
     * @throws IllegalArgumentException if the customer profile does not exist
     */
    @Transactional
    public void deleteCustomerProfile(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new IllegalArgumentException("Customer profile not found with ID: " + id);
        }
        customerRepository.deleteById(id);
    }

    /**
     * Counts the total number of customer profiles.
     *
     * @return total count of customer profiles
     */
    public long countAllCustomers() {
        return customerRepository.count();
    }

    /**
     * Finds or creates a customer profile by email.
     * <p>
     * If a profile exists with the given email, returns it.
     * Otherwise, creates a new profile with the provided email.
     * Useful for guest checkout and order placement flows.
     * </p>
     *
     * @param email the customer's email address
     * @return the existing or newly created customer profile
     */
    @Transactional
    public Customer findOrCreateByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseGet(() -> {
                    Customer newProfile = new Customer();
                    newProfile.setEmail(email);
                    return customerRepository.save(newProfile);
                });
    }
}
