package com.korede.middlewaretechnicalassessment.controller;

import com.korede.middlewaretechnicalassessment.apimodel.request.ZpTransactionRequest;
import com.korede.middlewaretechnicalassessment.apimodel.response.ZpTransactionResponse;
import com.korede.middlewaretechnicalassessment.controlleradvice.InsufficientFundsException;
import com.korede.middlewaretechnicalassessment.controlleradvice.TransactionFailedException;
import com.korede.middlewaretechnicalassessment.service.ZpTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/zp/transactions")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Transaction API", description = "Transaction Controller")
public class ZpTransactionController {

    private final ZpTransactionService zpTransactionService;

    @PostMapping("/initiate")
    @Operation(
            summary = "Initiate a new transaction",
            requestBody = @RequestBody(
                    description = "Transaction Request",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ZpTransactionRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Transaction Successful", content = @Content(schema = @Schema(implementation = ZpTransactionResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    public ResponseEntity<ZpTransactionResponse> initiateTransaction(
            @RequestBody ZpTransactionRequest request) {
        try {
            ZpTransactionResponse response = zpTransactionService.initiateTransaction(request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InsufficientFundsException | TransactionFailedException e) {
            log.error("Transaction initiation failed: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/status/{transactionReference}")
    @Operation(
            summary = "Check the status of a transaction",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Transaction status retrieved", content = @Content(schema = @Schema(implementation = ZpTransactionResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Transaction not found")
            }
    )
    public ResponseEntity<ZpTransactionResponse> checkTransactionStatus(
            @PathVariable String transactionReference) {
        try {
            ZpTransactionResponse response = zpTransactionService.checkTransactionStatus(transactionReference);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (TransactionFailedException e) {
            log.error("Checking transaction status failed: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/webhook-Notification")
    @Operation(
            summary = "Receive webhook notification for transaction updates",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Webhook notification received"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    public ResponseEntity<Void> receiveWebhookNotification(
            @RequestParam String transactionReference,
            @RequestParam String status,
            @RequestParam String statusMessage) {
        try {
            zpTransactionService.receiveWebhookNotification(transactionReference, status, statusMessage);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (TransactionFailedException e) {
            log.error("Webhook notification processing failed: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
