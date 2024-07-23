package com.korede.middlewaretechnicalassessment.apimodel.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZpTransactionRequest {
    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    private String paymentMethod;

    @NotBlank(message = "Sender Account Number is required")
    private String senderAccountNumber;

    @NotBlank(message = "Recipient Account Number is required")
    private String recipientAccountNumber;
}

//@AllArgsConstructor
//@NoArgsConstructor
//public class ZpTransactionRequest {
//    @NotNull(message = "Amount is required")
//    private BigDecimal amount;
//    private String paymentMethod;
//
//    public BigDecimal getAmount() {
//        return amount;
//    }
//
//    public void setAmount(BigDecimal amount) {
//        this.amount = amount;
//    }
//
//    public String getPaymentMethod() {
//        return paymentMethod;
//    }
//
//    public void setPaymentMethod(String paymentMethod) {
//        this.paymentMethod = paymentMethod;
//    }
//
//    public String getSenderAccountNumber() {
//        return senderAccountNumber;
//    }
//
//    public void setSenderAccountNumber(String senderAccountNumber) {
//        this.senderAccountNumber = senderAccountNumber;
//    }
//
//    public String getRecipientAccountNumber() {
//        return recipientAccountNumber;
//    }
//
//    public void setRecipientAccountNumber(String recipientAccountNumber) {
//        this.recipientAccountNumber = recipientAccountNumber;
//    }
//
//    @NotBlank(message = "Sender Account Number is required")
//    private String senderAccountNumber;
//    @NotBlank(message = "Recipient Account Number is required")
//    private String recipientAccountNumber;
//}