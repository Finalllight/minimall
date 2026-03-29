-- ===============================================================
-- schema.sql — 极简电子商城 Mini Mall 完整建表脚本
-- 数据库：MySQL 8
-- 编码：utf8mb4
-- ===============================================================

-- =========================
-- 1. 创建数据库
-- =========================
DROP DATABASE IF EXISTS mini_mall;
CREATE DATABASE mini_mall DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_general_ci;
USE mini_mall;

-- =========================
-- 2. 用户表
-- role: USER / ADMIN
-- balance: 用户余额
-- =========================
CREATE TABLE users (
                       id          BIGINT          PRIMARY KEY AUTO_INCREMENT,
                       username    VARCHAR(50)     NOT NULL UNIQUE          COMMENT '用户名：4-16位字母数字',
                       password    VARCHAR(100)    NOT NULL                 COMMENT '密码：≥8位，含大小写+数字+特殊字符',
                       role        VARCHAR(20)     NOT NULL DEFAULT 'USER'  COMMENT '角色：USER / ADMIN',
                       balance     DECIMAL(10,2)   NOT NULL DEFAULT 0.00    COMMENT '账户余额'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- =========================
-- 3. 商品表
-- =========================
CREATE TABLE product (
                         id          BIGINT          PRIMARY KEY AUTO_INCREMENT,
                         name        VARCHAR(100)    NOT NULL                 COMMENT '商品名称',
                         description TEXT                                     COMMENT '商品描述',
                         price       DECIMAL(10,2)   NOT NULL                 COMMENT '商品单价',
                         stock       INT             NOT NULL DEFAULT 0       COMMENT '库存数量'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- =========================
-- 4. 购物车表
-- 同一用户同一商品只能有一条记录（唯一索引）
-- =========================
CREATE TABLE cart_item (
                           id          BIGINT          PRIMARY KEY AUTO_INCREMENT,
                           user_id     BIGINT          NOT NULL                 COMMENT '所属用户ID',
                           product_id  BIGINT          NOT NULL                 COMMENT '商品ID',
                           quantity    INT             NOT NULL                 COMMENT '数量',

                           UNIQUE  KEY uk_user_product   (user_id, product_id),
                           CONSTRAINT  fk_cart_user      FOREIGN KEY (user_id)    REFERENCES users(id),
                           CONSTRAINT  fk_cart_product   FOREIGN KEY (product_id) REFERENCES product(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- =========================
-- 5. 优惠券表
-- type: FULL_REDUCTION（满减） / DISCOUNT（折扣）
-- threshold: 使用门槛金额
-- amount:
--   满减 → 减免金额（如 20.00 表示减20元）
--   折扣 → 折扣率  （如  0.90 表示打9折）
-- quantity: 可用次数
-- =========================
CREATE TABLE coupon (
                        id          BIGINT                              PRIMARY KEY AUTO_INCREMENT,
                        user_id     BIGINT          NOT NULL            COMMENT '所属用户ID',
                        type        ENUM('FULL_REDUCTION','DISCOUNT')   NOT NULL COMMENT '类型：满减/折扣',
                        threshold   DECIMAL(10,2)   NOT NULL            COMMENT '使用门槛金额',
                        amount      DECIMAL(10,2)   NOT NULL            COMMENT '满减金额 或 折扣率',
                        quantity    INT             NOT NULL DEFAULT 0  COMMENT '可用次数',

                        CONSTRAINT  fk_coupon_user  FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优惠券表';

-- =========================
-- 6. 订单表
-- =========================
CREATE TABLE orders (
                        id            BIGINT          PRIMARY KEY AUTO_INCREMENT,
                        user_id       BIGINT          NOT NULL                    COMMENT '下单用户ID',
                        total_amount  DECIMAL(10,2)   NOT NULL                    COMMENT '订单总价（优惠前）',
                        pay_amount    DECIMAL(10,2)   NOT NULL                    COMMENT '实付金额（优惠后）',
                        coupon_id     BIGINT          NULL                        COMMENT '使用的优惠券ID（可为空）',
                        created_time  DATETIME        NOT NULL                    COMMENT '下单时间',

                        CONSTRAINT  fk_order_user    FOREIGN KEY (user_id)   REFERENCES users(id),
                        CONSTRAINT  fk_order_coupon  FOREIGN KEY (coupon_id) REFERENCES coupon(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- =========================
-- 7. 订单明细表
-- =========================
CREATE TABLE order_item (
                            id            BIGINT          PRIMARY KEY AUTO_INCREMENT,
                            order_id      BIGINT          NOT NULL                    COMMENT '所属订单ID',
                            product_id    BIGINT          NOT NULL                    COMMENT '商品ID',
                            product_name  VARCHAR(100)    NOT NULL                    COMMENT '商品名称（下单快照）',
                            price         DECIMAL(10,2)   NOT NULL                    COMMENT '商品单价（下单快照）',
                            quantity      INT             NOT NULL                    COMMENT '购买数量',

                            CONSTRAINT  fk_oi_order    FOREIGN KEY (order_id)   REFERENCES orders(id),
                            CONSTRAINT  fk_oi_product  FOREIGN KEY (product_id) REFERENCES product(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';
