package com.onbelay.core.entity.repository;

import com.onbelay.core.entity.model.AbstractEntity;
import org.springframework.stereotype.Repository;

@Repository(value = "entityRepository")
public class EntityRepositoryBean extends BaseRepository<AbstractEntity> implements EntityRepository<AbstractEntity> {

}
