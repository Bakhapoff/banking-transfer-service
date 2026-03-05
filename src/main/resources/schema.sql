-- 1. ТОП-5 счетов по количеству транзакций за последний месяц
SELECT a.account_number, COUNT(t.id) AS transaction_count
FROM transactions t
         JOIN accounts a ON a.id = t.account_id
WHERE t.created_at >= DATE_TRUNC('month', CURRENT_DATE)
  AND t.created_at < DATE_TRUNC('month', CURRENT_DATE) + INTERVAL '1 month'
GROUP BY a.account_number
ORDER BY transaction_count DESC
    LIMIT 5;


-- 2. Общую сумма переводов за выбранный период
SELECT COALESCE(SUM(t.amount), 0) AS total_amount
FROM transactions t
WHERE t.operation_type = 'DEBIT'
  AND t.status = 'SUCCESS'
  AND t.created_at >= '2026-03-04'
  AND t.created_at < '2026-03-06';

-- 3. Счета с отрицательным балансом
SELECT a.account_number, a.balance FROM accounts a
WHERE a.balance < 0
ORDER BY a.account_number DESC;

-- 4. Оптимизировал первый запрос ТОП-5 счетов по количеству транзакций за последний месяц,
-- т.к. с индексом мы быстро находим нужные строки, не сканируя всю таблицу,
-- особенно transactions c большим количеством записей
-- у нас есть JOIN по account_id, сортировка по дате, по DESC мы получаем последние транзакции
CREATE INDEX IF NOT EXISTS idx_transactions_account_created
    ON transactions (account_id, created_at DESC);