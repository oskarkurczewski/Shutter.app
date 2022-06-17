package pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint;

import pl.lodz.p.it.ssbd2022.ssbd02.validation.REGEXP;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

/**
 * Adnotacja sprawdzająca poprawność przesyłanego tytułu zdjęcia
 * Pozwala na ustawienie wyłącznie tytułu składającego się z liter
 * (również polskich znaków), cyfr oraz podstawowych znaków interpunkcyjnych
 */
@Constraint(validatedBy = {})
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)

@ReportAsSingleViolation
@Pattern(regexp = REGEXP.PHOTO_TITLE_PATTERN)
@Size(max = 64)
public @interface PhotoTitle {
    String message() default "validator.incorrect.photo.title.regexp";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}