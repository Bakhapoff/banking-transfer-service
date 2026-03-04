package kg.ab.transfer.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kg.ab.transfer.model.payload.request.TransferRequest;

public class DifferentAccountsValidator implements ConstraintValidator<DifferentAccounts, TransferRequest> {

    @Override
    public boolean isValid(TransferRequest request, ConstraintValidatorContext context) {
        if (request.fromAccountNumber() == null || request.toAccountNumber() == null) {
            return true;
        }
        return !request.fromAccountNumber().equals(request.toAccountNumber());
    }
}