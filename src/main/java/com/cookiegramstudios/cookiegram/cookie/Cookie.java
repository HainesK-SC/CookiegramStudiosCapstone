package com.cookiegramstudios.cookiegram.cookie;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cookies")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cookie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    private String imageUrl;

    @Column(nullable = false)
    private double basePrice;

    @Column(nullable = false)
    private boolean active;

}
