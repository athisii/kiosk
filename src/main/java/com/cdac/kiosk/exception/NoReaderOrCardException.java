package com.cdac.kiosk.exception;

public class NoReaderOrCardException extends RuntimeException {
    public NoReaderOrCardException(String message) {
        super(message);
    }
}
