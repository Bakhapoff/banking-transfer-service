package kg.ab.transfer.validation;

import kg.ab.transfer.exception.custom_exception.InvalidDateRangeException;
import kg.ab.transfer.exception.custom_exception.InvalidPaginationException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AccountValidator {

    public void validate(LocalDateTime fromDate, LocalDateTime toDate, int page, int size) {
        validateDateRange(fromDate, toDate);
        validatePagination(page, size);
    }

    private void validateDateRange(LocalDateTime fromDate, LocalDateTime toDate) {
        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
            throw new InvalidDateRangeException("<fromDate> must be before <toDate>.");
        }
    }

    private void validatePagination(int page, int size) {
        if (page < 0) {
            throw new InvalidPaginationException("Page must be >= 0.");
        }
        if (size < 1 || size > 50) {
            throw new InvalidPaginationException("Size must be between 1 and 50.");
        }
    }
}