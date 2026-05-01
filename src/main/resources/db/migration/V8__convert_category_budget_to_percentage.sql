-- 1. Kategorie-Budgets ohne gültiges Gesamtbudget entfernen
DELETE FROM category_budget_limits
WHERE category_id IN (
    SELECT c.id
    FROM categories c
    LEFT JOIN budget_settings b ON b.user_id = c.user_id
    WHERE b.id IS NULL
       OR b.total_monthly_limit IS NULL
       OR b.total_monthly_limit <= 0
);

-- 2. Neue Spalte hinzufügen
ALTER TABLE category_budget_limits ADD COLUMN percentage NUMERIC(5,2);

-- 3. Werte umrechnen
UPDATE category_budget_limits cbl
SET percentage = ROUND(cbl.monthly_limit / b.total_monthly_limit * 100, 2)
FROM categories c, budget_settings b
WHERE cbl.category_id = c.id
  AND b.user_id = c.user_id;

-- 4. Einzelwerte > 100 hart auf 100 cappen
UPDATE category_budget_limits SET percentage = 100.00 WHERE percentage > 100.00;

-- 5. Spalte verpflichtend machen, Altspalte entfernen
ALTER TABLE category_budget_limits ALTER COLUMN percentage SET NOT NULL;
ALTER TABLE category_budget_limits DROP COLUMN monthly_limit;
