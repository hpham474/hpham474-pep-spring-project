package com.example.exception;

public class MessageCreationFailedException extends Exception {
    public MessageCreationFailedException (String message) {
        super(message);
    }
}
