package com.cookiegramstudios.cookiegram.customer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a customer profile in the CookieGram system.
 * <p>
 * Customers are individuals who place orders through the public-facing application.
 * They do not have access to admin panels or internal dashboards. Customer profiles
 * track basic contact information and order history metadata.
 * </p>
 * <p>
 * The entity includes the following key attributes:
 * </p>
 * <ul>
 * <li><b>id</b> - Unique identifier for the customer profile</li>
 * <li><b>email</b> - Customer's email address for order confirmations and communications</li>
 * <li><b>createdAt</b> - Timestamp of when the customer profile was created</li>
 * <li><b>lastOrderDate</b> - Timestamp of the customer's most recent order (for analytics)</li>
 * </ul>
 * <p>
 * <b>Future Considerations:</b>
 * </p>
 * <ul>
 * <li>Link to User entity if customers are allowed to create accounts</li>
 * <li>Support for guest checkout without account creation</li>
 * <li>Integration with order history and analytics</li>
 * </ul>
 *
 * @author Matthew Samaha
 * @date 2026-02-24
 * @version 1.0
 */
@Entity
@Table(name = "customer_profiles")
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private LocalDateTime lastOrderDate;

    /**
     * Automatically set creation timestamp before persisting.
     */
    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
    }

    /**
     * Updates the last order date to the current timestamp.
     * <p>
     * Should be called whenever a customer places a new order.
     * </p>
     */
    public void updateLastOrderDate(){
        lastOrderDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastOrderDate() {
        return lastOrderDate;
    }

    public void setLastOrderDate(LocalDateTime lastOrderDate) {
        this.lastOrderDate = lastOrderDate;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                ", lastOrderDate=" + lastOrderDate +
                '}';
    }
}
