package com.onbelay.core.entity.repository;

import com.onbelay.core.entity.model.AuditAbstractEntity;
import org.springframework.stereotype.Repository;

@Repository(value = "auditEntityRepository")
public class AuditEntityRepositoryBean extends BaseRepository<AuditAbstractEntity> implements AuditEntityRepository {


}
