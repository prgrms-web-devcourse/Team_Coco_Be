package com.cocodan.triplan.common.error;

public class NotExistsException extends RuntimeException {
    public NotExistsException(String message) {
        super(message);
    }
}
