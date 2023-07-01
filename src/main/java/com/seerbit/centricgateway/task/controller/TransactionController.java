package com.seerbit.centricgateway.task.controller;


import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.seerbit.centricgateway.task.models.TransactionRequestPayload;
import com.seerbit.centricgateway.task.core.services.TransactionService;
import com.seerbit.centricgateway.task.core.services.TransactionServiceImpl;
import com.seerbit.centricgateway.task.models.StatisticsResponsePayload;
import jakarta.validation.*;



@AllArgsConstructor
@RestController
@RequestMapping("/transaction")
@Validated
public class TransactionController {

    private static TransactionService transactionService = new TransactionServiceImpl();

    @PostMapping()    
    public ResponseEntity<?> postTransaction(@RequestBody @Valid TransactionRequestPayload transactionReqPayload) {
        
        /*Ensure the transaction timestamp was not completed less than 30 seconds ago */
        if(transactionReqPayload.getTimestamp().isAfter(LocalDateTime.now().minusSeconds(30)))
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        TransactionController.transactionService.saveTransaction(transactionReqPayload);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<StatisticsResponsePayload> getTransactionStatistics(){
        return new ResponseEntity<>(TransactionController.transactionService.retrieveStatistics(), HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteTransaction(){
        TransactionController.transactionService.clearTransactions();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
