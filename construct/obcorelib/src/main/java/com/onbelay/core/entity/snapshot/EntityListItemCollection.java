package com.onbelay.core.entity.snapshot;

import java.util.List;

public class EntityListItemCollection extends AbstractSnapshotCollection<EntityId> {

    public static final String ITEM_TYPE = "entityListItem";

    public EntityListItemCollection() {
        super(ITEM_TYPE);
    }

    public EntityListItemCollection(
            int start,
            int limit,
            int totalItems,
            List<EntityId> snapshots) {

        super(
                ITEM_TYPE,
                start,
                limit,
                totalItems,
                snapshots);
    }

    public EntityListItemCollection(
            String errorCode) {

        super(
                "listItems",
                errorCode);
    }


    public EntityListItemCollection(
            String errorCode,
            List<String> parameters) {

        super(
                "listItems",
                errorCode,
                parameters);
    }


    public EntityListItemCollection(
            String errorCode,
            boolean isPermissionException) {

        super(
                "listItems",
                errorCode,
                isPermissionException);
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
