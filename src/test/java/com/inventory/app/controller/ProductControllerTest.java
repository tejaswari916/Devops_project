package com.inventory.app.controller;

import com.inventory.app.dto.ProductDTO;
import com.inventory.app.entity.Product;
import com.inventory.app.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller-layer tests using Spring's MockMvc.
 * Tests HTTP routing, validation, and redirects without a real database.
 */
@WebMvcTest(ProductController.class)
@ActiveProfiles("test")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    // ── List Products ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /products returns product list page for authenticated user")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getProducts_authenticated_returnsListPage() throws Exception {
        Page<Product> emptyPage = new PageImpl<>(List.of());
        when(productService.searchProducts(any(), any(), any(), any())).thenReturn(emptyPage);
        when(productService.findAllCategories()).thenReturn(List.of());

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/list"));
    }

    @Test
    @DisplayName("GET /products requires authentication")
    void getProducts_notAuthenticated_requiresAuthentication() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isUnauthorized());
    }

    // ── New Product Form ──────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /products/new returns form page")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getNewProductForm_returnsFormPage() throws Exception {
        when(productService.findAllCategories()).thenReturn(List.of("Electronics", "Furniture"));

        mockMvc.perform(get("/products/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/form"))
                .andExpect(model().attributeExists("product"));
    }

    // ── Create Product Validation ─────────────────────────────────────────────

    @Test
    @DisplayName("POST /products/new with blank name fails validation and returns form")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void createProduct_blankName_returnsFormWithErrors() throws Exception {
        when(productService.findAllCategories()).thenReturn(List.of());

        mockMvc.perform(post("/products/new")
                        .with(csrf())
                        .param("name", "")          // blank - invalid
                        .param("sku", "TEST-001")
                        .param("price", "10.00")
                        .param("quantity", "5"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/form"))
                .andExpect(model().hasErrors());
    }

    @Test
    @DisplayName("POST /products/new with negative price fails validation")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void createProduct_negativePrice_returnsFormWithErrors() throws Exception {
        when(productService.findAllCategories()).thenReturn(List.of());

        mockMvc.perform(post("/products/new")
                        .with(csrf())
                        .param("name", "Test Product")
                        .param("sku", "TEST-001")
                        .param("price", "-5.00")    // negative - invalid
                        .param("quantity", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/form"))
                .andExpect(model().hasErrors());
    }

    @Test
    @DisplayName("POST /products/new with negative quantity fails validation")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void createProduct_negativeQuantity_returnsFormWithErrors() throws Exception {
        when(productService.findAllCategories()).thenReturn(List.of());

        mockMvc.perform(post("/products/new")
                        .with(csrf())
                        .param("name", "Test Product")
                        .param("sku", "TEST-001")
                        .param("price", "10.00")
                        .param("quantity", "-1"))   // negative - invalid
                .andExpect(status().isOk())
                .andExpect(view().name("products/form"))
                .andExpect(model().hasErrors());
    }

    @Test
    @DisplayName("POST /products/new with valid data redirects to product list")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void createProduct_validData_redirectsToList() throws Exception {
        Product created = new Product();
        created.setId(1L);
        created.setName("Test Product");
        created.setSku("TEST-001");
        created.setPrice(new BigDecimal("10.00"));
        created.setQuantity(5);
        created.setMinStockThreshold(3);

        when(productService.createProduct(any(ProductDTO.class))).thenReturn(created);

        mockMvc.perform(post("/products/new")
                        .with(csrf())
                        .param("name", "Test Product")
                        .param("sku", "TEST-001")
                        .param("price", "10.00")
                        .param("quantity", "5")
                        .param("minStockThreshold", "3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));
    }
}
