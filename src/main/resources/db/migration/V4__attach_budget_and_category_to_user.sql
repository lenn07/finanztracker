ALTER TABLE categories
    ADD COLUMN user_id BIGINT;

ALTER TABLE budget_settings
    ADD COLUMN user_id BIGINT;

ALTER TABLE categories
    ADD CONSTRAINT fk_categories_user
        FOREIGN KEY (user_id)
        REFERENCES app_users(id)
        ON DELETE CASCADE;

ALTER TABLE budget_settings
    ADD CONSTRAINT fk_budget_settings_user
        FOREIGN KEY (user_id)
        REFERENCES app_users(id)
        ON DELETE CASCADE;

ALTER TABLE categories
    DROP CONSTRAINT IF EXISTS categories_name_key;

CREATE UNIQUE INDEX IF NOT EXISTS ux_categories_user_lower_name
    ON categories (user_id, lower(name))
    WHERE user_id IS NOT NULL;

CREATE UNIQUE INDEX IF NOT EXISTS ux_budget_settings_user
    ON budget_settings (user_id)
    WHERE user_id IS NOT NULL;
