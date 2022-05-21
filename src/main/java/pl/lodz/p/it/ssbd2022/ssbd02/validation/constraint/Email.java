package pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint;

import pl.lodz.p.it.ssbd2022.ssbd02.validation.REGEXP;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

/**
 * Adnotacja służąca ograniczeniu pola przedstawiającego adres email użytkownika.
 * Minimalna długość to 6, a maksymalna to 64.
 * Adres email musi pasować do wyrażenia regularnego.
 */
@Constraint(validatedBy = {})
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)

@ReportAsSingleViolation
@javax.validation.constraints.Email(regexp = REGEXP.EMAIL_PATTERN)
@Size(min = 6, max = 64)
public @interface Email {
    String message() default "validator.incorrect.email.regexp";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
