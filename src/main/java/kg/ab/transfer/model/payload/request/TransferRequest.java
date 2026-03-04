package kg.ab.transfer.model.payload.request;

import java.math.BigDecimal;

public record TransferRequest(
        String fromAccountNumber,
        String toAccountNumber,
        BigDecimal amount
) {
}
