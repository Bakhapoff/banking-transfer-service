package kg.ab.transfer.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import kg.ab.transfer.model.payload.response.AccountResponse;
import kg.ab.transfer.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Validated
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{accountNumber}/statement")
    public ResponseEntity<AccountResponse> getStatement(
            @PathVariable
            @NotBlank(message = "<fromAccountNumber> must not be null or blank.")
            @Pattern(regexp = "[1-9][0-9]{9}",
                    message = "<fromAccountNumber> must be a 10-digit number starting with non-zero.")
            String accountNumber,
            @RequestParam(required = false) LocalDateTime fromDate,
            @RequestParam(required = false) LocalDateTime toDate,
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "<page> must be >= 0") int page,
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "<size> must be between 1 and 50")
            @Max(value = 50, message = "<size> must be between 1 and 50")
            int size) {
        AccountResponse response = accountService.filter(accountNumber, fromDate, toDate, page, size);
        return ResponseEntity.ok().body(response);
    }
}