package pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint;

import pl.lodz.p.it.ssbd2022.ssbd02.validation.validator.LoginValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.*;

@Constraint(validatedBy = {})
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)

@Pattern(regexp = LoginValidator.LOGIN_PATTERN,
        message = "validator.incorrect.login")
public @interface Login {
    String message() default "Invalid login.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
