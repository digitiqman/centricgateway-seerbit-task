package com.seerbit.centricgateway.task.core.services;

import org.springframework.stereotype.Service;
import com.seerbit.centricgateway.task.models.TransactionRequestPayload;

import lombok.extern.slf4j.Slf4j;

import com.seerbit.centricgateway.task.models.StatisticsResponsePayload;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;

@Service
@Slf4j
public final class TransactionServiceImpl implements TransactionService {

    private static List<TransactionRequestPayload> transactionRequestsCache = Collections.synchronizedList(new ArrayList<TransactionRequestPayload>());


    @Override
    public StatisticsResponsePayload retrieveStatistics() {
        if (TransactionServiceImpl.transactionRequestsCache.isEmpty())
            return new StatisticsResponsePayload();
            log.info("transactionRequestsCache is not empty!");

        long counter = 0;
        BigDecimal min = new BigDecimal("0.00");
        BigDecimal max = new BigDecimal("0.00");
        BigDecimal sum = new BigDecimal("0.00");

        /* Action to cater to request must be thread-safe while iterating and calculating response payload parameters */
        synchronized (TransactionServiceImpl.transactionRequestsCache) {
            Iterator<TransactionRequestPayload> i = TransactionServiceImpl.transactionRequestsCache.iterator();
            //Use the first as initial min value if not empty;
            if (TransactionServiceImpl.transactionRequestsCache.size() > 0) {
                min = TransactionServiceImpl.transactionRequestsCache.get(0).getAmount();
            }
            while (i.hasNext()) {
                TransactionRequestPayload onetransaction = (TransactionRequestPayload) i.next();
                if (onetransaction.getTimestamp().isAfter(LocalDateTime.now().minusSeconds(30)))
                    continue;

                if (onetransaction.getAmount().compareTo(max) > 0)
                    max = onetransaction.getAmount();

                if (onetransaction.getAmount().compareTo(min) < 0)
                    min = onetransaction.getAmount();

                sum = sum.add(onetransaction.getAmount());
                ++counter;
            }
        }
            
        min = min.setScale(2, RoundingMode.HALF_UP);
        max = max.setScale(2, RoundingMode.HALF_UP);
        sum = sum.setScale(2, RoundingMode.HALF_UP);

        BigDecimal avg = counter != 0 ? sum.divide(BigDecimal.valueOf(counter), RoundingMode.HALF_UP)
                : new BigDecimal("0.00");
        return StatisticsResponsePayload
                .builder()
                .min(min).max(max).avg(avg)
                .count(counter).sum(sum)
                .build();
        
    }
    
    @Override
    public List<TransactionRequestPayload> retrieveTransactionsList() {
        return TransactionServiceImpl.transactionRequestsCache;
    }

    
    @Override
    public void saveTransaction(TransactionRequestPayload transactionRequest) {
        boolean resp = TransactionServiceImpl.transactionRequestsCache.add(transactionRequest);
        System.out.println("Response after adding is: " + resp);
    }

    @Override
    public void clearTransactions() {
        TransactionServiceImpl.transactionRequestsCache.clear();
    }
}
