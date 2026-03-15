package com.cookiegramstudios.cookiegram.order;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.cookiegramstudios.cookiegram.product.Product;
import com.cookiegramstudios.cookiegram.product.ProductRepository;

/**
 * Controller for order-related web endpoints.
 * <p>
 * Serves as the entry point for order workflow pages and interactions,
 * including order creation, review, and confirmation flows.
 * Endpoint methods will be added incrementally as the order feature set is implemented.
 * </p>
 * <p>
 * <b>Base Path:</b> {@code /order}
 * </p>
 *
 * @author Matthew Samaha
 * @auhor Kyle Haines
 * @date 2026-02-23
 * @date 2026-03-13
 * @version 1.1
 */
@Controller
public class OrderController {
	
	private final ProductRepository productRepository;
	
	public OrderController(ProductRepository prodRepo) {
		this.productRepository = prodRepo;
	}
	
	@GetMapping("/order")
	public String order(Model model) {
		List<Product> currentProducts = productRepository.findAll();
		model.addAttribute("currentProducts", currentProducts);
		return "order";
	}
}
