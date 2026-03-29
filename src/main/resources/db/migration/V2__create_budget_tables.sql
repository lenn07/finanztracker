CREATE TABLE budget_settings (
    id BIGSERIAL PRIMARY KEY,
    total_monthly_limit NUMERIC(12, 2) NOT NULL
);

CREATE TABLE category_budget_limits (
    id BIGSERIAL PRIMARY KEY,
    category_id BIGINT NOT NULL UNIQUE,
    monthly_limit NUMERIC(12, 2) NOT NULL,

    CONSTRAINT fk_category_budget_limits_category
        FOREIGN KEY (category_id)
        REFERENCES categories(id)
        ON DELETE CASCADE
);