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
package com.onbelay.core.appsetting.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.onbelay.core.appsetting.repository.ApplicationSettingRepository;
import com.onbelay.core.appsetting.snapshot.ApplicationSettingSnapshot;
import com.onbelay.core.entity.component.ApplicationContextFactory;
import com.onbelay.core.enums.CoreTransactionErrorCode;
import com.onbelay.core.exception.OBValidationException;

/**
 * @author CANMXF
 *
 */
@Entity
@Table(name="APPLICATION_SETTING")
@NamedQueries({
    @NamedQuery(
    		name = ApplicationSettingRepositoryBean.LOAD_ALL,
    		query = "SELECT applicationSetting " +
                      "FROM ApplicationSetting applicationSetting " +
     	          "ORDER BY applicationSetting.key ")
})
public class ApplicationSetting {
    private String key;
    private String value;
    private boolean isDefault = false;

    public static final int MAX_KEY_LENGTH = 50;
    public static final int MAX_VALUE_LENGTH = 1000;
    
    public ApplicationSetting() {
    }
    
    
    @Id
    @Column (name = "PARM_KEY")
    public String getKey() {
        return key;
    }
    
    public void setKey(String key) {
        this.key = key.trim();
    }
    
    @Column (name = "PARM_VALUE")
    public String getValue() {
        return value;
    }
    
    public void delete() {
    	getRepository().remove(this);
    }
    
    @Transient
    public boolean hasValue() {
    	return value != null;
    }
    
    @Transient
    public Integer getValueAsInt() {
        return Integer.decode(value);
    }
    
    public void setValueAsInt(int i) {
    	value = Integer.toString(i);
    }
    
    @Transient
    public Long getValueAsLong() {
    	return Long.decode(value);
    }
    
    @Transient
    public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

    @Transient
    public boolean getValueAsBoolean() {
        if (value.equalsIgnoreCase("Y") || value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("true") || value.equalsIgnoreCase("t"))
            return true;
        else
            return false;
    }
    
    public void setValueAsBoolean(Boolean bool) {
        if (bool == Boolean.TRUE)
            value = "Y";
        else
            value = "N";
    }
    
    @Transient
    public BigDecimal getValueAsBigDecimal() {
        return new BigDecimal(value); 
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    public void updateWith(ApplicationSettingSnapshot applicationSettingValue) {
    	this.value = applicationSettingValue.getValue();
    }
    
    public void createWith(ApplicationSettingSnapshot applicationSettingValue) {
    	if (applicationSettingValue.getKey() != null)
    		this.key = applicationSettingValue.getKey().trim();
    	this.value = applicationSettingValue.getValue();
    	save();
    }
    
    public void validate() throws OBValidationException {
    	if (this.key == null) {
    		throw new OBValidationException(CoreTransactionErrorCode.APPSET_MISSING_KEY.getCode());
    	}
    	
    	if (this.key.length() == 0 || key.length() > MAX_KEY_LENGTH) {
    		throw new OBValidationException(CoreTransactionErrorCode.APPSET_MISSING_KEY.getCode());
    	}
    	
    	if (this.value != null &&
    		this.value.length() > MAX_VALUE_LENGTH) {
    		throw new OBValidationException(CoreTransactionErrorCode.APPSET_MISSING_VALUE.getCode());
    	}
    }
    
    public void save() {
        getRepository().save(this);
    }
    
    @Transient
    public ApplicationSettingRepositoryBean getRepository() {
        return (ApplicationSettingRepositoryBean) ApplicationContextFactory.getBean(ApplicationSettingRepository.BEAN_NAME);
    }

}
