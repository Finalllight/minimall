package com.example.minimall.controller;

import com.example.minimall.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
