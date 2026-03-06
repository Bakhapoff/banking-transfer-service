package kg.ab.transfer.service.impl;

import kg.ab.transfer.exception.custom_exception.AccountBlockedException;
import kg.ab.transfer.exception.custom_exception.AccountNotFoundException;
import kg.ab.transfer.exception.custom_exception.InvalidDateRangeException;
import kg.ab.transfer.model.entity.Account;
import kg.ab.transfer.model.entity.Transaction;
import kg.ab.transfer.model.enums.AccountStatus;
import kg.ab.transfer.model.payload.response.AccountResponse;
import kg.ab.transfer.model.payload.response.inner.TransactionResponse;
import kg.ab.transfer.repository.AccountRepository;
import kg.ab.transfer.repository.TransactionRepository;
import kg.ab.transfer.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional(readOnly = true)
    public AccountResponse filter(String accountNumber, LocalDateTime from,
                                  LocalDateTime to, int page, int size) {

        log.debug("Statement requested: accountNumber={}, from={}, to={}, page={}, size={}",
                accountNumber, from, to, page, size);

        if (from != null && to != null && from.isAfter(to)) {
            log.warn("Invalid date range: from={} is after to={}, accountNumber={}", from, to, accountNumber);
            throw new InvalidDateRangeException("<from> must be before <to>");
        }

        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> {
            log.warn("Account not found: accountNumber={}", accountNumber);
            return new AccountNotFoundException("Account not found: " + accountNumber);
        });

        validateAccountStatus(account);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Transaction> transactionPage = transactionRepository
                .findByAccountNumberAndDateRange(accountNumber, from, to, pageable);

        List<TransactionResponse> transactions = transactionPage.getContent().stream()
                .map(transaction -> new TransactionResponse(
                        transaction.getCreatedAt(),
                        transaction.getOperationType(),
                        transaction.getAmount(),
                        transaction.getBalanceAfter()
                )).toList();

        log.debug("Statement fetched: accountNumber={}, totalTransactions={}, page={}/{}",
                accountNumber, transactionPage.getTotalElements(), page, transactionPage.getTotalPages());
        return new AccountResponse(
                account.getAccountNumber(),
                account.getBalance(),
                transactions,
                transactionPage.getNumber(),
                transactionPage.getTotalPages(),
                transactionPage.getTotalElements()
        );
    }

    private void validateAccountStatus(Account account) {
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountBlockedException("Account " + account.getAccountNumber() + " is blocked");
        }
    }
}