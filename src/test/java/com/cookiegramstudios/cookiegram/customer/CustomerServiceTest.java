package com.cookiegramstudios.cookiegram.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void findById_delegatesToRepository() {
        Customer customer = buildCustomer(1L, "a@example.com");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Optional<Customer> result = customerService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("a@example.com", result.get().getEmail());
        verify(customerRepository).findById(1L);
    }

    @Test
    void findByEmail_delegatesToRepository() {
        Customer customer = buildCustomer(1L, "a@example.com");
        when(customerRepository.findByEmail("a@example.com")).thenReturn(Optional.of(customer));

        Optional<Customer> result = customerService.findByEmail("a@example.com");

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(customerRepository).findByEmail("a@example.com");
    }

    @Test
    void existsByEmail_delegatesToRepository() {
        when(customerRepository.existsByEmail("exists@example.com")).thenReturn(true);

        boolean result = customerService.existsByEmail("exists@example.com");

        assertTrue(result);
        verify(customerRepository).existsByEmail("exists@example.com");
    }

    @Test
    void findAllCustomers_delegatesToRepository() {
        when(customerRepository.findAll()).thenReturn(List.of(
                buildCustomer(1L, "a@example.com"),
                buildCustomer(2L, "b@example.com")
        ));

        List<Customer> result = customerService.findAllCustomers();

        assertEquals(2, result.size());
        verify(customerRepository).findAll();
    }

    @Test
    void findCustomersCreatedAfter_delegatesToRepository() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(7);
        when(customerRepository.findByCreatedAtAfter(cutoff)).thenReturn(List.of(
                buildCustomer(2L, "new@example.com")
        ));

        List<Customer> result = customerService.findCustomersCreatedAfter(cutoff);

        assertEquals(1, result.size());
        assertEquals("new@example.com", result.get(0).getEmail());
        verify(customerRepository).findByCreatedAtAfter(cutoff);
    }

    @Test
    void findActiveCustomers_delegatesToRepository() {
        Customer active = buildCustomer(1L, "active@example.com");
        active.setLastOrderDate(LocalDateTime.now().minusDays(1));
        when(customerRepository.findByLastOrderDateIsNotNull()).thenReturn(List.of(active));

        List<Customer> result = customerService.findActiveCustomers();

        assertEquals(1, result.size());
        assertNotNull(result.get(0).getLastOrderDate());
        verify(customerRepository).findByLastOrderDateIsNotNull();
    }

    @Test
    void findCustomersByRecentActivity_delegatesToRepository() {
        when(customerRepository.findAllByOrderByLastOrderDateDesc()).thenReturn(List.of(
                buildCustomer(1L, "recent@example.com"),
                buildCustomer(2L, "older@example.com")
        ));

        List<Customer> result = customerService.findCustomersByRecentActivity();

        assertEquals(2, result.size());
        verify(customerRepository).findAllByOrderByLastOrderDateDesc();
    }

    @Test
    void createCustomerProfile_whenEmailNotTaken_saves() {
        Customer input = buildCustomer(null, "new@example.com");
        when(customerRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(customerRepository.save(input)).thenAnswer(invocation -> {
            Customer c = invocation.getArgument(0);
            c.setId(100L);
            return c;
        });

        Customer result = customerService.createCustomerProfile(input);

        assertEquals(100L, result.getId());
        assertEquals("new@example.com", result.getEmail());
        verify(customerRepository).existsByEmail("new@example.com");
        verify(customerRepository).save(input);
    }

    @Test
    void createCustomerProfile_whenEmailExists_throwsIllegalArgumentException() {
        Customer input = buildCustomer(null, "dup@example.com");
        when(customerRepository.existsByEmail("dup@example.com")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> customerService.createCustomerProfile(input)
        );

        assertTrue(ex.getMessage().contains("already exists"));
        assertTrue(ex.getMessage().contains("dup@example.com"));
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void updateCustomerProfile_whenIdNull_throwsIllegalArgumentException() {
        Customer input = buildCustomer(null, "nullid@example.com");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> customerService.updateCustomerProfile(input)
        );

        assertTrue(ex.getMessage().contains("does not exist"));
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void updateCustomerProfile_whenIdNotFound_throwsIllegalArgumentException() {
        Customer input = buildCustomer(999L, "missing@example.com");
        when(customerRepository.existsById(999L)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> customerService.updateCustomerProfile(input)
        );

        assertTrue(ex.getMessage().contains("999"));
        verify(customerRepository).existsById(999L);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void updateCustomerProfile_whenIdExists_saves() {
        Customer input = buildCustomer(10L, "update@example.com");
        when(customerRepository.existsById(10L)).thenReturn(true);
        when(customerRepository.save(input)).thenReturn(input);

        Customer result = customerService.updateCustomerProfile(input);

        assertEquals(10L, result.getId());
        assertEquals("update@example.com", result.getEmail());
        verify(customerRepository).existsById(10L);
        verify(customerRepository).save(input);
    }

    @Test
    void updateLastOrderDate_whenCustomerFound_updatesDateAndSaves() {
        Customer customer = buildCustomer(20L, "active@example.com");
        customer.setLastOrderDate(null);
        when(customerRepository.findById(20L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        customerService.updateLastOrderDate(20L);

        ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(captor.capture());

        Customer saved = captor.getValue();
        assertNotNull(saved.getLastOrderDate());
        verify(customerRepository).findById(20L);
    }

    @Test
    void updateLastOrderDate_whenCustomerMissing_throwsIllegalArgumentException() {
        when(customerRepository.findById(404L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> customerService.updateLastOrderDate(404L)
        );

        assertTrue(ex.getMessage().contains("404"));
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void deleteCustomerProfile_whenExists_deletes() {
        when(customerRepository.existsById(30L)).thenReturn(true);

        customerService.deleteCustomerProfile(30L);

        verify(customerRepository).existsById(30L);
        verify(customerRepository).deleteById(30L);
    }

    @Test
    void deleteCustomerProfile_whenMissing_throwsIllegalArgumentException() {
        when(customerRepository.existsById(31L)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> customerService.deleteCustomerProfile(31L)
        );

        assertTrue(ex.getMessage().contains("31"));
        verify(customerRepository).existsById(31L);
        verify(customerRepository, never()).deleteById(anyLong());
    }

    @Test
    void findOrCreateByEmail_whenExisting_returnsExistingWithoutSave() {
        Customer existing = buildCustomer(40L, "exists@example.com");
        when(customerRepository.findByEmail("exists@example.com")).thenReturn(Optional.of(existing));

        Customer result = customerService.findOrCreateByEmail("exists@example.com");

        assertEquals(40L, result.getId());
        assertEquals("exists@example.com", result.getEmail());
        verify(customerRepository).findByEmail("exists@example.com");
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void findOrCreateByEmail_whenMissing_createsAndSaves() {
        when(customerRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer c = invocation.getArgument(0);
            c.setId(50L);
            return c;
        });

        Customer result = customerService.findOrCreateByEmail("new@example.com");

        assertEquals(50L, result.getId());
        assertEquals("new@example.com", result.getEmail());
        verify(customerRepository).findByEmail("new@example.com");
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void countAllCustomers_returnsRepositoryCount() {
        when(customerRepository.count()).thenReturn(123L);

        long result = customerService.countAllCustomers();

        assertEquals(123L, result);
        verify(customerRepository).count();
    }

    private Customer buildCustomer(Long id, String email) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setEmail(email);
        customer.setCreatedAt(LocalDateTime.now().minusDays(10));
        return customer;
    }
}