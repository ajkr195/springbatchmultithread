package com.spring.batch.retry;

import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

//@Configuration
//@EnableBatchProcessing 
public class ErrorProneTask2 {

	private final JobBuilderFactory jobs;

	private final StepBuilderFactory steps;

	@Autowired
	public ErrorProneTask2(JobBuilderFactory jobs, StepBuilderFactory steps) {
		this.jobs = jobs;
		this.steps = steps;
	}

	@Bean
	public Step step() {
		return steps.get("step")
				.tasklet((contribution, chunkContext) -> {
					Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();
					String name = (String) jobParameters.get("name");
					System.out.println("Hello " + name);
					throw new Exception("Boom!");
				})
				.build();
	}

	@Bean
	public Job job() {
		return jobs.get("job")
				.start(step())
				.build();
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Throwable {
		RetryTemplate retryTemplate = new RetryTemplate();
		retryTemplate.setRetryPolicy(new SimpleRetryPolicy(3));

		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ErrorProneTask2.class);
		JobLauncher jobLauncher = applicationContext.getBean(JobLauncher.class);
		Job job = applicationContext.getBean(Job.class);
		JobParameters jobParameters = new JobParametersBuilder().addString("name", "world").toJobParameters();

		retryTemplate.execute(retryContext -> {
			JobExecution jobExecution = jobLauncher.run(job, jobParameters);
			if (!jobExecution.getAllFailureExceptions().isEmpty()){
                                System.out.println("Job failed, retrying..");
				throw jobExecution.getAllFailureExceptions().iterator().next();
			}
			return jobExecution;
		});
	}

}