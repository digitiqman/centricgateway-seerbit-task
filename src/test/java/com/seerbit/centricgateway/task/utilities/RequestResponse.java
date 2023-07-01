package com.seerbit.centricgateway.task.utilities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.seerbit.centricgateway.task.models.StatisticsResponsePayload;
import com.seerbit.centricgateway.task.models.TransactionRequestPayload;

public class RequestResponse {

    private RequestResponse() {
    }

    public static StatisticsResponsePayload sampleResponse() {
        return StatisticsResponsePayload.builder()
                .max(new BigDecimal("55.90"))
                .min(new BigDecimal("6.66"))
                .avg(new BigDecimal("9.45"))
                .sum(new BigDecimal("17.59"))
                .count(3)
                .build();
    }

    public static TransactionRequestPayload sampleRequest(String amt, LocalDateTime dt) {
        return TransactionRequestPayload.builder()
                .amount(new BigDecimal(amt))
                .timestamp(dt)
                .build();
    }

    public static BadRequestPayload badsampleRequest(String amt, LocalDateTime dt) {
        return BadRequestPayload.builder()
                .amount(new BigDecimal(amt))
                //.timestamp(dt)
                .build();
    }

}


