package kg.ab.transfer.validation;

import kg.ab.transfer.exception.custom_exception.AccountBlockedException;
import kg.ab.transfer.exception.custom_exception.InsufficientFundsException;
import kg.ab.transfer.model.entity.Account;
import kg.ab.transfer.model.enums.AccountStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransferValidator {

    public void validate(Account fromAccount, Account toAccount, BigDecimal amount) {
        validateAccountStatus(fromAccount);
        validateAccountStatus(toAccount);
        validateSufficientBalance(fromAccount, amount);
    }

    private void validateAccountStatus(Account account) {
        if (account.getStatus() == AccountStatus.BLOCKED) {
            throw new AccountBlockedException("Account " + account.getAccountNumber() + " is blocked");
        }
    }

    private void validateSufficientBalance(Account fromAccount, BigDecimal amount) {
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds on account " + fromAccount.getAccountNumber() +
                    ". Current balance: " + fromAccount.getBalance());
        }
    }
}