package com.onbelay.core.controller;

import com.onbelay.core.auth.model.OBUser;
import com.onbelay.core.auth.service.ApplicationAuthenticationService;
import com.onbelay.core.entity.model.AuditManager;
import com.onbelay.core.entity.snapshot.AbstractSnapshot;
import com.onbelay.core.entity.snapshot.AbstractSnapshotCollection;
import com.onbelay.core.entity.snapshot.TransactionResult;
import com.onbelay.core.errorhandling.ErrorMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseRestController {

    @Autowired
    protected ErrorMessageService errorMessageService;

    protected ResponseEntity<? extends AbstractSnapshotCollection> processResponse(AbstractSnapshotCollection collection) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=utf-8");

        if (collection.isSuccessful()) {
            return new ResponseEntity<>(collection, httpHeaders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(collection, httpHeaders, HttpStatus.BAD_REQUEST);
        }
    }


    protected ResponseEntity<? extends AbstractSnapshot> processResponse(AbstractSnapshot snapshot) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=utf-8");

        if (snapshot.isSuccessful()) {
            return new ResponseEntity<>(snapshot, httpHeaders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(snapshot, httpHeaders, HttpStatus.BAD_REQUEST);
        }
    }



    protected ResponseEntity<TransactionResult> processResponse(TransactionResult result) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=utf-8");

        if (result.isSuccessful()) {
            return new ResponseEntity<>(result, httpHeaders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, httpHeaders, HttpStatus.BAD_REQUEST);
        }
    }

}
