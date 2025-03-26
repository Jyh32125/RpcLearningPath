package com.jyh.SimpleRpcV2.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

public class SerializationException extends RuntimeException {
    public SerializationException(String message) {
        super(message);
    }
}
