package com.example.minimall.controller;

import com.example.minimall.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {
    private final UserService userService;
    public AdminUserController(UserService userService) { this.userService = userService; }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userService.listAll());
        return "admin/user_list";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.getById(id));
        return "admin/user_form";
    }

    @PostMapping("/updateBalance")
    public String updateBalance(@RequestParam Long id,
                                @RequestParam BigDecimal balance) {
        userService.updateBalance(id, balance);
        return "redirect:/admin/users";
    }
}
