package com.onbelay.core.entity.repository;

import com.onbelay.core.entity.model.AuditAbstractEntity;

public interface AuditEntityRepository extends EntityRepository<AuditAbstractEntity> {

    public AuditAbstractEntity executeSingleResultQuery(
            String queryName,
            String[] parmNames,
            Object[] parms);
}