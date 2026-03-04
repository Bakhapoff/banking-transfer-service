package kg.ab.transfer.model.payload.response;

import kg.ab.transfer.model.payload.response.inner.TransactionResponse;

import java.math.BigDecimal;
import java.util.List;

public record AccountResponse(
        String accountNumber,
        BigDecimal balance,
        List<TransactionResponse> transactions,
        Integer page,
        Integer totalTransactionPages,
        Long totalTransactions
) {
}