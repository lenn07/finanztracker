ALTER TABLE transactions
    DROP CONSTRAINT fk_transaction_category;

ALTER TABLE transactions
    ADD CONSTRAINT fk_transaction_category
        FOREIGN KEY (category_id)
        REFERENCES categories(id)
        ON DELETE CASCADE;
