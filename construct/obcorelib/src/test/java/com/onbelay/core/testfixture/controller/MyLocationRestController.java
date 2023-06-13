package com.onbelay.core.testfixture.controller;

import com.onbelay.core.controller.BaseRestController;
import com.onbelay.core.entity.snapshot.TransactionResult;
import com.onbelay.core.exception.OBRuntimeException;
import com.onbelay.core.query.exception.DefinedQueryException;
import com.onbelay.core.testfixture.adapter.MyLocationRestAdapter;
import com.onbelay.core.testfixture.snapshot.MyLocationSnapshot;
import com.onbelay.core.testfixture.snapshot.MyLocationSnapshotCollection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/locations")
public class MyLocationRestController extends BaseRestController {
    private static final Logger logger = LogManager.getLogger();

    @Autowired
    private MyLocationRestAdapter myLocationRestAdapter;


    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<TransactionResult> saveLocation(
            @RequestHeader Map<String, String> headers,
            @RequestBody MyLocationSnapshot snapshot,
            BindingResult bindingResult) {


        if (bindingResult.getErrorCount() > 0) {
            logger.error("Errors on save person contact PUT");
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.toString());
            }
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }


        TransactionResult result;
        try {
            result = myLocationRestAdapter.save(snapshot);
        } catch (OBRuntimeException e) {
            result = new TransactionResult(e.getErrorCode());
            result.setErrorMessage(errorMessageService.getErrorMessage(e.getErrorCode()));
        } catch (RuntimeException e) {
            result = new TransactionResult(e.getMessage());
        }
        return processResponse(result);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<MyLocationSnapshot> getLocation(
            @RequestHeader Map<String, String> headers,
            @PathVariable Integer id) {

        MyLocationSnapshot snapshot;
        try {
            snapshot = myLocationRestAdapter.load(id);
        } catch (OBRuntimeException r) {
            snapshot = new MyLocationSnapshot(r.getErrorCode());
            snapshot.setErrorMessage(errorMessageService.getErrorMessage(r.getErrorCode()));
        } catch (DefinedQueryException r) {
            snapshot = new MyLocationSnapshot(r.getMessage());
        } catch (RuntimeException r) {
            snapshot = new MyLocationSnapshot(r.getMessage());
        }

        return (ResponseEntity<MyLocationSnapshot>) processResponse(snapshot);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<MyLocationSnapshotCollection> getLocations(
            @RequestHeader Map<String, String> headers,
            @RequestParam(value = "query", defaultValue = "default") String queryText,
            @RequestParam(value = "start", defaultValue = "0") Integer start,
            @RequestParam(value = "limit", defaultValue = "100") Integer limit) {

        MyLocationSnapshotCollection collection;

        try {
            collection = myLocationRestAdapter.find(
                    queryText,
                    start,
                    limit);
        } catch (OBRuntimeException r) {
            collection = new MyLocationSnapshotCollection(r.getErrorCode());
              collection.setErrorMessage(errorMessageService.getErrorMessage(r.getErrorCode()));
        } catch (DefinedQueryException r) {
            collection = new MyLocationSnapshotCollection(r.getMessage());
        } catch (RuntimeException r) {
            collection = new MyLocationSnapshotCollection(r.getMessage());
        }

        return (ResponseEntity<MyLocationSnapshotCollection>) processResponse(collection);
    }
}