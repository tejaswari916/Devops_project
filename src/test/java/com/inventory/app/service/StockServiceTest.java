package com.inventory.app.service;

import com.inventory.app.dto.StockTransactionDTO;
import com.inventory.app.entity.Product;
import com.inventory.app.entity.StockTransaction;
import com.inventory.app.entity.TransactionType;
import com.inventory.app.exception.InsufficientStockException;
import com.inventory.app.repository.ProductRepository;
import com.inventory.app.repository.StockTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for StockServiceImpl using Mockito.
 */
@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockTransactionRepository transactionRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private StockServiceImpl stockService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setSku("TEST-001");
        product.setPrice(new BigDecimal("10.00"));
        product.setQuantity(100);
        product.setMinStockThreshold(10);
    }

    // ── Stock In Tests ────────────────────────────────────────────────────────

    @Test
    @DisplayName("Stock In correctly increases product quantity")
    void stockIn_increasesQuantityCorrectly() {
        StockTransactionDTO dto = new StockTransactionDTO();
        dto.setQuantity(50);
        dto.setRemarks("Test delivery");

        StockTransaction savedTx = new StockTransaction(product, TransactionType.STOCK_IN, 50, "Test delivery");

        when(productService.findById(1L)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(transactionRepository.save(any(StockTransaction.class))).thenReturn(savedTx);

        StockTransaction result = stockService.stockIn(1L, dto);

        assertThat(result.getTransactionType()).isEqualTo(TransactionType.STOCK_IN);
        assertThat(result.getQuantity()).isEqualTo(50);
        assertThat(product.getQuantity()).isEqualTo(150); // 100 + 50
        verify(productRepository).save(product);
    }

    // ── Stock Out Tests ───────────────────────────────────────────────────────

    @Test
    @DisplayName("Stock Out correctly decreases product quantity")
    void stockOut_decreasesQuantityCorrectly() {
        StockTransactionDTO dto = new StockTransactionDTO();
        dto.setQuantity(30);
        dto.setRemarks("Sales order");

        StockTransaction savedTx = new StockTransaction(product, TransactionType.STOCK_OUT, 30, "Sales order");

        when(productService.findById(1L)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(transactionRepository.save(any(StockTransaction.class))).thenReturn(savedTx);

        StockTransaction result = stockService.stockOut(1L, dto);

        assertThat(result.getTransactionType()).isEqualTo(TransactionType.STOCK_OUT);
        assertThat(product.getQuantity()).isEqualTo(70); // 100 - 30
        verify(productRepository).save(product);
    }

    @Test
    @DisplayName("Stock Out throws InsufficientStockException when quantity exceeds available")
    void stockOut_insufficientStock_throwsException() {
        product.setQuantity(10);

        StockTransactionDTO dto = new StockTransactionDTO();
        dto.setQuantity(50); // more than available (10)
        dto.setRemarks("Over-order attempt");

        when(productService.findById(1L)).thenReturn(product);

        assertThatThrownBy(() -> stockService.stockOut(1L, dto))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("Insufficient stock")
                .hasMessageContaining("Test Product");

        verify(productRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Stock Out with exact available quantity succeeds (boundary case)")
    void stockOut_exactQuantity_succeeds() {
        product.setQuantity(50);

        StockTransactionDTO dto = new StockTransactionDTO();
        dto.setQuantity(50);

        StockTransaction savedTx = new StockTransaction(product, TransactionType.STOCK_OUT, 50, null);

        when(productService.findById(1L)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(transactionRepository.save(any(StockTransaction.class))).thenReturn(savedTx);

        StockTransaction result = stockService.stockOut(1L, dto);

        assertThat(result).isNotNull();
        assertThat(product.getQuantity()).isEqualTo(0);
    }
}
