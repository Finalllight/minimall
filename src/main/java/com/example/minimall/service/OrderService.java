package com.example.minimall.service;

import com.example.minimall.entity.*;
import com.example.minimall.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final CartItemRepository cartRepo;
    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    private final CouponRepository couponRepo;

    public OrderService(CartItemRepository cartRepo, ProductRepository productRepo,
                        OrderRepository orderRepo, OrderItemRepository orderItemRepo,
                        CouponRepository couponRepo) {
        this.cartRepo = cartRepo;
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
        this.couponRepo = couponRepo;
    }

    @Transactional
    public Order createOrder(Long userId, Long couponId) {
        List<CartItem> cartItems = cartRepo.findByUserId(userId);
        if (cartItems.isEmpty()) throw new RuntimeException("购物车为空");

        // 1. 计算总价
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            Product p = productRepo.findById(item.getProductId()).orElseThrow();
            total = total.add(p.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        // 2. 计算优惠后价格
        BigDecimal pay = total;
        if (couponId != null) {
            Coupon coupon = couponRepo.findById(couponId).orElse(null);
            if (coupon != null && coupon.getQuantity() > 0
                    && total.compareTo(coupon.getThreshold()) >= 0) {
                if (coupon.getType() == CouponType.FULL_REDUCTION) {
                    pay = total.subtract(coupon.getAmount());
                } else if (coupon.getType() == CouponType.DISCOUNT) {
                    pay = total.multiply(coupon.getAmount());
                }
                if (pay.compareTo(BigDecimal.ZERO) < 0) pay = BigDecimal.ZERO;
                coupon.setQuantity(coupon.getQuantity() - 1);
                couponRepo.save(coupon);
            }
        }

        // 3. 创建订单
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(total);
        order.setPayAmount(pay);
        order.setCouponId(couponId);
        order.setCreatedTime(LocalDateTime.now());
        orderRepo.save(order);

        // 4. 创建订单明细
        for (CartItem item : cartItems) {
            Product p = productRepo.findById(item.getProductId()).orElseThrow();
            OrderItem oi = new OrderItem();
            oi.setOrderId(order.getId());
            oi.setProductId(p.getId());
            oi.setProductName(p.getName());
            oi.setPrice(p.getPrice());
            oi.setQuantity(item.getQuantity());
            orderItemRepo.save(oi);
        }

        // 5. 清空购物车
        cartRepo.deleteByUserId(userId);

        return order;
    }

    public List<Order> listOrders(Long userId) {
        return orderRepo.findByUserId(userId);
    }

    public Order getOrder(Long id) {
        return orderRepo.findById(id).orElse(null);
    }

    public List<OrderItem> listItems(Long orderId) {
        return orderItemRepo.findByOrderId(orderId);
    }
    public List<Order> listAll() { return orderRepo.findAll(); }

}
