package com.cookiegramstudios.cookiegram.cookie;

import jakarta.persistence.*;
import lombok.*;

/**
 * Cookie entity representing a product available in the system.
 *
 * @name Nguyen Anh Khoa Tran
 * @date 2026-02-28
 * @version 1.0
 */
@Entity
@Table(name = "cookies")
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

	// No-args constructor
	public Cookie() {
	}

	// All-args constructor
	public Cookie(Long id, String name, String description, String imageUrl, double basePrice, boolean active) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.imageUrl = imageUrl;
		this.basePrice = basePrice;
		this.active = active;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
