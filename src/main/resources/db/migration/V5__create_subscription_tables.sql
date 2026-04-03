CREATE TABLE subscriptions (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    amount NUMERIC(12, 2) NOT NULL,
    type VARCHAR(20) NOT NULL,
    category_id BIGINT NOT NULL,
    billing_interval VARCHAR(20) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    note TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_subscriptions_category
        FOREIGN KEY (category_id)
        REFERENCES categories(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_subscriptions_user
        FOREIGN KEY (user_id)
        REFERENCES app_users(id)
        ON DELETE CASCADE
);

ALTER TABLE transactions
    ADD COLUMN subscription_id BIGINT;

ALTER TABLE transactions
    ADD CONSTRAINT fk_transactions_subscription
        FOREIGN KEY (subscription_id)
        REFERENCES subscriptions(id)
        ON DELETE SET NULL;

