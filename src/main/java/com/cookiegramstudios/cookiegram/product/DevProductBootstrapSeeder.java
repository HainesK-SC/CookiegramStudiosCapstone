package com.cookiegramstudios.cookiegram.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DevProductBootstrapSeeder implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(DevProductBootstrapSeeder.class);
	
	private final ProductRepository productRepository;
	
	public DevProductBootstrapSeeder(ProductRepository prodRepo) {
		this.productRepository = prodRepo;
	}
	
	@Override
	public void run(String...args) {
		logger.info("Boostrapping product seed data...");
		
		createProductIfMissing("Chocolate Chip Cookie", "Cookie", "A rich and moist chocolate chip cookie!", "url", 10.99);
	}
	
	private void createProductIfMissing(
			String baseName, 
			String productType, 
			String baseDescription, 
			String imgUrl, 
			double price
	) {
		
		if(productRepository.findByBaseName(baseName) != null) {
			logger.info("Product already exists. Skipping bootstrap: {}", productType);
			return;
		}
		
		Product product = new Product();
		product.setBaseName(baseName);
		product.setProductType(productType);
		product.setBaseDescription(baseDescription);
		product.setBasePrice(price);
		
		productRepository.save(product);
		
		logger.info("Bootstrapped product: {}", productType);
	}
}
