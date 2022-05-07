package pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint;


import pl.lodz.p.it.ssbd2022.ssbd02.validation.REGEXP;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import java.lang.annotation.*;

/**
 * Adnotacja służąca ograniczeniu pola przedstawiającego nadawany lub odbierany poziom dostępu.
 * Nazwa nie może być równa 'ADMINISTRATOR'.
 */
@Constraint(validatedBy = {})
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)

@ReportAsSingleViolation
@Pattern(regexp = REGEXP.ACCESS_LEVEL_PATTERN)
public @interface AccessLevel {
    String message() default "validator.incorrect.access_level.forbidden";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
