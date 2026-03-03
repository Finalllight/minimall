package com.example.minimall.controller;

import com.example.minimall.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {
    private final OrderService orderService;
    public AdminOrderController(OrderService orderService) { this.orderService = orderService; }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("orders", orderService.listAll());
        return "admin/order_list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.getOrder(id));
        model.addAttribute("items", orderService.listItems(id));
        return "admin/order_detail";
    }
}
