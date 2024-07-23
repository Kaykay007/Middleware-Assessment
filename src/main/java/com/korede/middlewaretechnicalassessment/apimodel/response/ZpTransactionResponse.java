package com.korede.middlewaretechnicalassessment.apimodel.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
public class ZpTransactionResponse {
    private String transactionReference;
    private String status;
    private  String statusMessage;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private String paymentMethod;
    private String senderAccountNumber;
    private String recipientAccountNumber;

}
