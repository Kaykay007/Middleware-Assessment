package com.korede.middlewaretechnicalassessment.controlleradvice;

public class TransactionFailedException extends Exception{
    public TransactionFailedException(String message) {
        super(message);
    }
}
