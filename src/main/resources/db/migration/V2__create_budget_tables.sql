CREATE TABLE monthly_budgets (
    id BIGSERIAL PRIMARY KEY,
    year INT NOT NULL,
    month INT NOT NULL,
    total_limit NUMERIC(12, 2) NOT NULL,

    CONSTRAINT uq_monthly_budget_year_month
        UNIQUE (year, month),

    CONSTRAINT chk_monthly_budget_month
        CHECK (month BETWEEN 1 AND 12),

    CONSTRAINT chk_monthly_budget_total_limit
        CHECK (total_limit >= 0)
);

CREATE TABLE category_budgets (
    id BIGSERIAL PRIMARY KEY,
    monthly_budget_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    limit_amount NUMERIC(12, 2) NOT NULL,

    CONSTRAINT fk_category_budget_monthly_budget
        FOREIGN KEY (monthly_budget_id)
        REFERENCES monthly_budgets(id),
        -- ON DELETE CASCADE,

    CONSTRAINT fk_category_budget_category
        FOREIGN KEY (category_id)
        REFERENCES categories(id),
        -- ON DELETE CASCADE,

    CONSTRAINT uq_category_budget_monthly_budget_category
        UNIQUE (monthly_budget_id, category_id),

    CONSTRAINT chk_category_budget_limit_amount
        CHECK (limit_amount >= 0)
);