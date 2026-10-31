package com.inventory.app.repository;

import com.inventory.app.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySku(String sku);

    boolean existsBySku(String sku);

    boolean existsBySkuAndIdNot(String sku, Long id);

    @Query("SELECT p FROM Product p WHERE " +
           "(:search IS NULL OR :search = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "AND (:category IS NULL OR :category = '' OR p.category = :category) " +
           "AND (:stockStatus IS NULL OR :stockStatus = '' OR " +
           "     (:stockStatus = 'OUT_OF_STOCK' AND p.quantity = 0) OR " +
           "     (:stockStatus = 'LOW_STOCK' AND p.quantity > 0 AND p.quantity <= p.minStockThreshold) OR " +
           "     (:stockStatus = 'IN_STOCK' AND p.quantity > p.minStockThreshold))")
    Page<Product> searchProducts(
            @Param("search") String search,
            @Param("category") String category,
            @Param("stockStatus") String stockStatus,
            Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.quantity > 0 AND p.quantity <= p.minStockThreshold")
    List<Product> findLowStockProducts();

    @Query("SELECT p FROM Product p WHERE p.quantity = 0")
    List<Product> findOutOfStockProducts();

    @Query("SELECT COUNT(p) FROM Product p WHERE p.quantity > 0 AND p.quantity <= p.minStockThreshold")
    long countLowStockProducts();

    @Query("SELECT COUNT(p) FROM Product p WHERE p.quantity = 0")
    long countOutOfStockProducts();

    @Query("SELECT COALESCE(SUM(p.quantity), 0) FROM Product p")
    long sumTotalQuantity();

    @Query("SELECT DISTINCT p.category FROM Product p WHERE p.category IS NOT NULL AND p.category <> '' ORDER BY p.category")
    List<String> findDistinctCategories();

    List<Product> findTop5ByOrderByCreatedAtDesc();
}
