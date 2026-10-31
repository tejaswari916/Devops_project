package com.inventory.app.service;

import com.inventory.app.dto.ProductDTO;
import com.inventory.app.entity.Product;
import com.inventory.app.exception.DuplicateSkuException;
import com.inventory.app.exception.ResourceNotFoundException;
import com.inventory.app.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product createProduct(ProductDTO dto) {
        String sku = dto.getSku().toUpperCase().trim();
        if (productRepository.existsBySku(sku)) {
            throw new DuplicateSkuException(sku);
        }
        Product product = new Product();
        mapDtoToEntity(dto, product);
        product.setSku(sku);
        log.info("Creating product with SKU: {}", sku);
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, ProductDTO dto) {
        Product existing = findById(id);
        String newSku = dto.getSku().toUpperCase().trim();
        if (productRepository.existsBySkuAndIdNot(newSku, id)) {
            throw new DuplicateSkuException(newSku);
        }
        mapDtoToEntity(dto, existing);
        existing.setSku(newSku);
        log.info("Updating product ID: {}", id);
        return productRepository.save(existing);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = findById(id);
        log.info("Deleting product ID: {} (SKU: {})", id, product.getSku());
        productRepository.delete(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> searchProducts(String search, String category, String stockStatus, Pageable pageable) {
        return productRepository.searchProducts(search, category, stockStatus, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> findAllCategories() {
        return productRepository.findDistinctCategories();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findLowStockProducts() {
        return productRepository.findLowStockProducts();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findOutOfStockProducts() {
        return productRepository.findOutOfStockProducts();
    }

    @Override
    @Transactional(readOnly = true)
    public long countTotal() {
        return productRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countLowStock() {
        return productRepository.countLowStockProducts();
    }

    @Override
    @Transactional(readOnly = true)
    public long countOutOfStock() {
        return productRepository.countOutOfStockProducts();
    }

    @Override
    @Transactional(readOnly = true)
    public long sumTotalQuantity() {
        return productRepository.sumTotalQuantity();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findRecentProducts() {
        return productRepository.findTop5ByOrderByCreatedAtDesc();
    }

    @Override
    public ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setSku(product.getSku());
        dto.setCategory(product.getCategory());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        dto.setMinStockThreshold(product.getMinStockThreshold());
        return dto;
    }

    private void mapDtoToEntity(ProductDTO dto, Product product) {
        product.setName(dto.getName().trim());
        product.setCategory(dto.getCategory());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.setMinStockThreshold(
                dto.getMinStockThreshold() != null ? dto.getMinStockThreshold() : 10);
    }
}
