package com.spring.batch.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class BatchRetryService {
	private static final Logger LOGGER = LoggerFactory.getLogger(BatchRetryService.class);

	private static int COUNTER = 0;

	@Retryable(value = { CustomTypeOneException.class,
			CustomTypeTwoException.class }, maxAttempts = 5, backoff = @Backoff(5000))
	public String retryWhenException() throws CustomTypeOneException, CustomTypeTwoException {
		COUNTER++;
		LOGGER.info("COUNTER = " + COUNTER);

		if (COUNTER == 1)
			throw new CustomTypeOneException();
		else if (COUNTER == 2)
			throw new CustomTypeTwoException();
		else
			throw new RuntimeException();
	}

	@Recover
	public String recover(Throwable t) {
		LOGGER.info("BatchRetryService.recover");
		return "Error Class :: " + t.getClass().getName();
	}
}