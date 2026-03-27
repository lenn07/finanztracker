CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    amount NUMERIC(12, 2) NOT NULL,
    type VARCHAR(20) NOT NULL,
    category_id BIGINT NOT NULL,
    date DATE NOT NULL,
    note TEXT,

    CONSTRAINT fk_transaction_category
        FOREIGN KEY (category_id)
        REFERENCES categories(id),

    CONSTRAINT chk_transaction_type
        CHECK (type IN ('INCOME', 'EXPENSE'))
);