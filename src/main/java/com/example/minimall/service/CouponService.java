package com.example.minimall.service;

import com.example.minimall.entity.Coupon;
import com.example.minimall.repository.CouponRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponService {
    private final CouponRepository couponRepo;
    public CouponService(CouponRepository couponRepo) { this.couponRepo = couponRepo; }

    public List<Coupon> availableCoupons(Long userId) {
        return couponRepo.findByUserIdAndQuantityGreaterThan(userId, 0);
    }

    public Coupon getById(Long id) {
        return couponRepo.findById(id).orElse(null);
    }

    public void save(Coupon c) { couponRepo.save(c); }
}
