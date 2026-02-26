package com.cookiegramstudios.cookiegram.customization;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cookie_customizations")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CookieCustomization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cookie_id", nullable = false)
    private Cookie cookie;

    private String icingType;
    private String toppings;
    private String messageText;
    private double additionalCost;

    // Future-proofing for dietary info
    private String specialDietaryInfo;
}
