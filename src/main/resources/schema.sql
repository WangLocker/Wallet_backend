CREATE TABLE IF NOT EXISTS users (
                                     id INTEGER PRIMARY KEY AUTOINCREMENT,          -- 用户ID
                                     name TEXT NOT NULL,                            -- 用户姓名
                                     ssn TEXT UNIQUE NOT NULL,                      -- SSN (必须唯一)
                                     phone TEXT UNIQUE NOT NULL,                    -- 电话号码 (必须唯一)
                                     email TEXT UNIQUE NOT NULL,                    -- 邮箱 (必须唯一)
                                     password TEXT NOT NULL,
                                     is_email_verified BOOLEAN DEFAULT 0,          -- 邮箱是否已验证
                                     is_phone_verified BOOLEAN DEFAULT 0           -- 电话是否已验证
);
CREATE TABLE IF NOT EXISTS bank_accounts (
                                             id INTEGER PRIMARY KEY AUTOINCREMENT,         -- 银行账户ID
                                             user_id INTEGER NOT NULL,                     -- 所属用户ID
                                             bank_id TEXT NOT NULL,                        -- 银行ID
                                             account_number TEXT NOT NULL,                 -- 银行账户号
                                             is_verified BOOLEAN DEFAULT 0,               -- 是否已验证
                                             is_primary BOOLEAN DEFAULT 0,                -- 是否为主要账户
                                             FOREIGN KEY (user_id) REFERENCES users(id)    -- 外键，关联用户
);
CREATE TABLE IF NOT EXISTS payments (
                                        id INTEGER PRIMARY KEY AUTOINCREMENT,         -- 支付记录ID
                                        sender_id INTEGER NOT NULL,                   -- 付款人ID
                                        recipient_id INTEGER,                         -- 收款人ID (可为空)
                                        recipient_email_or_phone TEXT,                -- 收款人邮箱或电话（当未注册 WALLET 时使用）
                                        amount REAL NOT NULL,                         -- 支付金额
                                        memo TEXT,                                    -- 支付备注
                                        status TEXT NOT NULL,                         -- 支付状态: 'completed', 'cancelled'
                                        initiated_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- 支付发起时间
                                        completed_at DATETIME,                        -- 支付完成时间
                                        cancellation_reason TEXT,                     -- 取消原因
                                        FOREIGN KEY (sender_id) REFERENCES users(id), -- 外键，关联付款人
    FOREIGN KEY (recipient_id) REFERENCES users(id) -- 外键，关联收款人
);
CREATE TABLE IF NOT EXISTS requests (
                                        id INTEGER PRIMARY KEY AUTOINCREMENT,         -- 请求记录ID
                                        requester_id INTEGER NOT NULL,                -- 请求发起人ID
                                        recipient_id INTEGER,                         -- 请求接收人ID (可为空)
                                        recipient_email_or_phone TEXT,                -- 接收人邮箱或电话（当未注册 WALLET 时使用）
                                        amount REAL NOT NULL,                         -- 请求金额
                                        memo TEXT,                                    -- 请求备注
                                        status TEXT NOT NULL,                         -- 请求状态: 'pending', 'completed'
                                        initiated_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- 请求发起时间
                                        completed_at DATETIME,                        -- 请求完成时间
                                        FOREIGN KEY (requester_id) REFERENCES users(id), -- 外键，关联请求发起人
    FOREIGN KEY (recipient_id) REFERENCES users(id)  -- 外键，关联请求接收人
);
CREATE TABLE IF NOT EXISTS statements (
                                          id INTEGER PRIMARY KEY AUTOINCREMENT,         -- 对账单ID
                                          user_id INTEGER NOT NULL,                     -- 所属用户ID
                                          month TEXT NOT NULL,                          -- 月份 (格式: YYYY-MM)
                                          total_sent REAL DEFAULT 0,                    -- 当月发送金额总和
                                          total_received REAL DEFAULT 0,                -- 当月接收金额总和
                                          FOREIGN KEY (user_id) REFERENCES users(id)    -- 外键，关联用户
);



