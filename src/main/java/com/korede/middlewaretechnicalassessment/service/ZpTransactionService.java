package com.korede.middlewaretechnicalassessment.service;

import com.korede.middlewaretechnicalassessment.apimodel.request.ZpTransactionRequest;
import com.korede.middlewaretechnicalassessment.apimodel.response.ZpTransactionResponse;
import com.korede.middlewaretechnicalassessment.controlleradvice.InsufficientFundsException;
import com.korede.middlewaretechnicalassessment.controlleradvice.TransactionFailedException;
import org.springframework.stereotype.Service;

import java.util.Optional;
public interface ZpTransactionService {
    ZpTransactionResponse initiateTransaction(ZpTransactionRequest request) throws InsufficientFundsException, TransactionFailedException;

//    Optional<ZpTransactionResponse> checkTransactionStatus(Long transactionId);
ZpTransactionResponse checkTransactionStatus(String transactionReference) throws TransactionFailedException;


    void receiveWebhookNotification(String transactionReference, String status, String statusMessage) throws TransactionFailedException;

}
