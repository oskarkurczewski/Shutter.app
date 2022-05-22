package pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint;

import pl.lodz.p.it.ssbd2022.ssbd02.validation.REGEXP;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import java.lang.annotation.*;

/**
 * Adnotacja służąca ograniczeniu pola przedstawiającego login użytkownika.
 * Login musi pasować do wyrażenia regularnego.
 */
@Constraint(validatedBy = {})
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)

@ReportAsSingleViolation
@Pattern(regexp = REGEXP.TWO_FA_CODE)
public @interface TwoFACode {
    String message() default "validator.incorrect.2facode.regexp";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
