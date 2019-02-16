package com.spring.batch.retry;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.Arrays;

//@Configuration
//@EnableBatchProcessing
public class ErrorProneTask {
  private final JobBuilderFactory jobs;
  private final StepBuilderFactory steps;

  @Autowired
  public ErrorProneTask(JobBuilderFactory jobs, StepBuilderFactory steps) {
    this.jobs = jobs;
    this.steps = steps;
  }

  @Bean
  public Step step() {
    return steps.get("step")
      .<Integer, Integer>chunk(2)
      .reader(itemReader())
      .writer(itemWriter())
      //.faultTolerant()
      //.retryLimit(5)
      //.retry(Exception.class)
      .build();
  }

  @Bean
  public ItemReader<Integer> itemReader() {
    return new ListItemReader<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
  }

  @Bean
  public ItemWriter<Integer> itemWriter() {
    return items -> {
      for (Integer item : items) {
        System.out.println("item = " + item);
        if (item.equals(7)) {
          throw new Exception("Sevens are sometime nasty, let's retry them");
        }
      }
    };
  }
  @Bean
  public Job job() {
    return jobs.get("job")
      .start(step())
      .build();
  }

  public static void main(String[] args) throws Throwable {
    RetryTemplate retryTemplate = new RetryTemplate();
    retryTemplate.setRetryPolicy(new SimpleRetryPolicy(3));
    ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ErrorProneTask.class);
    JobLauncher jobLauncher = applicationContext.getBean(JobLauncher.class);
    Job job = applicationContext.getBean(Job.class);
    //JobParameters jobParameters = new JobParametersBuilder().addString("name", "world").toJobParameters();
    retryTemplate.execute(retryContext -> {
      JobExecution jobExecution = jobLauncher.run(job, new JobParameters());
      if (!jobExecution.getAllFailureExceptions().isEmpty()) {
        System.out.println("Job failed, retrying..");
        throw jobExecution.getAllFailureExceptions().iterator().next();
      }
      return jobExecution;
    });  
   }
}