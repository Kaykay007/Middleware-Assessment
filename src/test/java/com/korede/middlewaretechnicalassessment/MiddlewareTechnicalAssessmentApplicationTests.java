package com.korede.middlewaretechnicalassessment;

import com.korede.middlewaretechnicalassessment.apimodel.request.ZpTransactionRequest;
import com.korede.middlewaretechnicalassessment.apimodel.response.ZpTransactionResponse;
import com.korede.middlewaretechnicalassessment.controlleradvice.InsufficientFundsException;
import com.korede.middlewaretechnicalassessment.controlleradvice.TransactionFailedException;
import com.korede.middlewaretechnicalassessment.entity.ZpCustomerAccount;
import com.korede.middlewaretechnicalassessment.entity.ZpTransaction;
import com.korede.middlewaretechnicalassessment.repository.ZpAccountRepository;
import com.korede.middlewaretechnicalassessment.repository.ZpTransactionRepository;
import com.korede.middlewaretechnicalassessment.service.ZpTransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MiddlewareTechnicalAssessmentApplicationTests {

    @Mock
    private ZpAccountRepository zpAccountRepository;

    @Mock
    private ZpTransactionRepository zpTransactionRepository;

    @InjectMocks
    private ZpTransactionServiceImpl zpTransactionServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testInitiateTransaction() throws InsufficientFundsException, TransactionFailedException {
        ZpTransactionRequest request = new ZpTransactionRequest();
        request.setAmount(new BigDecimal("50"));
        request.setPaymentMethod("transfer");
        request.setSenderAccountNumber("1122334455");
        request.setRecipientAccountNumber("9988776655");

        ZpCustomerAccount senderAcc = new ZpCustomerAccount();
        senderAcc.setAccountNumber("1122334455");
        senderAcc.setBalance(new BigDecimal("10000.00"));

        ZpCustomerAccount recipientAcc = new ZpCustomerAccount();
        recipientAcc.setAccountNumber("9988776655");
        recipientAcc.setBalance(new BigDecimal("200.00"));

        when(zpAccountRepository.findByAccountNumber("1122334455")).thenReturn(Optional.of(senderAcc));
        when(zpAccountRepository.findByAccountNumber("9988776655")).thenReturn(Optional.of(recipientAcc));

        ZpTransactionResponse response = zpTransactionServiceImpl.initiateTransaction(request);

        verify(zpTransactionRepository, times(1)).save(any(ZpTransaction.class));
        verify(zpAccountRepository, times(1)).save(senderAcc);
        verify(zpAccountRepository, times(1)).save(recipientAcc);

        assertEquals("COMPLETED", response.getStatus());
        assertEquals("1122334455", response.getSenderAccountNumber());
        assertEquals("9988776655", response.getRecipientAccountNumber());
        assertEquals(0, senderAcc.getBalance().compareTo(new BigDecimal("9950")));
        assertEquals(0, recipientAcc.getBalance().compareTo(new BigDecimal("250")));
    }
}
