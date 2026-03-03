package com.example.minimall.controller;

import com.example.minimall.entity.Product;
import com.example.minimall.service.CartService;
import com.example.minimall.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProductController {
    private final ProductService productService;
    private final CartService cartService;

    public ProductController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

    @GetMapping({"/", "/products"})
    public String list(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("products", productService.listAll(keyword));
        model.addAttribute("keyword", keyword);
        return "product_list";
    }

    @GetMapping("/products/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Product p = productService.getById(id);
        model.addAttribute("product", p);
        return "product_detail";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam Integer quantity,
                            HttpSession session) {
        var user = (com.example.minimall.entity.User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        cartService.addToCart(user.getId(), productId, quantity);
        return "redirect:/cart";
    }
}
