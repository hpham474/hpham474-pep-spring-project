package com.example.exception;

public class MessageNotFoundException extends Exception {
    public MessageNotFoundException(String message) {
        super(message);
    }
}
