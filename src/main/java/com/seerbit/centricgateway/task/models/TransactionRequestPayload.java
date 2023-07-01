package com.seerbit.centricgateway.task.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.seerbit.centricgateway.task.utilities.DateTimeSerializer;
import com.seerbit.centricgateway.task.utilities.DateTimeDeserializer;
import com.seerbit.centricgateway.task.core.validations.DateValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestPayload {

    @NotNull(message = "Amount must have a value.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0.0")
    @Digits(integer = Integer.MAX_VALUE, fraction = 4, message = "Fraction part of Amount must have a maximum of 4 decimal place")
    private BigDecimal amount;

    
    @DateValidation
    @NotNull(message = "Timestamp cannot be null")
    @JsonDeserialize(using = DateTimeDeserializer.class)
    @JsonSerialize(using = DateTimeSerializer.class)
    private LocalDateTime timestamp;

}
