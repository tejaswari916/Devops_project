package com.inventory.app.service;

import com.inventory.app.dto.ProductDTO;
import com.inventory.app.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Business logic contract for product management operations.
 */
public interface ProductService {

    Product createProduct(ProductDTO dto);

    Product updateProduct(Long id, ProductDTO dto);

    void deleteProduct(Long id);

    Product findById(Long id);

    Page<Product> searchProducts(String search, String category, String stockStatus, Pageable pageable);

    List<String> findAllCategories();

    List<Product> findLowStockProducts();

    List<Product> findOutOfStockProducts();

    long countTotal();

    long countLowStock();

    long countOutOfStock();

    long sumTotalQuantity();

    List<Product> findRecentProducts();

    ProductDTO toDTO(Product product);
}
