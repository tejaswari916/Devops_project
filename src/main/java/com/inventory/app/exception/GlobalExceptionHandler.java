package com.inventory.app.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Handles application-wide exceptions and maps them to user-friendly error pages.
 * Never exposes stack traces or sensitive information to the end user.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(ResourceNotFoundException ex, Model model) {
        log.warn("Resource not found: {}", ex.getMessage());
        model.addAttribute("errorTitle", "Not Found");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("statusCode", 404);
        return "error/error";
    }

    @ExceptionHandler(InsufficientStockException.class)
    public String handleInsufficientStock(InsufficientStockException ex,
                                          RedirectAttributes redirectAttributes) {
        log.warn("Insufficient stock: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/stock";
    }

    @ExceptionHandler(DuplicateSkuException.class)
    public String handleDuplicateSku(DuplicateSkuException ex, Model model) {
        log.warn("Duplicate SKU: {}", ex.getMessage());
        model.addAttribute("errorTitle", "Duplicate SKU");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("statusCode", 409);
        return "error/error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneral(Exception ex, Model model) {
        log.error("Unexpected error", ex);
        model.addAttribute("errorTitle", "Internal Server Error");
        model.addAttribute("errorMessage", "Something went wrong. Please try again or contact the administrator.");
        model.addAttribute("statusCode", 500);
        return "error/error";
    }
}
