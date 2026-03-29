package com.example.minimall.service;

import com.example.minimall.entity.*;
import com.example.minimall.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final CartItemRepository cartRepo;
    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    private final CouponRepository couponRepo;
    private final UserRepository userRepo;

    public OrderService(CartItemRepository cartRepo,
                        ProductRepository productRepo,
                        OrderRepository orderRepo,
                        OrderItemRepository orderItemRepo,
                        CouponRepository couponRepo,
                        UserRepository userRepo) {
        this.cartRepo = cartRepo;
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
        this.couponRepo = couponRepo;
        this.userRepo = userRepo;
    }

    /**
     * 创建订单（核心方法）
     * 流程：
     *   1) 查询购物车
     *   2) 计算商品总价
     *   3) 若选了优惠券，计算优惠后价格，并扣减优惠券数量
     *   4) 校验用户余额，扣减余额
     *   5) 生成订单 + 订单明细
     *   6) 清空购物车
     *
     * @param userId   当前登录用户ID
     * @param couponId 选择的优惠券ID（可为null，表示不使用优惠券）
     * @return 生成的订单
     */
    @Transactional
    public Order createOrder(Long userId, Long couponId) {

        // ========== 1. 查询购物车，校验非空 ==========
        List<CartItem> cartItems = cartRepo.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("购物车为空，无法下单");
        }

        // ========== 2. 计算商品总价 ==========
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            Product p = productRepo.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("商品不存在，ID=" + item.getProductId()));
            // 小计 = 单价 × 数量
            BigDecimal subTotal = p.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(subTotal);
        }

        // ========== 3. 计算优惠后应付金额 ==========
        BigDecimal pay = total;
        Coupon usedCoupon = null;

        if (couponId != null) {
            usedCoupon = couponRepo.findById(couponId).orElse(null);

            if (usedCoupon != null
                    && usedCoupon.getUserId().equals(userId)       // 必须是该用户的优惠券
                    && usedCoupon.getQuantity() > 0                // 还有剩余张数
                    && total.compareTo(usedCoupon.getThreshold()) >= 0) {  // 满足门槛

                if (usedCoupon.getType() == CouponType.FULL_REDUCTION) {
                    // 满减：应付 = 总价 - 减免金额
                    pay = total.subtract(usedCoupon.getAmount());
                } else if (usedCoupon.getType() == CouponType.DISCOUNT) {
                    // 折扣：应付 = 总价 × 折扣率（如 0.9 表示打九折）
                    pay = total.multiply(usedCoupon.getAmount())
                            .setScale(2, RoundingMode.HALF_UP);
                }

                // 应付金额最低为0
                if (pay.compareTo(BigDecimal.ZERO) < 0) {
                    pay = BigDecimal.ZERO;
                }

                // 扣减优惠券数量
                usedCoupon.setQuantity(usedCoupon.getQuantity() - 1);
                couponRepo.save(usedCoupon);

            } else {
                // 优惠券不可用（不满足条件），当作未使用处理
                couponId = null;
            }
        }

        // ========== 4. 校验用户余额 & 扣款 ==========
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (user.getBalance().compareTo(pay) < 0) {
            throw new RuntimeException("余额不足！当前余额：" + user.getBalance()
                    + "，需支付：" + pay);
        }

        // 扣减余额
        user.setBalance(user.getBalance().subtract(pay));
        userRepo.save(user);

        // ========== 5. 创建订单主记录 ==========
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(total);
        order.setPayAmount(pay);
        order.setCouponId(couponId);
        order.setCreatedTime(LocalDateTime.now());
        orderRepo.save(order);

        // ========== 6. 创建订单明细（逐条保存） ==========
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

        // ========== 7. 清空购物车 ==========
        cartRepo.deleteByUserId(userId);

        return order;
    }

    // ==================== 查询方法 ====================

    /**
     * 查询某个用户的所有订单（前台 - 我的订单）
     */
    public List<Order> listOrders(Long userId) {
        return orderRepo.findByUserId(userId);
    }

    /**
     * 查询所有订单（后台 - 管理员查看）
     */
    public List<Order> listAll() {
        return orderRepo.findAll();
    }

    /**
     * 根据ID获取单个订单
     */
    public Order getOrder(Long id) {
        return orderRepo.findById(id).orElse(null);
    }

    /**
     * 获取某个订单的明细列表
     */
    public List<OrderItem> listItems(Long orderId) {
        return orderItemRepo.findByOrderId(orderId);
    }
}