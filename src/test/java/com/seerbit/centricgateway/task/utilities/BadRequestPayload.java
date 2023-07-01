package com.seerbit.centricgateway.task.utilities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BadRequestPayload {

    private BigDecimal amount;
    private LocalDateTime timestamp;
}