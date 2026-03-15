package com.cookiegramstudios.cookiegram.product;

import jakarta.persistence.*;

/**
 * Class to represent a base product being offered - without customization.
 * This will allow for management to easily modify the currently offered products,
 * add more products, and/or delete current products entirely.
 * 
 * This will also make dynamic data retrieval possible for every product offered
 * for sale, and in cases where customization is an option, to display
 * the default and pre-customization values, mainly applicable for price.
 * 
 * @author Kyle Haines
 */

@Entity
@Table(name = "products")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false)
	private String baseName;
	
	@Column(nullable = false)
	private String baseDescription;
	
	private String imageUrl;
	
	@Column(nullable = false)
	private double basePrice;
	
	public Product() {
		
	}
	
	public Product(String name, String description, String imageUrl, double price) {
		this.baseName = name;
		this.baseDescription = description;
		this.imageUrl = imageUrl;
		this.basePrice = price;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBaseName() {
		return baseName;
	}

	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}

	public String getBaseDescription() {
		return baseDescription;
	}

	public void setBaseDescription(String baseDescription) {
		this.baseDescription = baseDescription;
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
	
	
}
