package com.korede.middlewaretechnicalassessment.controlleradvice;

public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
