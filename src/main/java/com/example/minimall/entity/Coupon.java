package com.example.minimall.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private CouponType type;

    // 满多少可用
    @Column(precision = 10, scale = 2)
    private BigDecimal threshold;

    // 满减金额 或 折扣率(如0.9)
    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    private Integer quantity;

    public Coupon() {
    }

    public Coupon(Long id, Long userId, CouponType type,
                  BigDecimal threshold, BigDecimal amount, Integer quantity) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.threshold = threshold;
        this.amount = amount;
        this.quantity = quantity;
    }

    @Transient
    public String getDisplay() {
        if (type == CouponType.FULL_REDUCTION) {
            return "满" + threshold + "减" + amount;
        } else {
            return "满" + threshold + "打" + amount + "折";
        }
    }

    // ===== getter / setter =====
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public CouponType getType() {
        return type;
    }

    public void setType(CouponType type) {
        this.type = type;
    }

    public BigDecimal getThreshold() {
        return threshold;
    }

    public void setThreshold(BigDecimal threshold) {
        this.threshold = threshold;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
