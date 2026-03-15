package com.cookiegramstudios.cookiegram.product;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class that will interact with the Product class to
 * perform various logical tasks related to a Product object
 * or all Product objects.
 * 
 * @author Kyle Haines
 */
public class ProductService {
	private final ProductRepository productRepository;
	
	public ProductService(ProductRepository productRepo) {
		this.productRepository = productRepo;
	}
	
	public List<Product> getAllProducts() {
		List<Product> allProducts = productRepository.findAll();
		
		return allProducts;
	}
}
