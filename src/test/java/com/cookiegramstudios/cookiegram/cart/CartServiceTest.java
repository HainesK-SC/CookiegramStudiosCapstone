package com.cookiegramstudios.cookiegram.cart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cookiegramstudios.cookiegram.product.Product;
import com.cookiegramstudios.cookiegram.product.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartService cartService;

    private Product testProduct;
    private Cart testCart;

    @BeforeEach
    public void setUp() {
        testProduct = new Product("Test Cookie", "A test cookie", null, 10.00);
        testProduct.setId(1L);

        testCart = new Cart();
    }

    // addToCart() tests

    @Test
    public void addToCart_NewItem_ShouldAddItemToCart() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        cartService.addToCart(testCart, 1L, 2);

        assertEquals(1, testCart.getCartItems().size());
    }

    @Test
    public void addToCart_NewItem_ShouldHaveCorrectQuantity() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        cartService.addToCart(testCart, 1L, 2);

        assertEquals(2, testCart.getCartItems().get(0).getItemQty());
    }

    @Test
    public void addToCart_NewItem_ShouldHaveCorrectProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        cartService.addToCart(testCart, 1L, 2);

        assertEquals(testProduct, testCart.getCartItems().get(0).getProductType());
    }

    @Test
    public void addToCart_DuplicateItem_ShouldNotIncreaseSizeOfCart() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        cartService.addToCart(testCart, 1L, 1);

        cartService.addToCart(testCart, 1L, 1);

        assertEquals(1, testCart.getCartItems().size());
    }

    @Test
    public void addToCart_DuplicateItem_ShouldIncrementQuantity() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        cartService.addToCart(testCart, 1L, 1);

        cartService.addToCart(testCart, 1L, 2);

        assertEquals(3, testCart.getCartItems().get(0).getItemQty());
    }

    @Test
    public void addToCart_ProductNotFound_ShouldThrowRuntimeException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            cartService.addToCart(testCart, 99L, 1);
        });
    }

    @Test
    public void addToCart_ProductNotFound_ShouldNotAddItemToCart() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        try {
            cartService.addToCart(testCart, 99L, 1);
        } catch (RuntimeException e) {
            // expected
        }

        assertEquals(0, testCart.getCartItems().size());
    }

    // getCartSize() tests

    @Test
    public void getCartSize_EmptyCart_ShouldReturnZero() {
        assertEquals(0, cartService.getCartSize(testCart));
    }

    @Test
    public void getCartSize_CartWithItems_ShouldReturnCorrectSize() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        cartService.addToCart(testCart, 1L, 1);

        assertEquals(1, cartService.getCartSize(testCart));
    }

    @Test
    public void getCartSize_CartWithMultipleItems_ShouldReturnCorrectSize() {
        Product secondProduct = new Product("Test Brownie", "A test brownie", null, 8.00);
        secondProduct.setId(2L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.findById(2L)).thenReturn(Optional.of(secondProduct));

        cartService.addToCart(testCart, 1L, 1);
        cartService.addToCart(testCart, 2L, 1);

        assertEquals(2, cartService.getCartSize(testCart));
    }
}