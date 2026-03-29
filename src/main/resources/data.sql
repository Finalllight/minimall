-- ===============================================================
-- data.sql — 极简电子商城 Mini Mall 初始化测试数据
-- 说明：Spring Boot 启动时自动执行（application.yml: spring.sql.init.mode=always）
-- 使用 INSERT IGNORE 防止重复启动插入冲突
-- ===============================================================

-- ========================
-- 1. 用户数据
-- 密码规则：≥8位，含大小写+数字+特殊字符
-- ========================
INSERT IGNORE INTO users (id, username, password, role, balance) VALUES
                                                                     (1, 'alice',  'Alice123!',  'USER',  500.00),
                                                                     (2, 'bob',    'Bob@2025x',  'USER',  300.00),
                                                                     (3, 'carol',  'Carol#99x',  'USER',  150.00),
                                                                     (4, 'david',  'David$888',  'USER',   80.00),
                                                                     (5, 'admin',  'Admin123!',  'ADMIN',   0.00);

-- ========================
-- 2. 商品数据
-- ========================
INSERT IGNORE INTO product (id, name, description, price, stock) VALUES
                                                                     (1,  'Java编程思想',       '经典Java进阶书籍，深入理解面向对象',               88.00,  100),
                                                                     (2,  '算法图解',           '图文并茂的入门算法书籍',                         55.00,  100),
                                                                     (3,  '机械键盘',           '茶轴机械键盘，手感舒适',                         199.00,   50),
                                                                     (4,  '无线鼠标',           '静音无线鼠标，办公利器',                          69.00,  200),
                                                                     (5,  'USB-C扩展坞',        '七合一Type-C扩展坞，兼容主流笔记本',              129.00,   80),
                                                                     (6,  '程序员帽衫',         '黑色连帽卫衣，前面印有代码图案',                   99.00,  150),
                                                                     (7,  '深入理解计算机系统',  'CSAPP经典教材，计算机科学必读',                   139.00,   60),
                                                                     (8,  '27寸显示器',         '2K高清IPS屏幕，75Hz刷新率',                      899.00,   30),
                                                                     (9,  '笔记本支架',         '铝合金材质，可调高度，散热好',                      59.00,  120),
                                                                     (10, '降噪耳机',           '主动降噪，蓝牙5.3，续航30小时',                   249.00,   40);

-- ========================
-- 3. 优惠券数据
-- type: FULL_REDUCTION（满减） / DISCOUNT（折扣）
-- threshold: 满多少可用
-- amount: 满减 → 减多少元 / 折扣 → 折扣率（如0.9表示打9折）
-- quantity: 可用次数
-- ========================

-- alice 的优惠券
INSERT IGNORE INTO coupon (id, user_id, type, threshold, amount, quantity) VALUES
                                                                               (1, 1, 'FULL_REDUCTION', 100.00,  20.00, 1),   -- 满100减20，可用1次
                                                                               (2, 1, 'DISCOUNT',        50.00,   0.90, 2),   -- 满50打9折，可用2次
                                                                               (3, 1, 'FULL_REDUCTION', 200.00,  50.00, 1);   -- 满200减50，可用1次

-- bob 的优惠券
INSERT IGNORE INTO coupon (id, user_id, type, threshold, amount, quantity) VALUES
                                                                               (4, 2, 'FULL_REDUCTION',  80.00,  10.00, 2),   -- 满80减10，可用2次
                                                                               (5, 2, 'DISCOUNT',       150.00,   0.85, 1);   -- 满150打8.5折，可用1次

-- carol 的优惠券
INSERT IGNORE INTO coupon (id, user_id, type, threshold, amount, quantity) VALUES
                                                                               (6, 3, 'FULL_REDUCTION',  60.00,   8.00, 3),   -- 满60减8，可用3次
                                                                               (7, 3, 'DISCOUNT',       100.00,   0.80, 1);   -- 满100打8折，可用1次

-- david 的优惠券
INSERT IGNORE INTO coupon (id, user_id, type, threshold, amount, quantity) VALUES
    (8, 4, 'FULL_REDUCTION',  50.00,   5.00, 2);   -- 满50减5，可用2次
