package kg.ab.transfer.service.impl;

import kg.ab.transfer.model.entity.Account;
import kg.ab.transfer.model.entity.Transaction;
import kg.ab.transfer.model.enums.OperationType;
import kg.ab.transfer.model.enums.TransactionStatus;
import kg.ab.transfer.model.payload.request.TransferRequest;
import kg.ab.transfer.model.payload.response.TransferResponse;
import kg.ab.transfer.repository.AccountRepository;
import kg.ab.transfer.repository.TransactionRepository;
import kg.ab.transfer.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public TransferResponse transfer(TransferRequest request) {
        Optional<Account> fromAccount = accountRepository.findByAccountNumber(request.fromAccountNumber());
        Optional<Account> toAccount = accountRepository.findByAccountNumber(request.toAccountNumber());

        if (fromAccount.isPresent() && toAccount.isPresent()) {
            Account fromAccountEntity = fromAccount.get();
            Account toAccountEntity = toAccount.get();

            BigDecimal balanceAfterFromAccount = fromAccountEntity.getBalance().subtract(request.amount());
            BigDecimal balanceAfterToAccount = toAccountEntity.getBalance().add(request.amount());

            fromAccountEntity.setBalance(balanceAfterFromAccount);
            toAccountEntity.setBalance(balanceAfterToAccount);

            Transaction transactionFromAccount = new Transaction();
            transactionFromAccount.setOperationType(OperationType.DEBIT);
            transactionFromAccount.setAmount(request.amount());
            transactionFromAccount.setBalanceAfter(balanceAfterFromAccount);
            transactionFromAccount.setStatus(TransactionStatus.SUCCESS);
            transactionFromAccount.setAccount(fromAccountEntity);
            transactionFromAccount.setCounterpartAccount(toAccountEntity);

            Transaction transactionToAccount = new Transaction();
            transactionToAccount.setOperationType(OperationType.CREDIT);
            transactionToAccount.setAmount(request.amount());
            transactionToAccount.setBalanceAfter(balanceAfterToAccount);
            transactionToAccount.setStatus(TransactionStatus.SUCCESS);
            transactionToAccount.setAccount(toAccountEntity);
            transactionToAccount.setCounterpartAccount(fromAccountEntity);

            accountRepository.save(fromAccountEntity);
            accountRepository.save(toAccountEntity);

            transactionRepository.save(transactionFromAccount);
            transactionRepository.save(transactionToAccount);

            return new TransferResponse(
                    request.fromAccountNumber(),
                    request.toAccountNumber(),
                    request.amount(),
                    TransactionStatus.SUCCESS
            );

        } else {
            throw new RuntimeException("Unknown error");
        }
    }
}