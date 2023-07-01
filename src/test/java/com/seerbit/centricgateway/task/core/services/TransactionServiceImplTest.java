package com.seerbit.centricgateway.task.core.services;


import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
//import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.seerbit.centricgateway.task.core.services.TransactionService;
import com.seerbit.centricgateway.task.core.services.TransactionServiceImpl;
import com.seerbit.centricgateway.task.models.StatisticsResponsePayload;
import com.seerbit.centricgateway.task.models.TransactionRequestPayload;
import static com.seerbit.centricgateway.task.utilities.RequestResponse.sampleRequest;
import static com.seerbit.centricgateway.task.utilities.RequestResponse.sampleResponse;

@SpringBootTest
class TransactionServiceImplTest {

    @MockBean
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        this.transactionService = new TransactionServiceImpl();
    }

    @AfterEach
    void tearDown() {
        this.transactionService.retrieveTransactionsList().clear();
    }

    @Test
    void shouldIncrementListSize_onPostTransaction() {
        buildRequestCallServiceAndAssert();
    }

    @Test
    void shouldReturnData_onRetrieveStatistics() throws InterruptedException {
        buildRequestCallServiceAndAssert();


        StatisticsResponsePayload response = transactionService.retrieveStatistics();
        assertSame(Long.valueOf(7), response.getCount());
        assertEquals(new BigDecimal("10.50"), response.getMin(), "Minimum will be 10.50");
        assertEquals(new BigDecimal("70.50"), response.getMax(), "Maximum will be 70.50");
        assertEquals(new BigDecimal("283.50"), response.getSum(), "Sum will be 283.50");
        assertEquals(new BigDecimal("283.50").divide(BigDecimal.valueOf(7)), response.getAvg(), "Avg will be 40.50");
    }

    @Test
    void shouldDeleteAllData_OnAction() {
        buildRequestCallServiceAndAssert();
        this.transactionService.clearTransactions();
        assertSame(0, transactionService.retrieveTransactionsList().size());
    }

    @Test
    void buildRequestCallServiceAndAssert() {
        
        TransactionRequestPayload tranRequest = sampleRequest("70.50", LocalDateTime.now().minusSeconds(55));
        transactionService.saveTransaction(tranRequest);
        assertSame(1, transactionService.retrieveTransactionsList().size(),
                "Transaction List Should be 1");

        tranRequest = sampleRequest("50.50", LocalDateTime.now().minusSeconds(50));
        transactionService.saveTransaction(tranRequest);
        assertSame(2, transactionService.retrieveTransactionsList().size(),
                "Transaction List Should be 2");
                
        tranRequest = sampleRequest("30.50", LocalDateTime.now().minusSeconds(45));
        transactionService.saveTransaction(tranRequest);
        assertSame(3, transactionService.retrieveTransactionsList().size(),
                "Transaction List Should be 3");

        tranRequest = sampleRequest("60.50", LocalDateTime.now().minusSeconds(40));
        transactionService.saveTransaction(tranRequest);
        assertSame(4, transactionService.retrieveTransactionsList().size(),
                "Transaction List Should be 4");

        tranRequest = sampleRequest("20.50", LocalDateTime.now().minusSeconds(35));
        transactionService.saveTransaction(tranRequest);
        assertSame(5, transactionService.retrieveTransactionsList().size(),
                "Transaction List Should be 5");

        tranRequest = sampleRequest("40.50", LocalDateTime.now().minusSeconds(32));
        transactionService.saveTransaction(tranRequest);
        assertSame(6, transactionService.retrieveTransactionsList().size(),
                "Size of the Transaction List Should be 6");

        tranRequest = sampleRequest("10.50", LocalDateTime.now().minusSeconds(41));
        transactionService.saveTransaction(tranRequest);
        assertSame(7, transactionService.retrieveTransactionsList().size(),
                "Size of the Transaction List Should be 7");
    }
}
