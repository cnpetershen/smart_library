-- 智慧图书馆数据库表结构

-- 用户表
CREATE TABLE IF NOT EXISTS `t_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `account` VARCHAR(50) NOT NULL COMMENT '账号',
    `password` VARCHAR(255) NOT NULL COMMENT '密码',
    `name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `role` VARCHAR(20) NOT NULL DEFAULT 'reader' COMMENT '角色：admin-管理员，reader-读者',
    `gender` VARCHAR(10) COMMENT '性别',
    `phone` VARCHAR(20) COMMENT '手机号',
    `email` VARCHAR(100) COMMENT '邮箱',
    `avatar` VARCHAR(255) COMMENT '头像路径',
    `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：1正常，0禁用',
    `register_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_account` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 图书表
CREATE TABLE IF NOT EXISTS `t_book` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `title` VARCHAR(200) NOT NULL COMMENT '书名',
    `author` VARCHAR(100) NOT NULL COMMENT '作者',
    `isbn` VARCHAR(50) COMMENT 'ISBN',
    `category` VARCHAR(50) NOT NULL COMMENT '分类：文学/科技/教育/艺术/其他',
    `publish_date` DATE COMMENT '出版日期',
    `cover_url` VARCHAR(255) COMMENT '封面路径',
    `total_stock` INT NOT NULL DEFAULT 0 COMMENT '总库存',
    `available_stock` INT NOT NULL DEFAULT 0 COMMENT '可借库存',
    `status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '状态：0可借，1已空',
    `description` TEXT COMMENT '图书描述',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    PRIMARY KEY (`id`),
    KEY `idx_category` (`category`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='图书表';

-- 借阅记录表
CREATE TABLE IF NOT EXISTS `t_borrow_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `book_id` BIGINT NOT NULL COMMENT '图书ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `borrow_time` DATETIME NOT NULL COMMENT '借阅时间',
    `expected_return_time` DATETIME NOT NULL COMMENT '预计归还时间',
    `actual_return_time` DATETIME COMMENT '实际归还时间',
    `overdue_days` INT NOT NULL DEFAULT 0 COMMENT '逾期天数',
    `status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '状态：0借阅中，1已归还',
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    PRIMARY KEY (`id`),
    KEY `idx_book_id` (`book_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='借阅记录表';

-- 图书分类统计表
CREATE TABLE IF NOT EXISTS `t_book_category_stat` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `category` VARCHAR(50) NOT NULL COMMENT '分类',
    `borrow_count` INT NOT NULL DEFAULT 0 COMMENT '借阅次数',
    `stat_date` DATE NOT NULL COMMENT '统计日期',
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_category_date` (`category`, `stat_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='图书分类统计表';

-- 用户画像标签表
CREATE TABLE IF NOT EXISTS `t_user_tag` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `tag_name` VARCHAR(50) NOT NULL COMMENT '标签名',
    `tag_type` VARCHAR(20) NOT NULL COMMENT '标签类型：category-分类偏好，frequency-借阅频率',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户画像标签表';

-- AI报告表
CREATE TABLE IF NOT EXISTS `t_ai_report` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `report_type` VARCHAR(20) NOT NULL COMMENT '报告类型：monthly-月度报告',
    `title` VARCHAR(200) NOT NULL DEFAULT '' COMMENT '报告标题',
    `report_title` VARCHAR(200) NOT NULL COMMENT '报告副标题',
    `report_content` LONGTEXT COMMENT '报告内容(Markdown)',
    `generate_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI报告表';
