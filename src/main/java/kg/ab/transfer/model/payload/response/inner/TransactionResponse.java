package kg.ab.transfer.model.payload.response.inner;

import kg.ab.transfer.model.enums.OperationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        LocalDateTime date,
        OperationType operationType,
        BigDecimal amount,
        BigDecimal balanceAfter
) {
}