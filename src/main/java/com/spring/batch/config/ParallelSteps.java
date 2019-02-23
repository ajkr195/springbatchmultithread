package com.spring.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

public class ParallelSteps {
//executing steps (step1,step2) in parallel with step3
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Bean
	public Job job() {
	    return jobBuilderFactory.get("job")
	        .start(splitFlow())
//	        .next(step4())
	        .build()        //builds FlowJobBuilder instance
	        .build();       //builds Job instance
	}

	@Bean
	public Flow splitFlow() {
	    return new FlowBuilder<SimpleFlow>("splitFlow")
	        .split(taskExecutor())
	        .add(flow1(), flow2())
	        .build();
	}

	@Bean
	public Flow flow1() {
	    return new FlowBuilder<SimpleFlow>("flow1")
//	        .start(step1())
//	        .next(step2())
	        .build();
	}

	@Bean
	public Flow flow2() {
	    return new FlowBuilder<SimpleFlow>("flow2")
//	        .start(step3())
	        .build();
	}

	@Bean
	public TaskExecutor taskExecutor(){
	    return new SimpleAsyncTaskExecutor("spring_batch");
	}
}
