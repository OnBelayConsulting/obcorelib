package com.onbelay.core.codes.snapshot;

import com.onbelay.core.entity.snapshot.AbstractSnapshotCollection;

import java.util.List;

public class CodeLabelCollection extends AbstractSnapshotCollection<CodeLabel> {
    private static final String NAME= "CodeLabel";


    public CodeLabelCollection() {
        super(NAME);
    }

    public CodeLabelCollection(String errorMessage) {
        super(NAME, errorMessage);
    }

    public CodeLabelCollection(
            String errorMessage,
            boolean isPermssionException) {
        super(
                NAME,
                errorMessage,
                isPermssionException);
    }

    public CodeLabelCollection(
            String errorMessage,
            List<String> parameters) {
        super(
                NAME,
                errorMessage,
                parameters);
    }

    public CodeLabelCollection(
            int start,
            int limit,
            int totalItems,
            List<CodeLabel> snapshots) {

        super(
                NAME,
                start,
                limit,
                totalItems,
                snapshots);
    }

    public CodeLabelCollection(
            int start,
            int limit,
            int totalItems) {
        super(
                NAME,
                start,
                limit,
                totalItems);
    }
}
