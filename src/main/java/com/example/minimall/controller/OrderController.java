package com.example.minimall.controller;

import com.example.minimall.service.CartService;
import com.example.minimall.service.CouponService;
import com.example.minimall.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class OrderController {
    private final OrderService orderService;
    private final CartService cartService;
    private final CouponService couponService;

    public OrderController(OrderService orderService, CartService cartService, CouponService couponService) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.couponService = couponService;
    }

    @GetMapping("/checkout")
    public String checkout(Model model, HttpSession session) {
        var user = (com.example.minimall.entity.User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        model.addAttribute("items", cartService.listCartItems(user.getId()));
        model.addAttribute("total", cartService.calcTotal(user.getId()));
        model.addAttribute("coupons", couponService.availableCoupons(user.getId()));
        return "checkout";
    }

    @PostMapping("/orders/submit")
    public String submit(@RequestParam(required = false) Long couponId,
                         HttpSession session) {
        var user = (com.example.minimall.entity.User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        var order = orderService.createOrder(user.getId(), couponId);
        return "redirect:/orders/" + order.getId();
    }

    @GetMapping("/orders")
    public String orders(Model model, HttpSession session) {
        var user = (com.example.minimall.entity.User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        model.addAttribute("orders", orderService.listOrders(user.getId()));
        return "order_list";
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable Long id, Model model, HttpSession session) {
        var user = (com.example.minimall.entity.User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        model.addAttribute("order", orderService.getOrder(id));
        model.addAttribute("items", orderService.listItems(id));
        return "order_detail";
    }
}
