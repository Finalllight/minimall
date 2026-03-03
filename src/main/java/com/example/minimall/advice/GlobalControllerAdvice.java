package com.example.minimall.advice;

import com.example.minimall.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ModelAttribute("currentUser")
    public User currentUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }
}
