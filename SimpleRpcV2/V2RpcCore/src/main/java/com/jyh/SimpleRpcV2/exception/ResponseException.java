package com.jyh.SimpleRpcV2.exception;

public class ResponseException extends RuntimeException {
    public ResponseException(String message) {
        super(message);
    }
}
