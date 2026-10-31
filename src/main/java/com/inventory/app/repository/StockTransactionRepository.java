package com.inventory.app.repository;

import com.inventory.app.entity.StockTransaction;
import com.inventory.app.entity.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockTransactionRepository extends JpaRepository<StockTransaction, Long> {

    Page<StockTransaction> findByProductId(Long productId, Pageable pageable);

    List<StockTransaction> findTop10ByOrderByTransactionDateDesc();

    @Query("SELECT t FROM StockTransaction t WHERE t.product.id = :productId ORDER BY t.transactionDate DESC")
    List<StockTransaction> findRecentByProductId(@Param("productId") Long productId, Pageable pageable);

    @Query("SELECT t FROM StockTransaction t JOIN FETCH t.product ORDER BY t.transactionDate DESC")
    Page<StockTransaction> findAllWithProduct(Pageable pageable);

    long countByTransactionType(TransactionType type);
}
