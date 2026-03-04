package kg.ab.transfer.service.impl;

import kg.ab.transfer.exception.custom_exception.AccountNotFoundException;
import kg.ab.transfer.model.entity.Account;
import kg.ab.transfer.model.entity.Transaction;
import kg.ab.transfer.model.payload.response.AccountResponse;
import kg.ab.transfer.model.payload.response.inner.TransactionResponse;
import kg.ab.transfer.repository.AccountRepository;
import kg.ab.transfer.repository.TransactionRepository;
import kg.ab.transfer.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public AccountResponse filter(String accountNumber, LocalDateTime fromDate,
                                  LocalDateTime toDate, int page, int size) {

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Transaction> transactionPage = transactionRepository
                .findByAccountNumberAndDateRange(accountNumber, fromDate, toDate, pageable);

        List<TransactionResponse> transactions = transactionPage.getContent().stream()
                .map(transaction -> new TransactionResponse(
                        transaction.getCreatedAt(),
                        transaction.getOperationType(),
                        transaction.getAmount(),
                        transaction.getBalanceAfter()
                )).toList();

        return new AccountResponse(
                account.getAccountNumber(),
                account.getBalance(),
                transactions,
                transactionPage.getNumber(),
                transactionPage.getTotalPages(),
                transactionPage.getTotalElements()
        );
    }
}