package kg.ab.transfer.service.impl;

import kg.ab.transfer.model.entity.Account;
import kg.ab.transfer.model.entity.Transaction;
import kg.ab.transfer.model.enums.OperationType;
import kg.ab.transfer.repository.TransactionRepository;
import kg.ab.transfer.service.TransactionAuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionAuditServiceImpl implements TransactionAuditService {

    private final TransactionRepository transactionRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveFailedTransfer(Account fromAccount, Account toAccount, BigDecimal amount) {
        Transaction failed = Transaction.failed(fromAccount, toAccount, amount, OperationType.DEBIT);
        try {
            transactionRepository.save(failed);
        } catch (Exception e) {
            log.error("Failed to save failed transaction: fromAccount={}, toAccount={}, amount={}, error={}",
                    fromAccount.getAccountNumber(), toAccount.getAccountNumber(), amount, e.getMessage(), e);
            throw e;
        }
    }
}