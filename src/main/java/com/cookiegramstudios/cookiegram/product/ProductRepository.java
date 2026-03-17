package com.cookiegramstudios.cookiegram.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Simple JPA Repository layer that will interact with the Database
 * to retrieve 
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	
	Product findByBaseName(String baseName);
	
	Product findByProductType(String productType);
	
	Product findByBasePrice(double price);
	
	@Override
	Optional<Product> findById(Long id);
	
	List<Product> findAll();
}
