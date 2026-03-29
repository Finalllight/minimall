package com.example.minimall.controller;

import com.example.minimall.entity.Order;
import com.example.minimall.entity.User;
import com.example.minimall.service.CartService;
import com.example.minimall.service.CouponService;
import com.example.minimall.service.OrderService;
import com.example.minimall.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OrderController {
    private final OrderService orderService;
    private final CartService cartService;
    private final CouponService couponService;
    private final UserService userService;
    public OrderController(OrderService orderService, CartService cartService, CouponService couponService, UserService userService) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.couponService = couponService;
        this.userService = userService;
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
                         HttpSession session,
                         Model model) {
        var user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        try {
            Order order = orderService.createOrder(user.getId(), couponId);
            // 更新 session 中的用户余额

            session.setAttribute("user", userService.getById(user.getId()));
            return "redirect:/orders/" + order.getId();
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("items", cartService.listCartItems(user.getId()));
            model.addAttribute("total", cartService.calcTotal(user.getId()));
            model.addAttribute("coupons", couponService.availableCoupons(user.getId()));
            return "checkout";
        }
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
