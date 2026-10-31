package com.inventory.app.service;

import com.inventory.app.dto.ProductDTO;
import com.inventory.app.entity.Product;
import com.inventory.app.exception.DuplicateSkuException;
import com.inventory.app.exception.ResourceNotFoundException;
import com.inventory.app.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ProductServiceImpl using Mockito.
 * No Spring context is loaded — very fast.
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductDTO validDTO;
    private Product savedProduct;

    @BeforeEach
    void setUp() {
        validDTO = new ProductDTO();
        validDTO.setName("Test Mouse");
        validDTO.setSku("MOUSE-TEST-001");
        validDTO.setCategory("Electronics");
        validDTO.setPrice(new BigDecimal("29.99"));
        validDTO.setQuantity(50);
        validDTO.setMinStockThreshold(10);

        savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName("Test Mouse");
        savedProduct.setSku("MOUSE-TEST-001");
        savedProduct.setPrice(new BigDecimal("29.99"));
        savedProduct.setQuantity(50);
        savedProduct.setMinStockThreshold(10);
    }

    // ── Create Product Tests ──────────────────────────────────────────────────

    @Test
    @DisplayName("Create product successfully when SKU is unique")
    void createProduct_success() {
        when(productRepository.existsBySku("MOUSE-TEST-001")).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        Product result = productService.createProduct(validDTO);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Mouse");
        assertThat(result.getSku()).isEqualTo("MOUSE-TEST-001");
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Create product fails with DuplicateSkuException when SKU already exists")
    void createProduct_duplicateSku_throwsException() {
        when(productRepository.existsBySku("MOUSE-TEST-001")).thenReturn(true);

        assertThatThrownBy(() -> productService.createProduct(validDTO))
                .isInstanceOf(DuplicateSkuException.class)
                .hasMessageContaining("MOUSE-TEST-001");

        verify(productRepository, never()).save(any());
    }

    // ── Update Product Tests ──────────────────────────────────────────────────

    @Test
    @DisplayName("Update product successfully")
    void updateProduct_success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(savedProduct));
        when(productRepository.existsBySkuAndIdNot("MOUSE-TEST-001", 1L)).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        validDTO.setName("Updated Mouse");
        Product result = productService.updateProduct(1L, validDTO);

        assertThat(result).isNotNull();
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("Update fails when new SKU conflicts with another product")
    void updateProduct_skuConflict_throwsException() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(savedProduct));
        when(productRepository.existsBySkuAndIdNot("MOUSE-TEST-001", 1L)).thenReturn(true);

        assertThatThrownBy(() -> productService.updateProduct(1L, validDTO))
                .isInstanceOf(DuplicateSkuException.class);
    }

    // ── Delete Product Tests ──────────────────────────────────────────────────

    @Test
    @DisplayName("Delete product successfully")
    void deleteProduct_success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(savedProduct));
        doNothing().when(productRepository).delete(savedProduct);

        productService.deleteProduct(1L);

        verify(productRepository).delete(savedProduct);
    }

    @Test
    @DisplayName("Delete throws ResourceNotFoundException for unknown ID")
    void deleteProduct_notFound_throwsException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.deleteProduct(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // ── Low Stock Tests ───────────────────────────────────────────────────────

    @Test
    @DisplayName("Product with quantity below threshold reports low stock")
    void product_lowStock_isDetectedCorrectly() {
        savedProduct.setQuantity(5);
        savedProduct.setMinStockThreshold(10);

        assertThat(savedProduct.isLowStock()).isTrue();
        assertThat(savedProduct.isOutOfStock()).isFalse();
        assertThat(savedProduct.getStockStatus()).isEqualTo("LOW_STOCK");
    }

    @Test
    @DisplayName("Product with zero quantity reports out of stock")
    void product_outOfStock_isDetectedCorrectly() {
        savedProduct.setQuantity(0);
        savedProduct.setMinStockThreshold(10);

        assertThat(savedProduct.isOutOfStock()).isTrue();
        assertThat(savedProduct.isLowStock()).isFalse();
        assertThat(savedProduct.getStockStatus()).isEqualTo("OUT_OF_STOCK");
    }

    @Test
    @DisplayName("Product with quantity above threshold is in stock")
    void product_inStock_isDetectedCorrectly() {
        savedProduct.setQuantity(50);
        savedProduct.setMinStockThreshold(10);

        assertThat(savedProduct.isOutOfStock()).isFalse();
        assertThat(savedProduct.isLowStock()).isFalse();
        assertThat(savedProduct.getStockStatus()).isEqualTo("IN_STOCK");
    }
}
