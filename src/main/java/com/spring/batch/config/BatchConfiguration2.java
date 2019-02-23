package com.spring.batch.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
//import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.retry.annotation.EnableRetry;

//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import com.spring.batch.itemprocessor.SalesItemProcessor2;
import com.spring.batch.listener.InterceptingJobExecution;
import com.spring.batch.model.Sales;

@Configuration
@EnableBatchProcessing
@EnableRetry
public class BatchConfiguration2 {

	private static final Logger log = LoggerFactory.getLogger(BatchConfiguration2.class);
	private static String FILENAME = "salesreport.csv";
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	InterceptingJobExecution interceptingJob;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	public InterceptingJobExecution jobExecution;

	@Autowired
	private DataSource dataSource;

	@Bean
	public SalesItemProcessor2 processor2() {
		return new SalesItemProcessor2();
	}

	@Bean(destroyMethod = "")
	public JdbcCursorItemReader<Sales> reader2() {
		log.info("BATCH CONFIG 2 - Reading DB values.........");
		JdbcCursorItemReader<Sales> reader2 = new JdbcCursorItemReader<Sales>();
//		reader2.setVerifyCursorPosition(true);
		reader2.setVerifyCursorPosition(false);
		reader2.setDataSource(dataSource);
		reader2.setSql("SELECT region, country, itemType, salesChannel, orderPriority, orderDate, orderID, shipDate, "
				+ "unitsSold, unitPrice, unitCost, totalRevenue, totalCost, totalProfit FROM salesreport");
		reader2.setRowMapper(new SalesRowMapper());

		return reader2;
	}

	public class SalesRowMapper implements RowMapper<Sales> {
		@Override
		public Sales mapRow(ResultSet rs, int rowNum) throws SQLException {
			Sales sales = new Sales();
			sales.setRegion(rs.getString("region"));
			sales.setCountry(rs.getString("country"));
			sales.setItemtype(rs.getString("itemtype"));
			sales.setSaleschannel(rs.getString("saleschannel"));
			sales.setOrderpriority(rs.getString("orderpriority"));
			sales.setOrderdate(rs.getString("orderdate"));
			sales.setOrderid(rs.getString("orderid"));
			sales.setShipdate(rs.getString("shipdate"));
			sales.setUnitssold(rs.getString("unitssold"));
			sales.setUnitprice(rs.getString("unitprice"));
			sales.setUnitcost(rs.getString("unitcost"));
			sales.setTotalrevenue(rs.getString("totalrevenue"));
			sales.setTotalcost(rs.getString("totalcost"));
			sales.setTotalprofit(rs.getString("totalprofit"));

			return sales;
		}

	}

	@Bean
	public FlatFileItemWriter<Sales> writer2() {
		log.info("BATCH CONFIG 2 - Exporting DB values to CSV file.........");
		FlatFileItemWriter<Sales> writer2 = new FlatFileItemWriter<Sales>();
//		writer2.setResource(new ClassPathResource("salesreport.csv"));
//		writer2.setResource(new FileSystemResource(new File("salesreport.csv")));
		writer2.setResource(new FileSystemResource(FILENAME));
		writer2.setShouldDeleteIfExists(true);
		writer2.setAppendAllowed(false);
//		writer2.setHeaderCallback(headerCallback());
//		writer2.setFooterCallback(footerCallback());
//		writer2.setLineAggregator(lineAggregator());

		writer2.setLineAggregator(new DelimitedLineAggregator<Sales>() {
			{
				setDelimiter(",");
				setFieldExtractor(new BeanWrapperFieldExtractor<Sales>() {
					{
						setNames(new String[] { "region", "country", "itemtype", "saleschannel", "orderpriority",
								"orderdate", "orderid", "shipdate", "unitssold", "unitprice", "unitcost",
								"totalrevenue", "totalcost", "totalprofit" });
					}
				});
			}
		});

		return writer2;
	}

	@Bean
	public Step step5() {
		return stepBuilderFactory.get("step5").<Sales, Sales>chunk(200).reader(reader2()).processor(processor2())
				.writer(writer2()).taskExecutor(taskExecutor2()).build();
	}

	@Bean
	public Job exportSalesJob() {
		return jobBuilderFactory.get("exportSalesJob").incrementer(new RunIdIncrementer()).flow(step5()).end()
				.listener(jobExecution).listener(jobExecutionListener2()).build();
	}

	@Bean
	public JobExecutionListener jobExecutionListener2() {
		return new JobExecutionListener() {
			@Override
			public void beforeJob(JobExecution jobExecution) {
				log.info(
						"This message is \"before\" the start of job. You might want to do something here - Before the job.");
			}

			@Override
			public void afterJob(JobExecution jobExecution) {
				log.info("This message is \"after\" the job. You might want to do something here - After the job.");

				log.info("Lets verify the generated output csv file");
				try (Stream<String> stream = Files.lines(Paths.get(FILENAME))) {

					stream.forEach(System.out::println);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
	}
	
	@Bean
	public TaskExecutor taskExecutor2(){
	    SimpleAsyncTaskExecutor asyncTaskExecutor=new SimpleAsyncTaskExecutor("exportSalesJob");
	    asyncTaskExecutor.setConcurrencyLimit(5);
	    return asyncTaskExecutor;
	}

//	public void javaStream() {
//		List<String> list = new ArrayList<>();
//		try (Stream<String> stream = Files.lines(Paths.get(FILENAME))) {
//			// 1. filter line 3 //2. convert all content to upper case //3. convert it into
//			// a List
//			list = stream.filter(line -> !line.startsWith("line3")).map(String::toUpperCase)
//					.collect(Collectors.toList());
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		list.forEach(System.out::println);
//	}

}
