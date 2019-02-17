package com.spring.batch.retry;

import org.springframework.boot.SpringApplication;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

//@SpringBootApplication
//@RestController // import spring-boot web for this
//@EnableRetry
public class SimpleRetryExample {
	
	
public static void main(String[] args) {
	SpringApplication.run(SimpleRetryExample.class, args);
}

//RequestMapping(value="",method=RequestMethod.GET)
@Retryable(value= {NumberFormatException.class,NullPointerException.class}, maxAttempts = 5, backoff = @Backoff(2000))
public String myApp() {
	System.out.println("My App API is calling......");
	String str=null;
	str.length();
	return "success";
	
}

@Recover 
public String recover (NumberFormatException nfex) {
	System.out.println("Recover method - Number format Exception");
	return "Recover Method - Number Format Exception";
	
}

@Recover 
public String recover (NullPointerException npex) {
	System.out.println("Recover method - Null Pointer Exception");
	return "Recover Method - Null Pointer Exception";
	
}



}
