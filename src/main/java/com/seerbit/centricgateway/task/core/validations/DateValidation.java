package com.seerbit.centricgateway.task.core.validations;



import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import jakarta.validation.*;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;


@Documented
@Target( { FIELD, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateValidationImpl.class)
public @interface DateValidation {
    Class<?>[] groups() default {}; //Constraints
    String message() default "Date is limited to past and current values."; //Error message
    Class<? extends Payload>[] payload() default {};
}