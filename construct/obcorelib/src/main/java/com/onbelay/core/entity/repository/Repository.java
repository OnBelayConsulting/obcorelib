/*
 Copyright 2019, OnBelay Consulting Ltd.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.  
*/
package com.onbelay.core.entity.repository;

import com.onbelay.core.entity.model.AbstractEntity;
import com.onbelay.core.entity.model.AuditAbstractEntity;
import com.onbelay.core.entity.model.TemporalAbstractEntity;

public interface Repository<T> {
	
	public void save(AbstractEntity entity);
	
	public void delete(AbstractEntity entity);
	
	public void recordHistory(AuditAbstractEntity auditEntity);
	
	public void saveWithHistory(TemporalAbstractEntity entity, AuditAbstractEntity auditEntity);
	
	public void flush();

}
