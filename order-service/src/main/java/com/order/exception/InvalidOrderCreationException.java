package com.order.exception;

public class InvalidOrderCreationException extends RuntimeException{
    public InvalidOrderCreationException(String message) {
        super(message);
    }
}
