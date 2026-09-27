package com.product.exception;

public class InvalidActionStateException extends RuntimeException{
    public InvalidActionStateException(String message) {
        super(message);
    }
}
