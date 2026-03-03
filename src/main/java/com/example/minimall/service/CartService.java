package com.example.minimall.service;

import com.example.minimall.dto.CartItemView;
import com.example.minimall.entity.CartItem;
import com.example.minimall.entity.Product;
import com.example.minimall.repository.CartItemRepository;
import com.example.minimall.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CartService {
    private final CartItemRepository cartRepo;
    private final ProductRepository productRepo;

    public CartService(CartItemRepository cartRepo, ProductRepository productRepo) {
        this.cartRepo = cartRepo;
        this.productRepo = productRepo;
    }

    public void addToCart(Long userId, Long productId, int quantity) {
        CartItem item = cartRepo.findByUserIdAndProductId(userId, productId)
                .orElse(null);
        if (item == null) {
            item = new CartItem();
            item.setUserId(userId);
            item.setProductId(productId);
            item.setQuantity(quantity);
        } else {
            item.setQuantity(item.getQuantity() + quantity);
        }
        cartRepo.save(item);
    }

    public List<CartItemView> listCartItems(Long userId) {
        List<CartItem> items = cartRepo.findByUserId(userId);
        List<CartItemView> views = new ArrayList<>();
        for (CartItem item : items) {
            Product p = productRepo.findById(item.getProductId()).orElse(null);
            if (p != null) views.add(new CartItemView(item, p));
        }
        return views;
    }

    public BigDecimal calcTotal(Long userId) {
        return listCartItems(userId).stream()
                .map(CartItemView::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void updateQuantity(Long itemId, int quantity) {
        cartRepo.findById(itemId).ifPresent(item -> {
            if (quantity <= 0) cartRepo.delete(item);
            else {
                item.setQuantity(quantity);
                cartRepo.save(item);
            }
        });
    }

    public void removeItem(Long itemId) {
        cartRepo.deleteById(itemId);
    }

    public void clearCart(Long userId) {
        cartRepo.deleteByUserId(userId);
    }
}
