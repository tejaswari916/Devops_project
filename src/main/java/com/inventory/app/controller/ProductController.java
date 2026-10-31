package com.inventory.app.controller;

import com.inventory.app.dto.ProductDTO;
import com.inventory.app.entity.Product;
import com.inventory.app.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String list(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "") String category,
            @RequestParam(defaultValue = "") String stockStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String dir,
            Model model) {

        Sort.Direction direction = dir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        Page<Product> productPage = productService.searchProducts(search, category, stockStatus, pageable);

        model.addAttribute("products", productPage);
        model.addAttribute("categories", productService.findAllCategories());
        model.addAttribute("search", search);
        model.addAttribute("category", category);
        model.addAttribute("stockStatus", stockStatus);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        model.addAttribute("activePage", "products");
        return "products/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("categories", productService.findAllCategories());
        model.addAttribute("activePage", "products");
        model.addAttribute("isEdit", false);
        return "products/form";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("product") ProductDTO dto,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("categories", productService.findAllCategories());
            model.addAttribute("activePage", "products");
            model.addAttribute("isEdit", false);
            return "products/form";
        }
        Product created = productService.createProduct(dto);
        redirectAttributes.addFlashAttribute("successMessage",
                "Product '" + created.getName() + "' created successfully!");
        return "redirect:/products";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        model.addAttribute("activePage", "products");
        return "products/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", productService.toDTO(product));
        model.addAttribute("categories", productService.findAllCategories());
        model.addAttribute("activePage", "products");
        model.addAttribute("isEdit", true);
        return "products/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("product") ProductDTO dto,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("categories", productService.findAllCategories());
            model.addAttribute("activePage", "products");
            model.addAttribute("isEdit", true);
            return "products/form";
        }
        Product updated = productService.updateProduct(id, dto);
        redirectAttributes.addFlashAttribute("successMessage",
                "Product '" + updated.getName() + "' updated successfully!");
        return "redirect:/products";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Product product = productService.findById(id);
        String name = product.getName();
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("successMessage",
                "Product '" + name + "' deleted successfully.");
        return "redirect:/products";
    }
}
