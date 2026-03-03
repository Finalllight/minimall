package com.example.minimall.dto;
import com.example.minimall.entity.CartItem;
import com.example.minimall.entity.Product;

import java.math.BigDecimal;

/**
 * 购物车展示 DTO：把 CartItem 与 Product 拼在一起，
 * 并提供小计计算。
 */
public class CartItemView {

    private CartItem cartItem;
    private Product product;

    public CartItemView() {
    }

    public CartItemView(CartItem cartItem, Product product) {
        this.cartItem = cartItem;
        this.product = product;
    }

    public CartItem getCartItem() {
        return cartItem;
    }

    public void setCartItem(CartItem cartItem) {
        this.cartItem = cartItem;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * 计算该商品的小计 = 单价 * 数量
     */
    public BigDecimal getSubTotal() {
        if (product == null || cartItem == null) {
            return BigDecimal.ZERO;
        }
        if (product.getPrice() == null || cartItem.getQuantity() == null) {
            return BigDecimal.ZERO;
        }
        return product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
    }
}
