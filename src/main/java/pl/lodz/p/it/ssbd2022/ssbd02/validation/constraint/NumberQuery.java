package pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint;


import pl.lodz.p.it.ssbd2022.ssbd02.validation.REGEXP;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

/**
 * Adnotacja służąca ograniczeniu pola przedstawiającego numer strony w przypadku paginacji.
 * Minimalna długość to 1
 */
@Constraint(validatedBy = {})
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)

@ReportAsSingleViolation
@Min(1)
@Max(Long.MAX_VALUE)
public @interface NumberQuery {
    String message() default "validator.incorrect.query_number.out_of_range";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
