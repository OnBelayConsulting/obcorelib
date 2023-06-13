package com.onbelay.core.restclient;

import com.onbelay.core.exception.OBRestClientException;
import org.springframework.http.HttpStatus;

import java.util.function.Predicate;

public class ApplicationBaseRestClient {

    protected Predicate<Throwable> checkThrowable = (ex) -> {
        if (ex instanceof OBRestClientException) {
            OBRestClientException rc = (OBRestClientException) ex;
            return rc.getHttpStatus().is5xxServerError();
        };
        return false;
    };


    protected OBRestClientException generateException() {
        return new OBRestClientException(HttpStatus.GATEWAY_TIMEOUT, "call to BA");
    }

}
