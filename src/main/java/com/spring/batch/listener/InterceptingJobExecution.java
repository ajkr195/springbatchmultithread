package com.spring.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

//import org.springframework.batch.core.JobExecution;
//
//public interface JobExecutionListener {
//	 void beforeJob(JobExecution jobExecution);
//	    void afterJob(JobExecution jobExecution);
//
//}

@Component
public class InterceptingJobExecution implements JobExecutionListener {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void beforeJob(JobExecution jobExecution) {
		//
		// Can Log || do some business code
		//
		log.info("Intercepting Job Excution - Before Job!");
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		//
		// Can Log || do some Business code
		//
//		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
//			// job success
//		} else if (jobExecution.getStatus() == BatchStatus.FAILED) {
//			// job failure
//		}
		log.info("Intercepting Job Excution - After Job!");
	}

}