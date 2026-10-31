-- ============================================================
-- V2: Sample data for demonstration
-- Only runs on first migration. Flyway tracks this.
-- ============================================================

-- NOTE: Dev profile uses DataInitializer.java instead.
-- This migration is here as a baseline for production MySQL.
-- The DataInitializer bean only runs with profile 'dev'.
-- In prod, Flyway runs this SQL to populate initial data.

INSERT IGNORE INTO products (name, sku, category, description, price, quantity, min_stock_threshold)
VALUES
  ('Wireless Mouse',      'MOUSE-001', 'Electronics', 'Ergonomic wireless mouse with USB receiver',            29.99,  45, 10),
  ('Mechanical Keyboard', 'KB-002',    'Electronics', 'Full-size mechanical keyboard with RGB',                89.99,   8, 15),
  ('USB-C Hub',           'HUB-003',   'Electronics', '7-in-1 USB-C hub with HDMI and SD reader',              49.99,   0,  5),
  ('Office Chair',        'CHAIR-004', 'Furniture',   'Adjustable ergonomic office chair',                    299.99,  12,  5),
  ('Standing Desk',       'DESK-005',  'Furniture',   'Height-adjustable standing desk 120x60cm',             599.99,   3,  5),
  ('A4 Paper Ream',       'PAPER-006', 'Stationery',  '500 sheets A4 80gsm printing paper',                    8.99, 200, 50),
  ('Ballpoint Pens Box',  'PEN-007',   'Stationery',  'Box of 50 blue ballpoint pens',                        12.50,   4, 10),
  ('Laptop Stand',        'STAND-008', 'Electronics', 'Adjustable aluminium laptop stand',                    35.00,  22,  8);
