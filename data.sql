-- 智慧图书馆初始化数据

-- 插入管理员账号（密码是 BCrypt 加密后的 admin123）
INSERT INTO `t_user` (`account`, `password`, `name`, `role`, `gender`, `phone`, `email`, `status`, `register_time`) VALUES
('admin', '$2a$10$55LuPCGCukosvJJcNl3RjOBikDM6z1wLxfz7ZK6jjIMLpqGFqZ.xm', '系统管理员', 'admin', '男', '13800138000', 'admin@library.com', 1, NOW()),
('test', '$2a$10$55LuPCGCukosvJJcNl3RjOBikDM6z1wLxfz7ZK6jjIMLpqGFqZ.xm', '测试管理员', 'admin', '女', '13800138001', 'test@library.com', 1, NOW());

-- 插入图书数据（20本）
INSERT INTO `t_book` (`title`, `author`, `isbn`, `category`, `publish_date`, `cover_url`, `total_stock`, `available_stock`, `status`, `description`) VALUES
-- 文学类
('活着', '余华', '978-7-5322-5000-0', '文学', '2012-08-01', '/uploads/books/default/cover1.jpg', 5, 4, 0, '余华的代表作，讲述农村人福贵悲惨的人生遭遇。'),
('平凡的世界', '路遥', '978-7-5322-6000-0', '文学', '2017-03-01', '/uploads/books/default/cover2.jpg', 8, 6, 0, '茅盾文学奖获奖作品，全景式地展现中国当代城乡社会生活。'),
('百年孤独', '加西亚·马尔克斯', '978-7-5322-7000-0', '文学', '2011-06-01', '/uploads/books/default/cover3.jpg', 4, 3, 0, '魔幻现实主义文学的代表作，描写布恩迪亚家族七代人的传奇故事。'),
('围城', '钱钟书', '978-7-5322-8000-0', '文学', '2019-05-01', '/uploads/books/default/cover4.jpg', 6, 5, 0, '中国现代文学史上一部风格独特的讽刺小说。'),
('三体', '刘慈欣', '978-7-5322-9000-0', '文学', '2020-08-01', '/uploads/books/default/cover5.jpg', 10, 8, 0, '科幻小说巅峰之作，讲述地球文明与三体文明的博弈。'),

-- 科技类
('深入理解计算机系统', 'Randal E. Bryant', '978-7-111-54493-7', '科技', '2015-11-01', '/uploads/books/default/cover6.jpg', 3, 2, 0, '系统级理解计算机系统的经典教材。'),
('算法导论', 'Thomas H. Cormen', '978-7-111-54493-8', '科技', '2012-01-01', '/uploads/books/default/cover7.jpg', 4, 4, 0, '算法领域的经典教材，全面介绍了计算机算法。'),
('Python编程：从入门到实践', 'Eric Matthes', '978-7-111-54493-9', '科技', '2019-04-01', '/uploads/books/default/cover8.jpg', 8, 7, 0, 'Python入门经典书籍，包含实践项目。'),
('Java核心技术', 'Cay S. Horstmann', '978-7-111-54494-0', '科技', '2018-08-01', '/uploads/books/default/cover9.jpg', 6, 5, 0, 'Java技术经典参考书。'),
('人工智能：一种现代方法', 'Stuart Russell', '978-7-111-54494-1', '科技', '2016-06-01', '/uploads/books/default/cover10.jpg', 5, 4, 0, 'AI领域的经典教材。'),

-- 教育类
('教育心理学', '林崇德', '978-7-111-54494-2', '教育', '2019-01-01', '/uploads/books/default/cover11.jpg', 7, 6, 0, '教育学专业核心教材。'),
('课程与教学论', '王策三', '978-7-111-54494-3', '教育', '2018-03-01', '/uploads/books/default/cover12.jpg', 5, 5, 0, '课程与教学论经典著作。'),
('现代教育技术', '祝智庭', '978-7-111-54494-4', '教育', '2020-02-01', '/uploads/books/default/cover13.jpg', 4, 3, 0, '教育技术学专业教材。'),

-- 艺术类
('艺术概论', '王宏建', '978-7-111-54494-5', '艺术', '2017-06-01', '/uploads/books/default/cover14.jpg', 6, 5, 0, '艺术学基础理论教材。'),
('中国美术史', '中央美术学院', '978-7-111-54494-6', '艺术', '2018-09-01', '/uploads/books/default/cover15.jpg', 4, 4, 0, '系统介绍中国美术发展历程。'),
('平面设计基础', '王雪青', '978-7-111-54494-7', '艺术', '2019-07-01', '/uploads/books/default/cover16.jpg', 5, 4, 0, '平面设计入门教材。'),

-- 其他
('时间简史', '斯蒂芬·霍金', '978-7-111-54494-8', '其他', '2018-01-01', '/uploads/books/default/cover17.jpg', 6, 5, 0, '宇宙学科普经典。'),
('万物简史', '比尔·布莱森', '978-7-111-54494-9', '其他', '2019-05-01', '/uploads/books/default/cover18.jpg', 4, 3, 0, '关于宇宙和人类历程的科普书。'),
('人类简史', '尤瓦尔·赫拉利', '978-7-111-54495-0', '其他', '2017-08-01', '/uploads/books/default/cover19.jpg', 8, 7, 0, '从认知革命到科技革命的人类发展史。'),
('未来简史', '尤瓦尔·赫拉利', '978-7-111-54495-1', '其他', '2020-10-01', '/uploads/books/default/cover20.jpg', 5, 4, 0, '预测人类未来走向的思考。');

-- 插入读者账号（10个）
INSERT INTO `t_user` (`account`, `password`, `name`, `role`, `gender`, `phone`, `email`, `status`, `register_time`) VALUES
('reader001', '$2a$10$7pTV1.XU/ccR1HjfRwxOfOyr77lBGtfams45jS.immxa2adPB34GC', '张三', 'reader', '男', '13900139001', 'zhangsan@reader.com', 1, DATE_SUB(NOW(), INTERVAL 180 DAY)),
('reader002', '$2a$10$7pTV1.XU/ccR1HjfRwxOfOyr77lBGtfams45jS.immxa2adPB34GC', '李四', 'reader', '女', '13900139002', 'lisi@reader.com', 1, DATE_SUB(NOW(), INTERVAL 150 DAY)),
('reader003', '$2a$10$7pTV1.XU/ccR1HjfRwxOfOyr77lBGtfams45jS.immxa2adPB34GC', '王五', 'reader', '男', '13900139003', 'wangwu@reader.com', 1, DATE_SUB(NOW(), INTERVAL 120 DAY)),
('reader004', '$2a$10$7pTV1.XU/ccR1HjfRwxOfOyr77lBGtfams45jS.immxa2adPB34GC', '赵六', 'reader', '女', '13900139004', 'zhaoliu@reader.com', 1, DATE_SUB(NOW(), INTERVAL 100 DAY)),
('reader005', '$2a$10$7pTV1.XU/ccR1HjfRwxOfOyr77lBGtfams45jS.immxa2adPB34GC', '钱七', 'reader', '男', '13900139005', 'qianqi@reader.com', 1, DATE_SUB(NOW(), INTERVAL 90 DAY)),
('reader006', '$2a$10$7pTV1.XU/ccR1HjfRwxOfOyr77lBGtfams45jS.immxa2adPB34GC', '孙八', 'reader', '女', '13900139006', 'sunba@reader.com', 1, DATE_SUB(NOW(), INTERVAL 80 DAY)),
('reader007', '$2a$10$7pTV1.XU/ccR1HjfRwxOfOyr77lBGtfams45jS.immxa2adPB34GC', '周九', 'reader', '男', '13900139007', 'zhoujiu@reader.com', 1, DATE_SUB(NOW(), INTERVAL 70 DAY)),
('reader008', '$2a$10$7pTV1.XU/ccR1HjfRwxOfOyr77lBGtfams45jS.immxa2adPB34GC', '吴十', 'reader', '女', '13900139008', 'wushi@reader.com', 1, DATE_SUB(NOW(), INTERVAL 60 DAY)),
('reader009', '$2a$10$7pTV1.XU/ccR1HjfRwxOfOyr77lBGtfams45jS.immxa2adPB34GC', '郑一', 'reader', '男', '13900139009', 'zhengyi@reader.com', 1, DATE_SUB(NOW(), INTERVAL 50 DAY)),
('reader010', '$2a$10$7pTV1.XU/ccR1HjfRwxOfOyr77lBGtfams45jS.immxa2adPB34GC', '陈二', 'reader', '女', '13900139010', 'chener@reader.com', 1, DATE_SUB(NOW(), INTERVAL 40 DAY));

-- 插入借阅记录（30条，包含已归还和借阅中，部分逾期）
-- 已归还的记录
INSERT INTO `t_borrow_record` (`book_id`, `user_id`, `borrow_time`, `expected_return_time`, `actual_return_time`, `overdue_days`, `status`) VALUES
(1, 3, DATE_SUB(NOW(), INTERVAL 90 DAY), DATE_SUB(NOW(), INTERVAL 60 DAY), DATE_SUB(NOW(), INTERVAL 65 DAY), 0, 1),
(2, 3, DATE_SUB(NOW(), INTERVAL 85 DAY), DATE_SUB(NOW(), INTERVAL 55 DAY), DATE_SUB(NOW(), INTERVAL 58 DAY), 0, 1),
(5, 4, DATE_SUB(NOW(), INTERVAL 80 DAY), DATE_SUB(NOW(), INTERVAL 50 DAY), DATE_SUB(NOW(), INTERVAL 52 DAY), 0, 1),
(6, 4, DATE_SUB(NOW(), INTERVAL 75 DAY), DATE_SUB(NOW(), INTERVAL 45 DAY), DATE_SUB(NOW(), INTERVAL 48 DAY), 0, 1),
(3, 5, DATE_SUB(NOW(), INTERVAL 70 DAY), DATE_SUB(NOW(), INTERVAL 40 DAY), DATE_SUB(NOW(), INTERVAL 42 DAY), 0, 1),
(8, 5, DATE_SUB(NOW(), INTERVAL 68 DAY), DATE_SUB(NOW(), INTERVAL 38 DAY), DATE_SUB(NOW(), INTERVAL 40 DAY), 0, 1),
(10, 6, DATE_SUB(NOW(), INTERVAL 65 DAY), DATE_SUB(NOW(), INTERVAL 35 DAY), DATE_SUB(NOW(), INTERVAL 37 DAY), 0, 1),
(12, 6, DATE_SUB(NOW(), INTERVAL 60 DAY), DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 32 DAY), 0, 1),
(15, 7, DATE_SUB(NOW(), INTERVAL 58 DAY), DATE_SUB(NOW(), INTERVAL 28 DAY), DATE_SUB(NOW(), INTERVAL 30 DAY), 0, 1),
(17, 7, DATE_SUB(NOW(), INTERVAL 55 DAY), DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 27 DAY), 0, 1),
(19, 8, DATE_SUB(NOW(), INTERVAL 50 DAY), DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 22 DAY), 0, 1),
(20, 8, DATE_SUB(NOW(), INTERVAL 48 DAY), DATE_SUB(NOW(), INTERVAL 18 DAY), DATE_SUB(NOW(), INTERVAL 20 DAY), 0, 1),
(4, 9, DATE_SUB(NOW(), INTERVAL 45 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 17 DAY), 0, 1),
(7, 9, DATE_SUB(NOW(), INTERVAL 40 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 12 DAY), 0, 1),
(11, 10, DATE_SUB(NOW(), INTERVAL 38 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY), 0, 1),
(14, 10, DATE_SUB(NOW(), INTERVAL 35 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), 0, 1),
(16, 11, DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),

-- 借阅中的记录
(9, 3, DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY), NULL, 0, 0),
(13, 4, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY), NULL, 0, 0),
(18, 5, DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY), NULL, 0, 0),
(2, 6, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), NULL, 0, 0),
(6, 7, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), NULL, 0, 0),

-- 逾期未还的记录
(1, 3, DATE_SUB(NOW(), INTERVAL 50 DAY), DATE_SUB(NOW(), INTERVAL 20 DAY), NULL, 30, 0),
(5, 4, DATE_SUB(NOW(), INTERVAL 45 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY), NULL, 15, 0),
(8, 5, DATE_SUB(NOW(), INTERVAL 40 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY), NULL, 10, 0),
(12, 6, DATE_SUB(NOW(), INTERVAL 35 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), NULL, 5, 0),
(15, 7, DATE_SUB(NOW(), INTERVAL 32 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), NULL, 2, 0),
(19, 8, DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(3, 9, DATE_SUB(NOW(), INTERVAL 28 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), NULL, 0, 0),
(10, 10, DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), NULL, 0, 0),
(20, 11, DATE_SUB(NOW(), INTERVAL 22 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY), NULL, 0, 0),
(7, 12, DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY), NULL, 0, 0);

-- 插入图书分类统计数据
INSERT INTO `t_book_category_stat` (`category`, `borrow_count`, `stat_date`) VALUES
('文学', 15, DATE_SUB(CURDATE(), INTERVAL 1 MONTH)),
('科技', 12, DATE_SUB(CURDATE(), INTERVAL 1 MONTH)),
('教育', 8, DATE_SUB(CURDATE(), INTERVAL 1 MONTH)),
('艺术', 6, DATE_SUB(CURDATE(), INTERVAL 1 MONTH)),
('其他', 9, DATE_SUB(CURDATE(), INTERVAL 1 MONTH)),
('文学', 18, CURDATE()),
('科技', 14, CURDATE()),
('教育', 10, CURDATE()),
('艺术', 7, CURDATE()),
('其他', 11, CURDATE());

-- 插入用户标签
INSERT INTO `t_user_tag` (`user_id`, `tag_name`, `tag_type`, `create_time`) VALUES
(3, '文学爱好者', 'category', DATE_SUB(NOW(), INTERVAL 60 DAY)),
(3, '高频读者', 'frequency', DATE_SUB(NOW(), INTERVAL 60 DAY)),
(4, '科技爱好者', 'category', DATE_SUB(NOW(), INTERVAL 50 DAY)),
(5, '综合阅读', 'category', DATE_SUB(NOW(), INTERVAL 40 DAY)),
(6, '教育类偏好', 'category', DATE_SUB(NOW(), INTERVAL 30 DAY)),
(7, '艺术类偏好', 'category', DATE_SUB(NOW(), INTERVAL 25 DAY)),
(8, '高频读者', 'frequency', DATE_SUB(NOW(), INTERVAL 20 DAY)),
(9, '文学爱好者', 'category', DATE_SUB(NOW(), INTERVAL 15 DAY)),
(10, '科技爱好者', 'category', DATE_SUB(NOW(), INTERVAL 10 DAY)),
(11, '综合阅读', 'category', NOW());

-- 插入AI报告
INSERT INTO `t_ai_report` (`report_type`, `report_title`, `report_content`, `generate_time`) VALUES
('monthly', '2026年5月度借阅分析报告', '# 2026年5月度借阅分析报告\n\n## 概述\n本月共处理借阅请求 150 次，归还 120 次，当前在借 30 本。\n\n## 分类统计\n- 文学类图书借阅量最高，占比 35%\n- 科技类图书紧随其后，占比 28%\n- 其他类图书占比 22%\n- 艺术类和教育类各占 10% 和 5%\n\n## 读者分析\n本月活跃读者 45 人，其中高频读者（借阅>=5本）12 人。\n\n## 逾期情况\n本月逾期未还 8 本，逾期率 5.3%，较上月下降 2%。\n\n## 建议\n1. 增加文学类图书库存\n2. 开展科技类图书推广活动\n3. 优化借阅规则，减少逾期', DATE_SUB(NOW(), INTERVAL 5 DAY)),
('monthly', '2026年4月度借阅分析报告', '# 2026年4月度借阅分析报告\n\n## 概述\n本月共处理借阅请求 140 次，归还 115 次，当前在借 25 本。\n\n## 分类统计\n- 文学类占比 32%\n- 科技类占比 30%\n- 其他类占比 20%\n- 教育类占比 12%\n- 艺术类占比 6%\n\n## 读者分析\n本月活跃读者 42 人，新增注册 8 人。', DATE_SUB(NOW(), INTERVAL 35 DAY));

-- 补充逾期未还的借阅记录（status=2）
INSERT INTO `t_borrow_record` (`book_id`, `user_id`, `borrow_time`, `expected_return_time`, `actual_return_time`, `overdue_days`, `status`) VALUES
(4, 3, DATE_SUB(NOW(), INTERVAL 60 DAY), DATE_SUB(NOW(), INTERVAL 30 DAY), NULL, 30, 2),
(9, 5, DATE_SUB(NOW(), INTERVAL 55 DAY), DATE_SUB(NOW(), INTERVAL 25 DAY), NULL, 25, 2),
(13, 7, DATE_SUB(NOW(), INTERVAL 50 DAY), DATE_SUB(NOW(), INTERVAL 20 DAY), NULL, 20, 2),
(18, 9, DATE_SUB(NOW(), INTERVAL 45 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY), NULL, 15, 2),
(20, 11, DATE_SUB(NOW(), INTERVAL 40 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY), NULL, 10, 2);

-- ============================================================
-- 补充数据（用于热门排行、智能推荐、趋势图展示）
-- ============================================================

-- 补充图书（10本，ID 21-30）
INSERT INTO `t_book` (`title`, `author`, `isbn`, `category`, `publish_date`, `cover_url`, `total_stock`, `available_stock`, `status`, `description`) VALUES
('红楼梦', '曹雪芹', '978-7-111-54495-2', '文学', '2018-12-01', '/uploads/books/default/cover21.jpg', 6, 5, 0, '中国古典四大名著之首，以贾宝玉、林黛玉的爱情悲剧为主线。'),
('三体II：黑暗森林', '刘慈欣', '978-7-111-54495-3', '文学', '2021-06-01', '/uploads/books/default/cover22.jpg', 8, 6, 0, '三体系列第二部，面壁计划与黑暗森林法则。'),
('设计模式：可复用面向对象软件的基础', 'Erich Gamma', '978-7-111-54495-4', '科技', '2014-09-01', '/uploads/books/default/cover23.jpg', 5, 4, 0, 'GoF经典设计模式教材，软件工程师必读。'),
('Spring Boot实战', 'Craig Walls', '978-7-111-54495-5', '科技', '2022-03-01', '/uploads/books/default/cover24.jpg', 7, 6, 0, 'Spring Boot开发实战指南。'),
('如何阅读一本书', '莫提默·J·艾德勒', '978-7-111-54495-6', '教育', '2016-11-01', '/uploads/books/default/cover25.jpg', 5, 4, 0, '阅读方法经典著作，教读者如何深度阅读。'),
('学习之道', '芭芭拉·奥克利', '978-7-111-54495-7', '教育', '2019-08-01', '/uploads/books/default/cover26.jpg', 6, 5, 0, '关于高效学习的思维方法。'),
('设计中的设计', '原研哉', '978-7-111-54495-8', '艺术', '2017-05-01', '/uploads/books/default/cover27.jpg', 4, 3, 0, '日本设计大师原研哉的设计理念。'),
('写给大家看的设计书', 'Robin Williams', '978-7-111-54495-9', '艺术', '2018-07-01', '/uploads/books/default/cover28.jpg', 5, 4, 0, '设计入门经典，简单易懂的设计原则。'),
('枪炮、病菌与钢铁', '贾雷德·戴蒙德', '978-7-111-54496-0', '其他', '2016-06-01', '/uploads/books/default/cover29.jpg', 6, 5, 0, '人类社会发展史的颠覆性著作。'),
('万历十五年', '黄仁宇', '978-7-111-54496-1', '其他', '2017-09-01', '/uploads/books/default/cover30.jpg', 5, 4, 0, '以万历十五年为切入点，剖析明代社会结构。');

-- 补充近30天大量借阅记录（使趋势图、热门排行生效）
-- 第30天（30天前）：11次
INSERT INTO `t_borrow_record` (`book_id`, `user_id`, `borrow_time`, `expected_return_time`, `actual_return_time`, `overdue_days`, `status`) VALUES
(5, 3, DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(1, 4, DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(6, 5, DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1),
(10, 6, DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(15, 7, DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 0, 1),
(19, 8, DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(3, 9, DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(22, 10, DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 1),
(8, 11, DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), 0, 1),
(25, 12, DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(30, 3, DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1);

-- 第29天：8次
INSERT INTO `t_borrow_record` (`book_id`, `user_id`, `borrow_time`, `expected_return_time`, `actual_return_time`, `overdue_days`, `status`) VALUES
(2, 4, DATE_SUB(NOW(), INTERVAL 29 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 1),
(7, 5, DATE_SUB(NOW(), INTERVAL 29 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(11, 6, DATE_SUB(NOW(), INTERVAL 29 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 0, 1),
(16, 7, DATE_SUB(NOW(), INTERVAL 29 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(21, 8, DATE_SUB(NOW(), INTERVAL 29 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(26, 9, DATE_SUB(NOW(), INTERVAL 29 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY), 0, 1),
(4, 10, DATE_SUB(NOW(), INTERVAL 29 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1),
(23, 11, DATE_SUB(NOW(), INTERVAL 29 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0);

-- 第28天：5次
INSERT INTO `t_borrow_record` (`book_id`, `user_id`, `borrow_time`, `expected_return_time`, `actual_return_time`, `overdue_days`, `status`) VALUES
(5, 4, DATE_SUB(NOW(), INTERVAL 28 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(1, 5, DATE_SUB(NOW(), INTERVAL 28 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), 0, 1),
(9, 6, DATE_SUB(NOW(), INTERVAL 28 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(14, 7, DATE_SUB(NOW(), INTERVAL 28 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 0, 1),
(18, 8, DATE_SUB(NOW(), INTERVAL 28 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 1);

-- 第27天：12次（高峰）
INSERT INTO `t_borrow_record` (`book_id`, `user_id`, `borrow_time`, `expected_return_time`, `actual_return_time`, `overdue_days`, `status`) VALUES
(5, 3, DATE_SUB(NOW(), INTERVAL 27 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1),
(6, 4, DATE_SUB(NOW(), INTERVAL 27 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(8, 5, DATE_SUB(NOW(), INTERVAL 27 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(22, 6, DATE_SUB(NOW(), INTERVAL 27 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(3, 7, DATE_SUB(NOW(), INTERVAL 27 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), 0, 1),
(1, 8, DATE_SUB(NOW(), INTERVAL 27 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY), 0, 1),
(10, 9, DATE_SUB(NOW(), INTERVAL 27 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 0, 1),
(27, 10, DATE_SUB(NOW(), INTERVAL 27 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(24, 11, DATE_SUB(NOW(), INTERVAL 27 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1),
(29, 12, DATE_SUB(NOW(), INTERVAL 27 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 1),
(2, 3, DATE_SUB(NOW(), INTERVAL 27 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(21, 4, DATE_SUB(NOW(), INTERVAL 27 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0);

-- 第26天：7次
INSERT INTO `t_borrow_record` (`book_id`, `user_id`, `borrow_time`, `expected_return_time`, `actual_return_time`, `overdue_days`, `status`) VALUES
(1, 3, DATE_SUB(NOW(), INTERVAL 26 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY), 0, 1),
(5, 9, DATE_SUB(NOW(), INTERVAL 26 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 0, 1),
(7, 10, DATE_SUB(NOW(), INTERVAL 26 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1),
(23, 11, DATE_SUB(NOW(), INTERVAL 26 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(25, 12, DATE_SUB(NOW(), INTERVAL 26 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), 0, 1),
(11, 5, DATE_SUB(NOW(), INTERVAL 26 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(30, 6, DATE_SUB(NOW(), INTERVAL 26 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0);

-- 第25天：10次（高峰）
INSERT INTO `t_borrow_record` (`book_id`, `user_id`, `borrow_time`, `expected_return_time`, `actual_return_time`, `overdue_days`, `status`) VALUES
(6, 3, DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1),
(5, 4, DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 1),
(8, 5, DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(2, 6, DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(9, 7, DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(22, 8, DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 0, 1),
(24, 9, DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY), 0, 1),
(28, 10, DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(1, 11, DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), 0, 1),
(26, 12, DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1);

-- 第24天：6次
INSERT INTO `t_borrow_record` (`book_id`, `user_id`, `borrow_time`, `expected_return_time`, `actual_return_time`, `overdue_days`, `status`) VALUES
(10, 3, DATE_SUB(NOW(), INTERVAL 24 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), 0, 1),
(15, 4, DATE_SUB(NOW(), INTERVAL 24 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 0, 1),
(20, 5, DATE_SUB(NOW(), INTERVAL 24 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(25, 6, DATE_SUB(NOW(), INTERVAL 24 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(29, 7, DATE_SUB(NOW(), INTERVAL 24 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 1),
(3, 8, DATE_SUB(NOW(), INTERVAL 24 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1);

-- 第23天：14次（高峰-周末）
INSERT INTO `t_borrow_record` (`book_id`, `user_id`, `borrow_time`, `expected_return_time`, `actual_return_time`, `overdue_days`, `status`) VALUES
(5, 3, DATE_SUB(NOW(), INTERVAL 23 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(1, 4, DATE_SUB(NOW(), INTERVAL 23 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 0, 1),
(6, 5, DATE_SUB(NOW(), INTERVAL 23 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY), 0, 1),
(8, 6, DATE_SUB(NOW(), INTERVAL 23 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(22, 7, DATE_SUB(NOW(), INTERVAL 23 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1),
(21, 8, DATE_SUB(NOW(), INTERVAL 23 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), 0, 1),
(7, 9, DATE_SUB(NOW(), INTERVAL 23 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY), 0, 1),
(30, 10, DATE_SUB(NOW(), INTERVAL 23 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(24, 11, DATE_SUB(NOW(), INTERVAL 23 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(27, 12, DATE_SUB(NOW(), INTERVAL 23 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 1),
(11, 3, DATE_SUB(NOW(), INTERVAL 23 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), 0, 1),
(4, 4, DATE_SUB(NOW(), INTERVAL 23 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 0, 1),
(14, 5, DATE_SUB(NOW(), INTERVAL 23 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(9, 6, DATE_SUB(NOW(), INTERVAL 23 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1);

-- 第22天：4次
INSERT INTO `t_borrow_record` (`book_id`, `user_id`, `borrow_time`, `expected_return_time`, `actual_return_time`, `overdue_days`, `status`) VALUES
(18, 7, DATE_SUB(NOW(), INTERVAL 22 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), 0, 1),
(23, 8, DATE_SUB(NOW(), INTERVAL 22 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(2, 9, DATE_SUB(NOW(), INTERVAL 22 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(28, 10, DATE_SUB(NOW(), INTERVAL 22 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), 0, 1);

-- 第21天：9次
INSERT INTO `t_borrow_record` (`book_id`, `user_id`, `borrow_time`, `expected_return_time`, `actual_return_time`, `overdue_days`, `status`) VALUES
(5, 11, DATE_SUB(NOW(), INTERVAL 21 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1),
(1, 12, DATE_SUB(NOW(), INTERVAL 21 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 1),
(8, 3, DATE_SUB(NOW(), INTERVAL 21 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(24, 4, DATE_SUB(NOW(), INTERVAL 21 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(29, 5, DATE_SUB(NOW(), INTERVAL 21 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 0, 1),
(6, 6, DATE_SUB(NOW(), INTERVAL 21 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), 0, 1),
(15, 7, DATE_SUB(NOW(), INTERVAL 21 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), 0, 1),
(26, 8, DATE_SUB(NOW(), INTERVAL 21 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(3, 9, DATE_SUB(NOW(), INTERVAL 21 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1);

-- 第20天：13次（高峰）
INSERT INTO `t_borrow_record` (`book_id`, `user_id`, `borrow_time`, `expected_return_time`, `actual_return_time`, `overdue_days`, `status`) VALUES
(5, 3, DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(1, 4, DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 0, 1),
(6, 5, DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY), 0, 1),
(22, 6, DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(21, 7, DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1),
(9, 8, DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 1),
(27, 9, DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), 0, 1),
(7, 10, DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(30, 11, DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 0, 1),
(10, 12, DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY), 0, 1),
(2, 3, DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(4, 4, DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), 0, 1),
(16, 5, DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1);

-- 第19天：6次
INSERT INTO `t_borrow_record` (`book_id`, `user_id`, `borrow_time`, `expected_return_time`, `actual_return_time`, `overdue_days`, `status`) VALUES
(8, 6, DATE_SUB(NOW(), INTERVAL 19 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 1),
(14, 7, DATE_SUB(NOW(), INTERVAL 19 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(18, 8, DATE_SUB(NOW(), INTERVAL 19 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(23, 9, DATE_SUB(NOW(), INTERVAL 19 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 0, 1),
(28, 10, DATE_SUB(NOW(), INTERVAL 19 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), 0, 1),
(5, 11, DATE_SUB(NOW(), INTERVAL 19 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1);

-- 第18天：11次（高峰）
INSERT INTO `t_borrow_record` (`book_id`, `user_id`, `borrow_time`, `expected_return_time`, `actual_return_time`, `overdue_days`, `status`) VALUES
(1, 3, DATE_SUB(NOW(), INTERVAL 18 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY), 0, 1),
(5, 4, DATE_SUB(NOW(), INTERVAL 18 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(6, 5, DATE_SUB(NOW(), INTERVAL 18 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(22, 6, DATE_SUB(NOW(), INTERVAL 18 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 0, 1),
(24, 7, DATE_SUB(NOW(), INTERVAL 18 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 1),
(2, 8, DATE_SUB(NOW(), INTERVAL 18 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1),
(29, 9, DATE_SUB(NOW(), INTERVAL 18 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY), 0, 1),
(8, 10, DATE_SUB(NOW(), INTERVAL 18 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(25, 11, DATE_SUB(NOW(), INTERVAL 18 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), 0, 1),
(21, 12, DATE_SUB(NOW(), INTERVAL 18 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(3, 3, DATE_SUB(NOW(), INTERVAL 18 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), 0, 1);

-- 第17天：5次
INSERT INTO `t_borrow_record` (`book_id`, `user_id`, `borrow_time`, `expected_return_time`, `actual_return_time`, `overdue_days`, `status`) VALUES
(11, 4, DATE_SUB(NOW(), INTERVAL 17 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1),
(17, 5, DATE_SUB(NOW(), INTERVAL 17 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 1),
(27, 6, DATE_SUB(NOW(), INTERVAL 17 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(7, 7, DATE_SUB(NOW(), INTERVAL 17 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(30, 8, DATE_SUB(NOW(), INTERVAL 17 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 0, 1);

-- 第16天：8次
INSERT INTO `t_borrow_record` (`book_id`, `user_id`, `borrow_time`, `expected_return_time`, `actual_return_time`, `overdue_days`, `status`) VALUES
(5, 9, DATE_SUB(NOW(), INTERVAL 16 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1),
(1, 10, DATE_SUB(NOW(), INTERVAL 16 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(6, 11, DATE_SUB(NOW(), INTERVAL 16 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), 0, 1),
(8, 12, DATE_SUB(NOW(), INTERVAL 16 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(22, 3, DATE_SUB(NOW(), INTERVAL 16 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), 0, 1),
(24, 4, DATE_SUB(NOW(), INTERVAL 16 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 0, 1),
(9, 5, DATE_SUB(NOW(), INTERVAL 16 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY), 0, 1),
(15, 6, DATE_SUB(NOW(), INTERVAL 16 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0);

-- 第15天：3次（低谷）
INSERT INTO `t_borrow_record` (`book_id`, `user_id`, `borrow_time`, `expected_return_time`, `actual_return_time`, `overdue_days`, `status`) VALUES
(29, 7, DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(21, 8, DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 1),
(5, 9, DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1);

-- 第14天：10次（高峰）
INSERT INTO `t_borrow_record` (`book_id`, `user_id`, `borrow_time`, `expected_return_time`, `actual_return_time`, `overdue_days`, `status`) VALUES
(1, 3, DATE_SUB(NOW(), INTERVAL 14 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 0, 1),
(5, 4, DATE_SUB(NOW(), INTERVAL 14 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(6, 5, DATE_SUB(NOW(), INTERVAL 14 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(8, 6, DATE_SUB(NOW(), INTERVAL 14 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), 0, 1),
(22, 7, DATE_SUB(NOW(), INTERVAL 14 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1),
(30, 8, DATE_SUB(NOW(), INTERVAL 14 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 1),
(2, 9, DATE_SUB(NOW(), INTERVAL 14 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), 0, 1),
(25, 10, DATE_SUB(NOW(), INTERVAL 14 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(26, 11, DATE_SUB(NOW(), INTERVAL 14 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY), 0, 1),
(4, 12, DATE_SUB(NOW(), INTERVAL 14 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1);

-- 第13天：7次
INSERT INTO `t_borrow_record` (`book_id`, `user_id`, `borrow_time`, `expected_return_time`, `actual_return_time`, `overdue_days`, `status`) VALUES
(10, 3, DATE_SUB(NOW(), INTERVAL 13 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 0, 1),
(14, 4, DATE_SUB(NOW(), INTERVAL 13 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 1),
(21, 5, DATE_SUB(NOW(), INTERVAL 13 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(24, 6, DATE_SUB(NOW(), INTERVAL 13 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(28, 7, DATE_SUB(NOW(), INTERVAL 13 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1),
(7, 8, DATE_SUB(NOW(), INTERVAL 13 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), 0, 1),
(3, 9, DATE_SUB(NOW(), INTERVAL 13 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), 0, 1);

-- 第12天：12次（高峰-周末）
INSERT INTO `t_borrow_record` (`book_id`, `user_id`, `borrow_time`, `expected_return_time`, `actual_return_time`, `overdue_days`, `status`) VALUES
(5, 3, DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(1, 4, DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(6, 5, DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 0, 1),
(8, 6, DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY), 0, 1),
(22, 7, DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1),
(23, 8, DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY), 0, 1),
(29, 9, DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(11, 10, DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), 0, 1),
(27, 11, DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(17, 12, DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 1),
(5, 4, DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), 0, 1),
(1, 5, DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1);

-- 第11天：4次
INSERT INTO `t_borrow_record` (`book_id`, `user_id`, `borrow_time`, `expected_return_time`, `actual_return_time`, `overdue_days`, `status`) VALUES
(30, 6, DATE_SUB(NOW(), INTERVAL 11 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 0, 1),
(18, 7, DATE_SUB(NOW(), INTERVAL 11 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(9, 8, DATE_SUB(NOW(), INTERVAL 11 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(15, 9, DATE_SUB(NOW(), INTERVAL 11 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY), 0, 1);

-- 第10天：9次
INSERT INTO `t_borrow_record` (`book_id`, `user_id`, `borrow_time`, `expected_return_time`, `actual_return_time`, `overdue_days`, `status`) VALUES
(5, 10, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1),
(1, 11, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), 0, 1),
(6, 12, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(22, 3, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(24, 4, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 1),
(2, 5, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), 0, 1),
(21, 6, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 0, 1),
(8, 7, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY), 0, 1),
(26, 8, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0);

-- 第9天：15次（高峰）
INSERT INTO `t_borrow_record` (`book_id`, `user_id`, `borrow_time`, `expected_return_time`, `actual_return_time`, `overdue_days`, `status`) VALUES
(5, 3, DATE_SUB(NOW(), INTERVAL 9 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1),
(1, 4, DATE_SUB(NOW(), INTERVAL 9 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(6, 5, DATE_SUB(NOW(), INTERVAL 9 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY), 0, 1),
(8, 6, DATE_SUB(NOW(), INTERVAL 9 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(22, 7, DATE_SUB(NOW(), INTERVAL 9 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 0, 1),
(23, 8, DATE_SUB(NOW(), INTERVAL 9 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 1),
(24, 9, DATE_SUB(NOW(), INTERVAL 9 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), 0, 1),
(25, 10, DATE_SUB(NOW(), INTERVAL 9 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), 0, 1),
(29, 11, DATE_SUB(NOW(), INTERVAL 9 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(30, 12, DATE_SUB(NOW(), INTERVAL 9 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(4, 3, DATE_SUB(NOW(), INTERVAL 9 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY), 0, 1),
(7, 4, DATE_SUB(NOW(), INTERVAL 9 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1),
(10, 5, DATE_SUB(NOW(), INTERVAL 9 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 1),
(14, 6, DATE_SUB(NOW(), INTERVAL 9 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 0, 1),
(27, 7, DATE_SUB(NOW(), INTERVAL 9 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0);

-- 第8天：6次
INSERT INTO `t_borrow_record` (`book_id`, `user_id`, `borrow_time`, `expected_return_time`, `actual_return_time`, `overdue_days`, `status`) VALUES
(3, 8, DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1),
(11, 9, DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), 0, 1),
(18, 10, DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(15, 11, DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(16, 12, DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY), 0, 1),
(9, 3, DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 0, 1);

-- 第7天：4次
INSERT INTO `t_borrow_record` (`book_id`, `user_id`, `borrow_time`, `expected_return_time`, `actual_return_time`, `overdue_days`, `status`) VALUES
(5, 4, DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(1, 5, DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 1),
(6, 6, DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(22, 7, DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1);

-- 第6天：11次（高峰-周末）
INSERT INTO `t_borrow_record` (`book_id`, `user_id`, `borrow_time`, `expected_return_time`, `actual_return_time`, `overdue_days`, `status`) VALUES
(5, 3, DATE_SUB(NOW(), INTERVAL 6 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 0, 1),
(1, 4, DATE_SUB(NOW(), INTERVAL 6 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(6, 5, DATE_SUB(NOW(), INTERVAL 6 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1),
(8, 6, DATE_SUB(NOW(), INTERVAL 6 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), 0, 1),
(22, 7, DATE_SUB(NOW(), INTERVAL 6 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), 0, 1),
(24, 8, DATE_SUB(NOW(), INTERVAL 6 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(23, 9, DATE_SUB(NOW(), INTERVAL 6 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 1),
(29, 10, DATE_SUB(NOW(), INTERVAL 6 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(30, 11, DATE_SUB(NOW(), INTERVAL 6 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 0, 1),
(2, 12, DATE_SUB(NOW(), INTERVAL 6 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY), 0, 1),
(21, 3, DATE_SUB(NOW(), INTERVAL 6 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1);

-- 第5天：7次
INSERT INTO `t_borrow_record` (`book_id`, `user_id`, `borrow_time`, `expected_return_time`, `actual_return_time`, `overdue_days`, `status`) VALUES
(7, 4, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(10, 5, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), 0, 1),
(14, 6, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(18, 7, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 0, 1),
(27, 8, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 1),
(25, 9, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1),
(11, 10, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), 0, 1);

-- 第4天：13次（高峰）
INSERT INTO `t_borrow_record` (`book_id`, `user_id`, `borrow_time`, `expected_return_time`, `actual_return_time`, `overdue_days`, `status`) VALUES
(5, 3, DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1),
(1, 4, DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(6, 5, DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 1),
(22, 6, DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(24, 7, DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 0, 1),
(21, 8, DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY), 0, 1),
(8, 9, DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), 0, 1),
(29, 10, DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1),
(23, 11, DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY), 0, 1),
(30, 12, DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(2, 3, DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(4, 4, DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), 0, 1),
(26, 5, DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 0, 1);

-- 第3天：5次
INSERT INTO `t_borrow_record` (`book_id`, `user_id`, `borrow_time`, `expected_return_time`, `actual_return_time`, `overdue_days`, `status`) VALUES
(9, 6, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1),
(15, 7, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(17, 8, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(3, 9, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 1),
(28, 10, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), 0, 1);

-- 第2天：9次
INSERT INTO `t_borrow_record` (`book_id`, `user_id`, `borrow_time`, `expected_return_time`, `actual_return_time`, `overdue_days`, `status`) VALUES
(5, 3, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), 0, 1),
(1, 4, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(6, 5, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1),
(8, 6, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 0, 1),
(22, 7, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 1),
(24, 8, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 1),
(21, 9, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), 0, 1),
(29, 10, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), NULL, 0, 0),
(30, 11, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 0 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 0, 1);

-- 昨天：8次
INSERT INTO `t_borrow_record` (`book_id`, `user_id`, `borrow_time`, `expected_return_time`, `actual_return_time`, `overdue_days`, `status`) VALUES
(5, 3, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_ADD(NOW(), INTERVAL 29 DAY), NULL, 0, 0),
(1, 4, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_ADD(NOW(), INTERVAL 29 DAY), NULL, 0, 0),
(6, 5, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_ADD(NOW(), INTERVAL 29 DAY), NULL, 0, 0),
(22, 6, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_ADD(NOW(), INTERVAL 29 DAY), NULL, 0, 0),
(24, 7, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_ADD(NOW(), INTERVAL 29 DAY), NULL, 0, 0),
(8, 8, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_ADD(NOW(), INTERVAL 29 DAY), NULL, 0, 0),
(23, 9, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_ADD(NOW(), INTERVAL 29 DAY), NULL, 0, 0),
(29, 10, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_ADD(NOW(), INTERVAL 29 DAY), NULL, 0, 0);

-- 今天：5次（借阅中）
INSERT INTO `t_borrow_record` (`book_id`, `user_id`, `borrow_time`, `expected_return_time`, `actual_return_time`, `overdue_days`, `status`) VALUES
(5, 11, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), NULL, 0, 0),
(1, 12, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), NULL, 0, 0),
(6, 3, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), NULL, 0, 0),
(22, 4, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), NULL, 0, 0),
(21, 5, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), NULL, 0, 0);

-- 补充用户画像标签
INSERT INTO `t_user_tag` (`user_id`, `tag_name`, `tag_type`, `create_time`) VALUES
(3, '文学爱好者', 'category', NOW()),
(3, '高频读者', 'frequency', NOW()),
(4, '科技发烧友', 'category', NOW()),
(5, '全栈开发者', 'category', NOW()),
(6, 'AI爱好者', 'category', NOW()),
(7, '设计师', 'category', NOW()),
(8, '艺术鉴赏家', 'category', NOW()),
(9, '历史迷', 'category', NOW()),
(10, '文学青年', 'category', NOW()),
(11, '程序员', 'category', NOW()),
(12, '教育工作者', 'category', NOW());
