CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    ssn TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS user_emails (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    email TEXT UNIQUE NOT NULL,
    is_verified BOOLEAN DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS user_phone (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    phone TEXT UNIQUE NOT NULL,
    is_verified BOOLEAN DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS bank_accounts (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    bank_id TEXT NOT NULL,
    account_number TEXT NOT NULL UNIQUE,
    is_verified BOOLEAN DEFAULT 0,
    is_primary BOOLEAN DEFAULT 0,
    money REAL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS payments (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    sender_id INTEGER NOT NULL,
    recipient_id INTEGER,
    recipient_email_or_phone TEXT CHECK (length(recipient_email_or_phone) <= 100),
    recipient_type TEXT CHECK (recipient_type IN ('email', 'phone')),
    amount REAL NOT NULL CHECK (amount > 0),
    sender_account_number TEXT,
    recipient_account_number TEXT,
    memo TEXT,
    status TEXT NOT NULL DEFAULT 'pending' CHECK (status IN ('completed', 'cancelled', 'pending')),
    initiated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    completed_at DATETIME,
    cancellation_reason TEXT,
    FOREIGN KEY (sender_id) REFERENCES users(id),
    FOREIGN KEY (recipient_id) REFERENCES users(id)
);


CREATE TABLE IF NOT EXISTS requests (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    requester_id INTEGER NOT NULL,
    recipient_id INTEGER,
    recipient_email_or_phone TEXT CHECK (length(recipient_email_or_phone) <= 100),
    amount REAL NOT NULL CHECK (amount > 0),
    total_amount REAL NOT NULL CHECK (total_amount > 0),
    requester_account_number TEXT,
    recipient_account_number TEXT,
    memo TEXT,
    status TEXT NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'completed', 'cancelled')),
    initiated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    completed_at DATETIME,
    FOREIGN KEY (requester_id) REFERENCES users(id),
    FOREIGN KEY (recipient_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS statements (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    month TEXT NOT NULL,
    total_sent REAL DEFAULT 0 CHECK (total_sent >= 0),
    total_received REAL DEFAULT 0 CHECK (total_received >= 0),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX IF NOT EXISTS idx_user_email ON user_emails(email);
CREATE INDEX IF NOT EXISTS idx_user_phone ON user_phone(phone);
CREATE INDEX IF NOT EXISTS idx_bank_account ON bank_accounts(account_number);
