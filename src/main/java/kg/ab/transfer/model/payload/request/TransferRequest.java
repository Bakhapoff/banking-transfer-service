package kg.ab.transfer.model.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.DecimalMax;
import kg.ab.transfer.validation.DifferentAccounts;

import java.math.BigDecimal;

@DifferentAccounts
public record TransferRequest(

        @NotBlank(message = "<fromAccountNumber> must not be null or blank.")
        @Pattern(regexp = "[1-9][0-9]{9}",
                message = "<fromAccountNumber> must be a 10-digit number starting with non-zero.")
        String fromAccountNumber,

        @NotBlank(message = "<toAccountNumber> must not be null or blank.")
        @Pattern(regexp = "[1-9][0-9]{9}",
                message = "<toAccountNumber> must be a 10-digit number starting with non-zero.")
        String toAccountNumber,

        @NotNull(message = "<amount> must not be null.")
        @Positive(message = "<amount> must be positive.")
        @Digits(integer = 6, fraction = 2,
                message = "<amount> must contain no more than 2 decimal places and no more than 6 digits before the decimal point.")
        @DecimalMax(value = "100000.00", message = "<amount> must not exceed 100000.00.")
        BigDecimal amount
) {
}