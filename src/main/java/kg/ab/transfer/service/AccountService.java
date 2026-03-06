package kg.ab.transfer.service;

import kg.ab.transfer.model.payload.response.AccountResponse;

import java.time.LocalDateTime;

public interface AccountService {

    AccountResponse filter(String accountNumber, LocalDateTime from,
                           LocalDateTime to, int page, int size);
}