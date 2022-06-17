package pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint;

import pl.lodz.p.it.ssbd2022.ssbd02.validation.REGEXP;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

/**
 * Adnotacja służąca ograniczeniu pola przedstawiającego imię użytkownika.
 * Minimalna długość to 1, a maksymalna to 64.
 */
@Constraint(validatedBy = {})
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)

@ReportAsSingleViolation
@Size(min = 1, max = 64)
@Pattern(regexp = REGEXP.NAME_SURNAME_PATTERN)
public @interface Name {
    String message() default "validator.incorrect.name.regexp";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
