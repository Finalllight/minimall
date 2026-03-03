package com.example.minimall.controller;

import com.example.minimall.entity.Product;
import com.example.minimall.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {
    private final ProductService productService;

    public AdminProductController(ProductService productService) { this.productService = productService; }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.listAll(null));
        return "admin/product_list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin/product_form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getById(id));
        return "admin/product_form";
    }

    @PostMapping("/save")
    public String save(Product product) {
        productService.save(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        productService.deleteById(id);
        return "redirect:/admin/products";
    }
}
