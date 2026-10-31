package com.inventory.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a product creation or update attempts to use an already-existing SKU.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateSkuException extends RuntimeException {

    public DuplicateSkuException(String sku) {
        super("A product with SKU '" + sku + "' already exists.");
    }
}
