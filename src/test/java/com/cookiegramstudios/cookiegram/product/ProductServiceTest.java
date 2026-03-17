package com.cookiegramstudios.cookiegram.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;
    private List<Product> testProducts;

    @BeforeEach
    public void setUp() {
        testProduct = new Product("Test Cookie", "A test cookie", null, 10.00);
        testProduct.setId(1L);

        testProducts = new ArrayList<>();
        testProducts.add(testProduct);
    }

    // getAllProducts() tests

    @Test
    public void getAllProducts_ShouldReturnAllProducts() {
        when(productRepository.findAll()).thenReturn(testProducts);

        List<Product> result = productService.getAllProducts();

        assertEquals(testProducts, result);
    }

    @Test
    public void getAllProducts_ShouldReturnCorrectSize() {
        when(productRepository.findAll()).thenReturn(testProducts);

        List<Product> result = productService.getAllProducts();

        assertEquals(1, result.size());
    }

    @Test
    public void getAllProducts_EmptyRepository_ShouldReturnEmptyList() {
        when(productRepository.findAll()).thenReturn(new ArrayList<>());

        List<Product> result = productService.getAllProducts();

        assertTrue(result.isEmpty());
    }

    @Test
    public void getAllProducts_ShouldCallRepositoryOnce() {
        when(productRepository.findAll()).thenReturn(testProducts);

        productService.getAllProducts();

        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void getAllProducts_MultipleProducts_ShouldReturnAll() {
        Product secondProduct = new Product("Test Brownie", "A test brownie", null, 8.00);
        secondProduct.setId(2L);
        testProducts.add(secondProduct);

        when(productRepository.findAll()).thenReturn(testProducts);

        List<Product> result = productService.getAllProducts();

        assertEquals(2, result.size());
    }
}