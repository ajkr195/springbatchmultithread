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
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
//import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

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
	
	@Value("${mycustom.batch.chunk.size}")
	private int mycustombatchchunksize;
	@Value("${mycustom.batch.concurrency.size}")
	private int mycustombatchconcurrencysize;
	@Value("${mycustom.batch.throttle.limit}")
	private int mycustombatchthrottlelimit;
	@Value("${mycustom.batch.maxpool.size}")
	private int mycustombatchmaxpoolsize;
	@Value("${mycustom.batch.corepool.size}")
	private int mycustombatchcorepoolsize;
	@Value("${mycustom.batch.queuecapacity.size}")
	private int mycustombatchqueuecapacitysize;
	
	
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
		log.info("BATCH CONFIG 2 - Reading Batch Configurations");
		JdbcCursorItemReader<Sales> reader2 = new JdbcCursorItemReader<Sales>();
//		reader2.setVerifyCursorPosition(true);
		reader2.setVerifyCursorPosition(false);
		reader2.setDataSource(dataSource);
		reader2.setSql("SELECT salesreport.region, salesreport.country, salesreport.itemType, salesreport.salesChannel, salesreport.orderPriority, salesreport.orderDate, salesreport.orderID, salesreport.shipDate, "
				+ "salesreport.unitsSold, salesreport.unitPrice, salesreport.unitCost, salesreport.totalRevenue, salesreport.totalCost, salesreport.totalProfit FROM salesreport");
		reader2.setRowMapper(new SalesRowMapper());

		return reader2;
	}

	public class SalesRowMapper implements RowMapper<Sales> {
		@Override
		public Sales mapRow(ResultSet rs, int rowNum) throws SQLException {
			Sales sales = new Sales();
			sales.setRegion(rs.getString(1));//("region"));
			sales.setCountry(rs.getString(2));//("country"));
			sales.setItemtype(rs.getString(3));//("itemtype"));
			sales.setSaleschannel(rs.getString(4));
			sales.setOrderpriority(rs.getString(5));
			sales.setOrderdate(rs.getString(6));
			sales.setOrderid(rs.getString(7));
			sales.setShipdate(rs.getString(8));
			sales.setUnitssold(rs.getString(9));
			sales.setUnitprice(rs.getString(10));
			sales.setUnitcost(rs.getString(11));
			sales.setTotalrevenue(rs.getString(12));
			sales.setTotalcost(rs.getString(13));
			sales.setTotalprofit(rs.getString(14));
			return sales;
		}

	}

	@Bean
	public FlatFileItemWriter<Sales> writer2() {
		log.info("BATCH CONFIG 2 - Export Configuration");
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
	public TaskExecutor taskExecutor2(){
	    SimpleAsyncTaskExecutor asyncTaskExecutor=new SimpleAsyncTaskExecutor("EXPORTSALES");
	    asyncTaskExecutor.setConcurrencyLimit(mycustombatchconcurrencysize);
	    return asyncTaskExecutor;
	}
	
	@Bean
	public ThreadPoolTaskExecutor secondthreadpooltaskExecutor() {
		ThreadPoolTaskExecutor secondtaskExecutor = new ThreadPoolTaskExecutor();
		secondtaskExecutor.setMaxPoolSize(mycustombatchmaxpoolsize);
		secondtaskExecutor.setCorePoolSize(mycustombatchcorepoolsize);
		secondtaskExecutor.setQueueCapacity(mycustombatchqueuecapacitysize);
		secondtaskExecutor.setThreadNamePrefix("DB2CSV-");
		secondtaskExecutor.afterPropertiesSet();
		return secondtaskExecutor;
	}
	
	@Bean
	public Step step5() {
		return stepBuilderFactory.get("step5").<Sales, Sales>chunk(mycustombatchchunksize).reader(reader2()).processor(processor2())
				.writer(writer2()).taskExecutor(taskExecutor2()).taskExecutor(secondthreadpooltaskExecutor()).throttleLimit(mycustombatchthrottlelimit).build();
	}
	
	@Bean
	public Job exportSalesJob() {
		return jobBuilderFactory.get("exportSalesJob").incrementer(new RunIdIncrementer()).flow(step5()).end()
				.listener(jobExecution).listener(jobExecutionListener2(secondthreadpooltaskExecutor())).build();
	}

	@Bean
	public JobExecutionListener jobExecutionListener2(ThreadPoolTaskExecutor executor) {
		return new JobExecutionListener() {
			private ThreadPoolTaskExecutor taskExecutor = executor;
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
					//stream.forEach(System.out::println);
//			        stream.peek(a -> log.info("Reading generated output csv file line-by-line")).
			        stream.forEach(a -> {log.info("Reading CSV Line  >>  "+a);});
				} catch (IOException e) {
					e.printStackTrace();
				}
				taskExecutor.shutdown();
			}
		};
	}

//	@Bean
//    ItemReader<Sales> reader3(DataSource dataSource) {
//        JdbcPagingItemReader<Sales> reader3 = new JdbcPagingItemReader<>();
//        reader3.setDataSource(dataSource);
//        reader3.setPageSize(1);
//        PagingQueryProvider queryProvider = createQueryProvider();
//        reader3.setQueryProvider(queryProvider);
//        reader3.setRowMapper(new BeanPropertyRowMapper<>(Sales.class));
//        return reader3;
//    }
// 
//    private PagingQueryProvider createQueryProvider() {
//        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
//        queryProvider.setSelectClause("SELECT region, country, itemType, salesChannel, orderPriority, orderDate, orderID, shipDate, unitsSold, unitPrice, unitCost, totalRevenue, totalCost, totalProfit");
//        queryProvider.setFromClause("FROM salesreport");
//        queryProvider.setSortKeys(sortByorderIDAddressAsc());
//        return queryProvider;
//    }
	
}
