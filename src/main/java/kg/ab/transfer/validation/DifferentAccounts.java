package kg.ab.transfer.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DifferentAccountsValidator.class)
public @interface DifferentAccounts {

    String message() default "<fromAccountNumber> and <toAccountNumber> must be different";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}