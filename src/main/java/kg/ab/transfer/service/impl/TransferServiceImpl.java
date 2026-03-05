package kg.ab.transfer.service.impl;

import kg.ab.transfer.exception.custom_exception.AccountBlockedException;
import kg.ab.transfer.exception.custom_exception.AccountNotFoundException;
import kg.ab.transfer.model.entity.Account;
import kg.ab.transfer.model.entity.Transaction;
import kg.ab.transfer.model.enums.AccountStatus;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public TransferResponse transfer(TransferRequest request) {
        Map<String, Account> accounts = accountRepository.findAllByAccountNumberInForUpdate(
                        List.of(request.fromAccountNumber(), request.toAccountNumber()))
                .stream()
                .collect(Collectors.toMap(Account::getAccountNumber, a -> a));

        Account fromAccount = getAccount(accounts, request.fromAccountNumber());
        Account toAccount   = getAccount(accounts, request.toAccountNumber());

        validate(fromAccount, toAccount);

        BigDecimal balanceAfterFrom = fromAccount.debit(request.amount());
        BigDecimal balanceAfterTo   = toAccount.credit(request.amount());

        Transaction debitTx  = Transaction.debit(fromAccount, toAccount, request.amount(), balanceAfterFrom);
        Transaction creditTx = Transaction.credit(fromAccount, toAccount, request.amount(), balanceAfterTo);

        transactionRepository.saveAll(List.of(debitTx, creditTx));

        return new TransferResponse(
                request.fromAccountNumber(),
                request.toAccountNumber(),
                request.amount(),
                TransactionStatus.SUCCESS
        );
    }

    private Account getAccount(Map<String, Account> accounts, String accountNumber) {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException("Account not found: " + accountNumber);
        }
        return account;
    }

    private void validate(Account fromAccount, Account toAccount) {
        validateAccountStatus(fromAccount);
        validateAccountStatus(toAccount);
    }

    private void validateAccountStatus(Account account) {
        if (account.getStatus() == AccountStatus.BLOCKED) {
            throw new AccountBlockedException("Account " + account.getAccountNumber() + " is blocked");
        }
    }
}