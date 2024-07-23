package com.korede.middlewaretechnicalassessment.service;

import com.korede.middlewaretechnicalassessment.apimodel.request.ZpTransactionRequest;
import com.korede.middlewaretechnicalassessment.apimodel.response.ZpTransactionResponse;
import com.korede.middlewaretechnicalassessment.controlleradvice.InsufficientFundsException;
import com.korede.middlewaretechnicalassessment.controlleradvice.TransactionFailedException;
import com.korede.middlewaretechnicalassessment.entity.ZpCustomerAccount;
import com.korede.middlewaretechnicalassessment.entity.ZpTransaction;
import com.korede.middlewaretechnicalassessment.enums.ZpTransactionStatus;
import com.korede.middlewaretechnicalassessment.repository.ZpAccountRepository;
import com.korede.middlewaretechnicalassessment.repository.ZpTransactionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ZpTransactionServiceImpl implements ZpTransactionService{

    @Autowired
    private ZpTransactionRepository zpTransactionRepository;
    @Autowired
    private ZpAccountRepository zpAccountRepository;


    @Override
   @Transactional
    public ZpTransactionResponse initiateTransaction(ZpTransactionRequest request) throws InsufficientFundsException, TransactionFailedException {
        log.info("Processing transaction request in service layer!!!!!!: {}", request);
        if (request.getSenderAccountNumber().equals(request.getRecipientAccountNumber())) {
            log.error("Transaction failed due to identical account numbers.");
            throw new TransactionFailedException("Sender and recipient account numbers cannot be the same.");
        }

        if (request.getSenderAccountNumber() == null || request.getRecipientAccountNumber() == null) {
            log.error("Transaction failed due to missing account numbers.");
            throw new TransactionFailedException("Sender and recipient account numbers must be present for transaction processing.");
        }

        ZpCustomerAccount senderAccount = zpAccountRepository.findByAccountNumber(request.getSenderAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("Sender account not found: " + request.getSenderAccountNumber()));

        ZpCustomerAccount recipientAccount = zpAccountRepository.findByAccountNumber(request.getRecipientAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("Recipient account not found: " + request.getRecipientAccountNumber()));

        ZpTransaction transaction = new ZpTransaction();
        transaction.setTransactionReference(UUID.randomUUID().toString());
        transaction.setAmount(request.getAmount());
        transaction.setPaymentMethod(request.getPaymentMethod());
        transaction.setStatus(ZpTransactionStatus.PENDING);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setSenderAccount(senderAccount);
        transaction.setRecipientAccount(recipientAccount);

        try {
            BigDecimal senderBalance = senderAccount.getBalance();
            if (senderBalance.compareTo(request.getAmount()) < 0) {
                transaction.setStatus(ZpTransactionStatus.INSUFFICIENT_FUNDS);
                transaction.setStatusMessage("Insufficient funds in sender's account.");
                throw new InsufficientFundsException("Insufficient funds in sender's account.");
            } else {
                transaction.setStatus(ZpTransactionStatus.COMPLETED);
                senderAccount.setBalance(senderBalance.subtract(request.getAmount()));
                log.info("Sender account balance updated: {}", senderAccount.getBalance());
                recipientAccount.setBalance(recipientAccount.getBalance().add(request.getAmount()));
                log.info("Recipient account balance updated: {}", recipientAccount.getBalance());
                zpAccountRepository.save(senderAccount);
                zpAccountRepository.save(recipientAccount);
            }
        } catch (InsufficientFundsException e) {
            throw e;
        } catch (Exception e) {
            transaction.setStatus(ZpTransactionStatus.FAILED);
            transaction.setStatusMessage("Transaction failed due to an error: " + e.getMessage());
            log.error("Transaction processing error: {}", e.getMessage(), e);
        }

        zpTransactionRepository.save(transaction);
        log.info("transaction initiated successfully:{},  ", transaction);

        return toTransactionResponse(transaction);
    }

    @Override
    public ZpTransactionResponse checkTransactionStatus(String transactionReference) throws TransactionFailedException {
        ZpTransaction transaction = zpTransactionRepository.findByTransactionReference(transactionReference)
                .orElseThrow(() -> new TransactionFailedException("Transaction not found with reference: " + transactionReference));

        return toTransactionResponse(transaction);
    }

    @Override
    public void receiveWebhookNotification(String transactionReference, String status, String statusMessage) throws TransactionFailedException {
        ZpTransaction transaction = zpTransactionRepository.findByTransactionReference(transactionReference)
                .orElseThrow(() -> new TransactionFailedException("Transaction not found with reference: " + transactionReference));

        try {
            ZpTransactionStatus transactionStatus = ZpTransactionStatus.valueOf(status);
            transaction.setStatus(transactionStatus);
            transaction.setStatusMessage(statusMessage);
            zpTransactionRepository.save(transaction);
            log.info("Transaction status updated via webhook: {}", transaction);
        } catch (IllegalArgumentException e) {
            log.error("Invalid status received in webhook: {}", status);
            throw new TransactionFailedException("Invalid status received in webhook: " + status);
        }

    }


    private ZpTransactionResponse toTransactionResponse(ZpTransaction transaction) {
        ZpTransactionResponse response = new ZpTransactionResponse();
        response.setTransactionReference(transaction.getTransactionReference());
        response.setStatus(String.valueOf(transaction.getStatus()));
        response.setStatusMessage(transaction.getStatusMessage());
        response.setAmount(transaction.getAmount());
        response.setCreatedAt(transaction.getCreatedAt());
        response.setPaymentMethod(transaction.getPaymentMethod());
        response.setSenderAccountNumber(transaction.getSenderAccount().getAccountNumber());
        response.setRecipientAccountNumber(transaction.getRecipientAccount().getAccountNumber());
        return response;
    }






}
