package kg.ab.transfer.util;

import kg.ab.transfer.model.entity.Account;
import kg.ab.transfer.model.entity.Transaction;
import kg.ab.transfer.model.enums.OperationType;
import kg.ab.transfer.model.enums.TransactionStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionFactory {

    public Transaction buildDebit(Account fromAccount,
                                  Account toAccount,
                                  BigDecimal amount,
                                  BigDecimal balanceAfter,
                                  TransactionStatus status) {

        Transaction transaction = new Transaction();
        transaction.setOperationType(OperationType.DEBIT);
        transaction.setAmount(amount);
        transaction.setBalanceAfter(balanceAfter);
        transaction.setStatus(status);
        transaction.setAccount(fromAccount);
        transaction.setCounterpartAccount(toAccount);

        return transaction;
    }

    public Transaction buildCredit(Account fromAccount,
                                  Account toAccount,
                                  BigDecimal amount,
                                  BigDecimal balanceAfter,
                                  TransactionStatus status) {

        Transaction transaction = new Transaction();
        transaction.setOperationType(OperationType.CREDIT);
        transaction.setAmount(amount);
        transaction.setBalanceAfter(balanceAfter);
        transaction.setStatus(status);
        transaction.setAccount(toAccount);
        transaction.setCounterpartAccount(fromAccount);

        return transaction;
    }
}