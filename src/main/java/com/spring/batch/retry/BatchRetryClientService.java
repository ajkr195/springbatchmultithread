package com.spring.batch.retry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//@Service
public class BatchRetryClientService {

	@Autowired
	private BatchRetryService sampleRetryService;

	public String callRetryService() throws CustomTypeOneException, CustomTypeTwoException {
		return sampleRetryService.retryWhenException();
	}

}