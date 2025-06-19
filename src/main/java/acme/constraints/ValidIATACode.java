
package acme.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@ReportAsSingleViolation
@NotNull
@NotBlank
@Pattern(regexp = "^[A-Z]{3}$")
public @interface ValidIATACode {

	// Standard validation properties -----------------------------------------

	String message() default "{acme.validation.airport.invalid-IATACode.message}";
	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
