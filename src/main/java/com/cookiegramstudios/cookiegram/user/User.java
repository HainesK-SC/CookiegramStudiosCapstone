package com.cookiegramstudios.cookiegram.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a user in the CookieGram system.
 * <p>
 * Users are authenticated and authorized based on their assigned role.
 * Each user has a unique ID and email address, with the email serving
 * as the login identifier.
 * </p>
 * <p>
 * The entity includes the following key attributes:
 * </p>
 * <ul>
 * <li><b>id</b> - Unique identifier for the user</li>
 * <li><b>email</b> - User's email address, used for authentication and login</li>
 * <li><b>password</b> - User's password, stored securely (should be encrypted)</li>
 * <li><b>role</b> - User's role within the system, determines access permissions</li>
 * <li><b>firstName</b> - User's first name</li>
 * <li><b>lastName</b> - User's last name</li>
 * <li><b>createdAt</b> - Timestamp of when the user account was created</li>
 * </ul>
 *
 * @author Matthew Samaha
 * @date 2026-02-18
 * @version 1.0
 */
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email is required")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotBlank(message = "Role is required")
    private UserRole role;

    @Column(nullable = false)
    @NotBlank(message = "First name is required")
    private String firstName;

    @Column(nullable = false)
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;


    /**
     * Automatically set creation timestamp before persisting
     */
    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
