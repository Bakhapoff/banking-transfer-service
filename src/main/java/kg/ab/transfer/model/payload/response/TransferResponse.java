package kg.ab.transfer.model.payload.response;

import kg.ab.transfer.model.enums.TransactionStatus;

import java.math.BigDecimal;

public record TransferResponse(
        String fromAccountNumber,
        String toAccountNumber,
        BigDecimal amount,
        TransactionStatus status
) {
}