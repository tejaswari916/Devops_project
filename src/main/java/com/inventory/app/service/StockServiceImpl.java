package com.inventory.app.service;

import com.inventory.app.dto.StockTransactionDTO;
import com.inventory.app.entity.Product;
import com.inventory.app.entity.StockTransaction;
import com.inventory.app.entity.TransactionType;
import com.inventory.app.exception.InsufficientStockException;
import com.inventory.app.repository.ProductRepository;
import com.inventory.app.repository.StockTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StockServiceImpl implements StockService {

    private static final Logger log = LoggerFactory.getLogger(StockServiceImpl.class);

    private final StockTransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    public StockServiceImpl(StockTransactionRepository transactionRepository,
                             ProductRepository productRepository,
                             ProductService productService) {
        this.transactionRepository = transactionRepository;
        this.productRepository = productRepository;
        this.productService = productService;
    }

    @Override
    public StockTransaction stockIn(Long productId, StockTransactionDTO dto) {
        Product product = productService.findById(productId);
        product.setQuantity(product.getQuantity() + dto.getQuantity());
        productRepository.save(product);

        StockTransaction tx = new StockTransaction(
                product, TransactionType.STOCK_IN, dto.getQuantity(), dto.getRemarks());
        log.info("Stock IN: {} units for product '{}' (ID: {})",
                dto.getQuantity(), product.getName(), productId);
        return transactionRepository.save(tx);
    }

    @Override
    public StockTransaction stockOut(Long productId, StockTransactionDTO dto) {
        Product product = productService.findById(productId);
        if (product.getQuantity() < dto.getQuantity()) {
            throw new InsufficientStockException(
                    product.getName(), dto.getQuantity(), product.getQuantity());
        }
        product.setQuantity(product.getQuantity() - dto.getQuantity());
        productRepository.save(product);

        StockTransaction tx = new StockTransaction(
                product, TransactionType.STOCK_OUT, dto.getQuantity(), dto.getRemarks());
        log.info("Stock OUT: {} units for product '{}' (ID: {})",
                dto.getQuantity(), product.getName(), productId);
        return transactionRepository.save(tx);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StockTransaction> findByProductId(Long productId, Pageable pageable) {
        return transactionRepository.findByProductId(productId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StockTransaction> findAll(Pageable pageable) {
        return transactionRepository.findAllWithProduct(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockTransaction> findRecentTransactions() {
        return transactionRepository.findTop10ByOrderByTransactionDateDesc();
    }
}
