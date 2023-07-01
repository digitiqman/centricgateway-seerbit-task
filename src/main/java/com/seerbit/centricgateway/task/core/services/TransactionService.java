package com.seerbit.centricgateway.task.core.services;

import com.seerbit.centricgateway.task.models.TransactionRequestPayload;
import com.seerbit.centricgateway.task.models.StatisticsResponsePayload;

import java.util.List;

public interface TransactionService {

    StatisticsResponsePayload retrieveStatistics();
    
    List<TransactionRequestPayload> retrieveTransactionsList();

    void saveTransaction(TransactionRequestPayload transactionRequest);

    void clearTransactions();
}
