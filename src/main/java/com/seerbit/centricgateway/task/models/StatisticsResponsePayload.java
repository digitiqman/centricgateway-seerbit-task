package com.seerbit.centricgateway.task.models;


import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;



@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsResponsePayload implements Serializable {


  private long count = 0;
  private BigDecimal min = new BigDecimal("0.00");
  private BigDecimal max = new BigDecimal("0.00");
  private BigDecimal avg = new BigDecimal("0.00");
  private BigDecimal sum = new BigDecimal("0.00");

}
