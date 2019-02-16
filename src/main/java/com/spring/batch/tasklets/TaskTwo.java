package com.spring.batch.tasklets;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
@Component
public class TaskTwo implements Tasklet {
 @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception
    {
        System.out.println("TaskTwo start..");
 
        // ... your code
         
        System.out.println("TaskTwo done..");
        return RepeatStatus.FINISHED;
    }   
}