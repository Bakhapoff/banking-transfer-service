package kg.ab.transfer.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolationException;
import kg.ab.transfer.exception.custom_exception.InvalidDateRangeException;
import kg.ab.transfer.exception.custom_exception.InsufficientFundsException;
import kg.ab.transfer.exception.custom_exception.AccountNotFoundException;
import kg.ab.transfer.exception.custom_exception.AccountBlockedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException hmnre) {
        Map<String, Object> result = new HashMap<>();
        result.put("error", "Invalid JSON format.");

        Throwable cause = hmnre.getCause();

        if (cause instanceof InvalidFormatException ife && ife.getPath() != null && !ife.getPath().isEmpty()) {
            String fieldName = ife.getPath().getLast().getFieldName();
            result.put("description", "Invalid value for field <" + fieldName + ">.");
            result.put("invalidValue", ife.getValue());
        } else {
            result.put("description", hmnre.getMessage());
        }
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException manve) {
        Map<String, String> errors = new HashMap<>();

        manve.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        manve.getBindingResult().getGlobalErrors().forEach(error ->
                errors.put("error", error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException matme) {
        return buildError(
                "Invalid value for parameter '" + matme.getName() +
                        "'. Expected format: yyyy-MM-ddTHH:mm:ss", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException cve) {
        String message = cve.getConstraintViolations().iterator().next().getMessage();
        return buildError(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDateRangeException.class)
    public ResponseEntity<Map<String, String>> handleInvalidDateRangeException(InvalidDateRangeException idre) {
        return buildError(idre.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<Map<String, String>> handleInsufficientFunds(InsufficientFundsException ife) {
        return buildError(ife.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleAccountNotFound(AccountNotFoundException anfe) {
        return buildError(anfe.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccountBlockedException.class)
    public ResponseEntity<Map<String, String>> handleAccountBlocked(AccountBlockedException abe) {
        return buildError(abe.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAllExceptions(Exception e) {
        log.error("Unexpected error: ", e);
        return buildError("An unexpected error has occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Map<String, String>> buildError(String message, HttpStatus status) {
        return new ResponseEntity<>(Map.of("error", message), status);
    }
}