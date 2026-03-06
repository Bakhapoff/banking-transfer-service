-- 1. TOP 5 accounts by the number of transactions over the last month
SELECT a.account_number, COUNT(t.id) AS transaction_count
FROM transactions t
         JOIN accounts a ON a.id = t.account_id
WHERE t.created_at >= DATE_TRUNC('month', CURRENT_DATE)
  AND t.created_at < DATE_TRUNC('month', CURRENT_DATE) + INTERVAL '1 month'
GROUP BY a.account_number
ORDER BY transaction_count DESC
    LIMIT 5;


-- 2. The total amount of transfers for the selected period
SELECT COALESCE(SUM(t.amount), 0) AS total_amount
FROM transactions t
WHERE t.operation_type = 'DEBIT'
  AND t.status = 'SUCCESS'
  AND t.created_at >= '2026-03-04'
  AND t.created_at < '2026-03-06';

-- 3. Accounts with a negative balance
SELECT a.account_number, a.balance FROM accounts a
WHERE a.balance < 0
ORDER BY a.account_number DESC;

-- 4. Optimized the first query of the TOP 5 accounts by the number of transactions over the last month,
-- because with the index, we quickly find the necessary rows without scanning the entire table,
-- especially transactions with a large number of records
-- we have a JOIN by account_id, sorted by date, and we get the latest transactions by DESC
CREATE INDEX IF NOT EXISTS idx_transactions_account_created
    ON transactions (account_id, created_at DESC);