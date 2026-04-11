ALTER TABLE budget_settings
    ADD COLUMN rollover_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN rollover_start_month DATE;
