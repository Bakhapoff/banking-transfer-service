package kg.ab.transfer.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import kg.ab.transfer.exception.custom_exception.AccountBlockedException;
import kg.ab.transfer.exception.custom_exception.AccountNotFoundException;
import kg.ab.transfer.exception.custom_exception.InsufficientFundsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

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

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<Map<String, String>> handleInsufficientFunds(InsufficientFundsException e) {
        return buildError(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleAccountNotFound(AccountNotFoundException e) {
        return buildError(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccountBlockedException.class)
    public ResponseEntity<Map<String, String>> handleAccountBlocked(AccountBlockedException e) {
        return buildError(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAllExceptions(Exception e) {
        return buildError("An unexpected error has occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Map<String, String>> buildError(String message, HttpStatus status) {
        return new ResponseEntity<>(Map.of("error", message), status);
    }
}