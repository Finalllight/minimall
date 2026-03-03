package com.example.minimall.controller;

import com.example.minimall.entity.User;
import com.example.minimall.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    private final UserService userService;
    public AuthController(UserService userService) { this.userService = userService; }

    @GetMapping("/register")
    public String registerPage() { return "register"; }

    @PostMapping("/register")
    public String doRegister(@RequestParam String username,
                             @RequestParam String password,
                             Model model) {
        try {
            userService.register(username, password);
            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String loginPage() { return "login"; }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpSession session,
                          Model model) {
        User user = userService.login(username, password);
        if (user == null) {
            model.addAttribute("error", "用户名或密码错误");
            return "login";
        }
        session.setAttribute("user", user);
        return "redirect:/products";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
