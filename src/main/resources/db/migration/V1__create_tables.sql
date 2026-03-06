CREATE TABLE users
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL,
    passport_number VARCHAR(255) NOT NULL,
    mail VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE accounts
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    account_number VARCHAR(255) NOT NULL UNIQUE,
    balance NUMERIC(19, 2) NOT NULL,
    status VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_accounts_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE transactions
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    operation_type VARCHAR(255) NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    balance_after NUMERIC(19, 2) NOT NULL,
    status VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    account_id BIGINT NOT NULL,
    counterpart_account_id BIGINT NOT NULL,
    CONSTRAINT fk_transactions_account FOREIGN KEY (account_id) REFERENCES accounts(id),
    CONSTRAINT fk_transactions_counterpart FOREIGN KEY (counterpart_account_id) REFERENCES accounts(id)
);