package kg.ab.transfer.util;

import kg.ab.transfer.model.entity.Account;
import kg.ab.transfer.model.enums.AccountStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransferValidatorUtil {

    public void validate(Account fromAccount, Account toAccount, BigDecimal amount) {
        validateAmount(amount);
        validateAccountStatus(fromAccount);
        validateAccountStatus(toAccount);
        validateSufficientBalance(fromAccount, amount);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }
    }

    private void validateAccountStatus(Account account) {
        if (account.getStatus() == AccountStatus.BLOCKED) {
            throw new RuntimeException("Account " + account.getAccountNumber() + " is blocked");
        }
    }

    private void validateSufficientBalance(Account fromAccount, BigDecimal amount) {
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds on account " + fromAccount.getAccountNumber());
        }
    }
}