package com.seerbit.centricgateway.task.core.validations;


import java.time.LocalDateTime;
import jakarta.validation.*;

public class DateValidationImpl implements ConstraintValidator<DateValidation, LocalDateTime> {


    @Override
    public void initialize(DateValidation constraintAnnotation) {

    }
    
    @Override
    public boolean isValid(LocalDateTime date, ConstraintValidatorContext context) {
        if (date == null)
            return false;

        return true;
        //return !(LocalDateTime.now().minusSeconds(30)).isAfter(date);
    }
}
