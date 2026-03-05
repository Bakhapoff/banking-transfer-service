package kg.ab.transfer.service;

import kg.ab.transfer.model.entity.Account;

import java.math.BigDecimal;

public interface TransactionAuditService {

    void saveFailedTransfer(Account fromAccount, Account toAccount, BigDecimal amount);
}