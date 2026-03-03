package com.example.minimall.controller;

import com.example.minimall.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CartController {
    private final CartService cartService;
    public CartController(CartService cartService) { this.cartService = cartService; }

    @GetMapping("/cart")
    public String cart(Model model, HttpSession session) {
        var user = (com.example.minimall.entity.User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        model.addAttribute("items", cartService.listCartItems(user.getId()));
        model.addAttribute("total", cartService.calcTotal(user.getId()));
        return "cart";
    }

    @PostMapping("/cart/update")
    public String update(@RequestParam Long itemId,
                         @RequestParam Integer quantity) {
        cartService.updateQuantity(itemId, quantity);
        return "redirect:/cart";
    }

    @GetMapping("/cart/delete/{id}")
    public String delete(@PathVariable Long id) {
        cartService.removeItem(id);
        return "redirect:/cart";
    }
}
