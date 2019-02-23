package com.spring.batch.config;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

//	To Enable the Scheduling uncomment @EnableScheduling in the Main Application class
	
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000)//(cron = "0 */1 * * * ?")
    public void reportCurrentTime() {
    	log.info("A Scheduled Task keeps running for ever...!!!");
        log.info("The time is now {}", dateFormat.format(new Date()));
    }
    
}