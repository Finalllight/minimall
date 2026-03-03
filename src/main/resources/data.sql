-- =========================
-- 1. 创建数据库
-- =========================
DROP DATABASE IF EXISTS mini_mall;
CREATE DATABASE mini_mall DEFAULT CHARSET utf8mb4;
USE mini_mall;

-- =========================
-- 2. 用户表
-- =========================
CREATE TABLE users (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================
-- 3. 商品表
-- =========================
CREATE TABLE product (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         name VARCHAR(100) NOT NULL,
                         description TEXT,
                         price DECIMAL(10,2) NOT NULL,
                         stock INT NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================
-- 4. 购物车表
-- =========================
CREATE TABLE cart_item (
                           id BIGINT PRIMARY KEY AUTO_INCREMENT,
                           user_id BIGINT NOT NULL,
                           product_id BIGINT NOT NULL,
                           quantity INT NOT NULL,
                           UNIQUE KEY uk_user_product (user_id, product_id),
                           CONSTRAINT fk_cart_user FOREIGN KEY (user_id) REFERENCES users(id),
                           CONSTRAINT fk_cart_product FOREIGN KEY (product_id) REFERENCES product(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================
-- 5. 优惠券表
-- type: FULL_REDUCTION / DISCOUNT
-- =========================
CREATE TABLE coupon (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        user_id BIGINT NOT NULL,
                        type ENUM('FULL_REDUCTION','DISCOUNT') NOT NULL,
                        threshold DECIMAL(10,2) NOT NULL,
                        amount DECIMAL(10,2) NOT NULL,
                        quantity INT NOT NULL DEFAULT 0,
                        CONSTRAINT fk_coupon_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================
-- 6. 订单表
-- =========================
CREATE TABLE orders (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        user_id BIGINT NOT NULL,
                        total_amount DECIMAL(10,2) NOT NULL,
                        pay_amount DECIMAL(10,2) NOT NULL,
                        coupon_id BIGINT NULL,
                        created_time DATETIME NOT NULL,
                        CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES users(id),
                        CONSTRAINT fk_order_coupon FOREIGN KEY (coupon_id) REFERENCES coupon(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================
-- 7. 订单明细表
-- =========================
CREATE TABLE order_item (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            order_id BIGINT NOT NULL,
                            product_id BIGINT NOT NULL,
                            product_name VARCHAR(100) NOT NULL,
                            price DECIMAL(10,2) NOT NULL,
                            quantity INT NOT NULL,
                            CONSTRAINT fk_orderitem_order FOREIGN KEY (order_id) REFERENCES orders(id),
                            CONSTRAINT fk_orderitem_product FOREIGN KEY (product_id) REFERENCES product(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================
-- 8. 可选：插入测试数据
-- =========================
INSERT INTO users(id, username, password) VALUES (1,'alice','123');

INSERT INTO product(id, name, description, price, stock) VALUES
                                                             (1,'Java编程思想','经典Java书籍',88.00,100),
                                                             (2,'算法图解','入门算法书籍',55.00,100),
                                                             (3,'机械键盘','茶轴机械键盘',199.00,50);

INSERT INTO coupon(id, user_id, type, threshold, amount, quantity) VALUES
                                                                       (1,1,'FULL_REDUCTION',100,20,1),
                                                                       (2,1,'DISCOUNT',50,0.9,2);
