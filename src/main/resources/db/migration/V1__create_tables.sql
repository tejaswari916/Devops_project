-- ============================================================
-- V1: Create core tables
-- Compatible with H2 (dev/test) and MySQL (prod)
-- ============================================================

CREATE TABLE IF NOT EXISTS products (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    name                VARCHAR(255)    NOT NULL,
    sku                 VARCHAR(100)    NOT NULL,
    category            VARCHAR(100),
    description         TEXT,
    price               DECIMAL(12, 2)  NOT NULL,
    quantity            INTEGER         NOT NULL DEFAULT 0,
    min_stock_threshold INTEGER         NOT NULL DEFAULT 10,
    created_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_product_sku UNIQUE (sku)
);

CREATE TABLE IF NOT EXISTS stock_transactions (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id       BIGINT          NOT NULL,
    transaction_type VARCHAR(20)     NOT NULL,
    quantity         INTEGER         NOT NULL,
    remarks          VARCHAR(500),
    transaction_date TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by       VARCHAR(100)    DEFAULT 'admin',
    CONSTRAINT fk_transaction_product
        FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_product_sku ON products(sku);
CREATE INDEX IF NOT EXISTS idx_product_category ON products(category);
CREATE INDEX IF NOT EXISTS idx_transaction_product ON stock_transactions(product_id);
CREATE INDEX IF NOT EXISTS idx_transaction_date ON stock_transactions(transaction_date);
