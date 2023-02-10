package com.onbelay.core.test.serviceimpl;

import com.onbelay.core.entity.serviceimpl.BaseDomainService;

public class CoreTestServiceBean extends BaseDomainService {
	public void initializeSession() {
		super.initializeSession("fred");
	}
	

}
