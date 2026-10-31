package com.inventory.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a stock-out operation requests more units than are available.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientStockException extends RuntimeException {

    private final int requested;
    private final int available;

    public InsufficientStockException(String productName, int requested, int available) {
        super(String.format(
                "Insufficient stock for '%s'. Requested: %d, Available: %d.",
                productName, requested, available));
        this.requested = requested;
        this.available = available;
    }

    public int getRequested() { return requested; }
    public int getAvailable() { return available; }
}
