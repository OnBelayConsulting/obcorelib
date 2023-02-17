package com.onbelay.core.entity.snapshot;

import java.util.List;

public class EntityListItemCollection extends AbstractSnapshotCollection<EntityListItem> {

    public static final String ITEM_TYPE = "entityListItem";

    public EntityListItemCollection() {
        super(ITEM_TYPE);
    }

    public EntityListItemCollection(
            int start,
            int limit,
            int totalItems,
            List<EntityListItem> snapshots) {

        super(
                ITEM_TYPE,
                start,
                limit,
                totalItems,
                snapshots);
    }

    public EntityListItemCollection(
            String errorCode,
            String errorMessage) {

        super(
                errorCode,
                errorMessage);
    }

    public EntityListItemCollection(
            int start,
            int limit,
            int totalItems) {

        super(
                ITEM_TYPE,
                start,
                limit,
                totalItems);
    }



}
