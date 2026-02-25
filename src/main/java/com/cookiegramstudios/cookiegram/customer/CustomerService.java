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
     * General methods
     */
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    public List<Customer> findAllCustomers() {
        return customerRepository.findAll();
    }

    public List<Customer> findCustomersCreatedAfter(LocalDateTime date) {
        return customerRepository.findByCreatedAtAfter(date);
    }

    public List<Customer> findActiveCustomers() {
        return customerRepository.findByLastOrderDateIsNotNull();
    }

    public List<Customer> findCustomersByRecentActivity() {
        return customerRepository.findAllByOrderByLastOrderDateDesc();
    }

    /**
     * CRUD Methods
     */
    @Transactional
    public Customer createCustomerProfile(Customer customerProfile) {
        if (existsByEmail(customerProfile.getEmail())) {
            throw new IllegalArgumentException("Customer profile already exists with email: " + customerProfile.getEmail());
        }
        return customerRepository.save(customerProfile);
    }

    @Transactional
    public Customer updateCustomerProfile(Customer customerProfile) {
        if (customerProfile.getId() == null || !customerRepository.existsById(customerProfile.getId())) {
            throw new IllegalArgumentException("Customer profile does not exist with ID: " + customerProfile.getId());
        }
        return customerRepository.save(customerProfile);
    }

    @Transactional
    public void updateLastOrderDate(Long customerId) {
        Customer profile = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer profile not found with ID: " + customerId));
        profile.updateLastOrderDate();
        customerRepository.save(profile);
    }

    @Transactional
    public void deleteCustomerProfile(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new IllegalArgumentException("Customer profile not found with ID: " + id);
        }
        customerRepository.deleteById(id);
    }

    @Transactional
    public Customer findOrCreateByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseGet(() -> {
                    Customer newProfile = new Customer();
                    newProfile.setEmail(email);
                    return customerRepository.save(newProfile);
                });
    }

    /**
     * Helper methods
     */
    public long countAllCustomers() {
        return customerRepository.count();
    }


}
