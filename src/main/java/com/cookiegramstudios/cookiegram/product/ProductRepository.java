package com.cookiegramstudios.cookiegram.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Simple JPA Repository layer that will interact with the Database
 * to retrieve 
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
	Product findByBasePrice(double price);
	
	Product findByProductType(String productType);
	
	@Override
	Optional<Product> findById(Long id);
	
	List<Product> findAll();
}
