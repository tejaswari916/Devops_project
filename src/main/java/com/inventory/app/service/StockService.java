package com.inventory.app.service;

import com.inventory.app.dto.StockTransactionDTO;
import com.inventory.app.entity.StockTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Business logic contract for stock movement operations.
 */
public interface StockService {

    StockTransaction stockIn(Long productId, StockTransactionDTO dto);

    StockTransaction stockOut(Long productId, StockTransactionDTO dto);

    Page<StockTransaction> findByProductId(Long productId, Pageable pageable);

    Page<StockTransaction> findAll(Pageable pageable);

    List<StockTransaction> findRecentTransactions();
}
