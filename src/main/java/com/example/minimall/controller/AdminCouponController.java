package com.example.minimall.controller;

import com.example.minimall.entity.Coupon;
import com.example.minimall.entity.CouponType;
import com.example.minimall.service.CouponService;
import com.example.minimall.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/coupons")
public class AdminCouponController {
    private final CouponService couponService;
    private final UserService userService;

    public AdminCouponController(CouponService couponService, UserService userService) {
        this.couponService = couponService;
        this.userService = userService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("coupons", couponService.listAll());
        return "admin/coupon_list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("coupon", new Coupon());
        model.addAttribute("users", userService.listAll());
        model.addAttribute("types", CouponType.values());
        return "admin/coupon_form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("coupon", couponService.getById(id));
        model.addAttribute("users", userService.listAll());
        model.addAttribute("types", CouponType.values());
        return "admin/coupon_form";
    }

    @PostMapping("/save")
    public String save(Coupon coupon) {
        couponService.save(coupon);
        return "redirect:/admin/coupons";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        couponService.deleteById(id);
        return "redirect:/admin/coupons";
    }
}
