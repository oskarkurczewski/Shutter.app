package pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint;


import pl.lodz.p.it.ssbd2022.ssbd02.validation.REGEXP;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import java.lang.annotation.*;

/**
 * Adnotacja służąca ograniczeniu pola przedstawiającego żeton
 */
@Constraint(validatedBy = {})
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)

@ReportAsSingleViolation
@Pattern(regexp = REGEXP.UUID)
public @interface Token {
    String message() default "validator.incorrect.token.regexp";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
