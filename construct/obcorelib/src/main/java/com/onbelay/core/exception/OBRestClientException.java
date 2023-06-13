package com.onbelay.core.exception;

import org.springframework.http.HttpStatus;

public class OBRestClientException extends RuntimeException {

    private HttpStatus httpStatus;

    public OBRestClientException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
