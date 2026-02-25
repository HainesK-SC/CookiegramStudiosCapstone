package com.cookiegramstudios.cookiegram.recipient;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Represents a recipient of an order in the CookieGram system.
 * <p>
 * Recipients are the individuals who will receive delivered orders.
 * Each order has an associated recipient with delivery address and
 * any special instructions for delivery or personalization.
 * </p>
 * <p>
 * The entity includes the following key attributes:
 * </p>
 * <ul>
 * <li><b>id</b> - Unique identifier for the recipient</li>
 * <li><b>name</b> - Full name of the recipient</li>
 * <li><b>street</b> - Street address for delivery</li>
 * <li><b>city</b> - City for delivery</li>
 * <li><b>postalCode</b> - Postal/ZIP code for delivery</li>
 * <li><b>country</b> - Country for delivery</li>
 * <li><b>specialInstructions</b> - Optional delivery or personalization instructions</li>
 * </ul>
 * <p>
 * <b>Future Considerations:</b>
 * </p>
 * <ul>
 * <li>Implement address validation using postal code libraries</li>
 * <li>Support multiple recipients per customer for recurring orders</li>
 * <li>Add province/state field for regions requiring it</li>
 * <li>Integrate with delivery routing systems</li>
 * </ul>
 *
 * @author Matthew Samaha
 * @date 2026-02-24
 * @version 1.0
 */
@Entity
@Table(name = "recipients")
@AllArgsConstructor
@NoArgsConstructor
public class Recipient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Recipient name is required")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "Street address is required")
    private String street;

    @Column(nullable = false)
    @NotBlank(message = "City is required")
    private String city;

    @Column(nullable = false)
    @NotBlank(message = "Postal code is required")
    private String postalCode;

    @Column(nullable = false)
    @NotBlank(message = "Country is required")
    private String country;

    @Column(columnDefinition = "TEXT")
    private String specialInstructions;

    /**
     * Returns the full formatted address as a single string.
     * <p>
     * Useful for display purposes and delivery labels.
     * </p>
     *
     * @return formatted address string
     */
    public String getFullAddress() {
        return String.format("%s, %s, %s, %s",
                street, city, postalCode, country);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    @Override
    public String toString() {
        return "Recipient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", country='" + country + '\'' +
                ", specialInstructions='" + specialInstructions + '\'' +
                '}';
    }
}
