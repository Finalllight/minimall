package com.example.minimall.repository;

import com.example.minimall.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    List<Coupon> findByUserIdAndQuantityGreaterThan(Long userId, Integer quantity);
}