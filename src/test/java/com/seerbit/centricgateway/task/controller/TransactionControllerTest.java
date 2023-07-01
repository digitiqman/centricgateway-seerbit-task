package com.seerbit.centricgateway.task.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultMatcher;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seerbit.centricgateway.task.core.services.TransactionService;
import com.seerbit.centricgateway.task.models.TransactionRequestPayload;
import com.seerbit.centricgateway.task.utilities.BadRequestPayload;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import static com.seerbit.centricgateway.task.utilities.RequestResponse.sampleResponse;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

    private final String BASE_URL = "/transaction";


    @MockBean
	private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;
	
    @Autowired
	private MockMvc mockMvc;
	


    @BeforeEach
    void initialize() {
        doNothing().when(transactionService).saveTransaction(any());
        doNothing().when(transactionService).clearTransactions();
        when(transactionService.retrieveStatistics()).thenReturn(sampleResponse());
    }

    @Test
    void should_successfully_post_transaction() throws Exception {
        TransactionRequestPayload transactionRequest = TransactionRequestPayload.builder()
                .amount(new BigDecimal("100.1234"))
                .timestamp(LocalDateTime.now().minusSeconds(33))
                .build();

        triggerPostTransaction(transactionRequest, status().isCreated());
    }

    @Test
    void ifTimestampIsOlderThanAtLeast30Seconds_thenShouldReturnCreated() throws Exception {
        TransactionRequestPayload transactionRequest = TransactionRequestPayload.builder()
                .amount(new BigDecimal("23.1234"))
                .timestamp(LocalDateTime.parse("2023-06-29T04:45:45.232Z", ISO_DATE_TIME ))
                .build();

        triggerPostTransaction(transactionRequest, status().isCreated());
    }


    @Test
    void ifStatisticsIsFetched_shouldReturnSuccess() throws Exception {

        mockMvc.perform(get(BASE_URL)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.max", Is.is(55.9)))
                .andExpect(jsonPath("$.min", Is.is(6.66)))
                .andExpect(jsonPath("$.avg", Is.is(9.45)))
                .andExpect(jsonPath("$.sum", Is.is(17.59)))
                .andExpect(jsonPath("$.count", Is.is(3)))
                .andDo(print());
    }

    @Test
    void ifDeleteIsInvoked_shouldReturnNoContentStatus() throws Exception {

        mockMvc.perform(delete(BASE_URL))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void ifMoreThanFourFractionalDigits_thenShouldReturnBadRequest() throws Exception {
        TransactionRequestPayload transactionRequest = TransactionRequestPayload.builder()
                .amount(new BigDecimal("22.123456"))
                .timestamp(LocalDateTime.parse("2022-05-13T00:45:51.312Z", ISO_DATE_TIME ))
                .build();

        triggerPostTransaction(transactionRequest, status().isBadRequest());
    }

    @Test
    void ifDateIsInFuture_thenShouldReturnNoContent() throws Exception {
        TransactionRequestPayload transactionRequest = TransactionRequestPayload.builder()
                .amount(new BigDecimal("33.44"))
                .timestamp(LocalDateTime.parse("2023-07-11T00:45:51.312Z", ISO_DATE_TIME))
                .build();

        triggerPostTransaction(transactionRequest, status().isNoContent());
    }
    
    private void triggerPostTransaction(TransactionRequestPayload transactionRequest, ResultMatcher expectedStatus) throws Exception {

        mockMvc.perform(post(BASE_URL)
                        .contentType(APPLICATION_JSON).content(asJsonString(transactionRequest)))
                .andExpect(expectedStatus)
                .andDo(print());
    }

    private String asJsonString(final Object obj) {
        try {
            return this.objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}