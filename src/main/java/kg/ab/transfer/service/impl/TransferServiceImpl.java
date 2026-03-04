package kg.ab.transfer.service.impl;

import kg.ab.transfer.exception.custom_exception.AccountNotFoundException;
import kg.ab.transfer.model.entity.Account;
import kg.ab.transfer.model.entity.Transaction;
import kg.ab.transfer.model.enums.TransactionStatus;
import kg.ab.transfer.model.payload.request.TransferRequest;
import kg.ab.transfer.model.payload.response.TransferResponse;
import kg.ab.transfer.repository.AccountRepository;
import kg.ab.transfer.repository.TransactionRepository;
import kg.ab.transfer.service.TransferService;
import kg.ab.transfer.util.TransactionFactory;
import kg.ab.transfer.validation.TransferValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransferValidator transferValidator;
    private final TransactionFactory transactionFactory;

    @Override
    @Transactional
    public TransferResponse transfer(TransferRequest request) {
        Account fromAccount = accountRepository.findByAccountNumber(request.fromAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + request.fromAccountNumber()));

        Account toAccount = accountRepository.findByAccountNumber(request.toAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + request.fromAccountNumber()));

        transferValidator.validate(fromAccount, toAccount, request.amount());

        BigDecimal balanceAfterFrom = fromAccount.getBalance().subtract(request.amount());
        BigDecimal balanceAfterTo = toAccount.getBalance().add(request.amount());

        fromAccount.setBalance(balanceAfterFrom);
        toAccount.setBalance(balanceAfterTo);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transactionFrom = transactionFactory.buildDebit(
                fromAccount, toAccount, request.amount(), balanceAfterFrom, TransactionStatus.SUCCESS);

        Transaction transactionTo = transactionFactory.buildCredit(
                fromAccount, toAccount, request.amount(), balanceAfterTo, TransactionStatus.SUCCESS);

        transactionRepository.save(transactionFrom);
        transactionRepository.save(transactionTo);

        return new TransferResponse(
                request.fromAccountNumber(),
                request.toAccountNumber(),
                request.amount(),
                TransactionStatus.SUCCESS
        );
    }
}