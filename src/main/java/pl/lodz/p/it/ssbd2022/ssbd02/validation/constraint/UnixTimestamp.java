package pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint;

import pl.lodz.p.it.ssbd2022.ssbd02.validation.REGEXP;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import java.lang.annotation.*;

@Constraint(validatedBy = {})
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)

@ReportAsSingleViolation
@Pattern(regexp = REGEXP.UNIX_TIMESTAMP_PATTERN)
public @interface UnixTimestamp {
    String message() default "validator.incorrect.timestamp";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
