package com.korede.middlewaretechnicalassessment.enums;

public enum ZpTransactionStatus {
    PENDING ("PENDING"),
    INSUFFICIENT_FUNDS ("INSUFFICIENT_FUNDS"),
    COMPLETED ("COMPLETED"),
    FAILED ("FAILED");

    private String status;

    ZpTransactionStatus( String status) {
        this.status =status;
    }
}
