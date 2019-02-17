# Spring Batch with Spring Cloud DataFlow Procedure

Download these jars - 

https://repo.spring.io/release/org/springframework/cloud/spring-cloud-dataflow-server-local/1.7.4.RELEASE/spring-cloud-dataflow-server-local-1.7.4.RELEASE.jar


http://repo.spring.io/milestone/org/springframework/cloud/spring-cloud-dataflow-shell/1.7.4.RELEASE/spring-cloud-dataflow-shell-1.7.4.RELEASE.jar


Run the first jars  


java -jar spring-cloud-dataflow-server-local-1.7.4.RELEASE.jar

or 

Use your own database (for example, Oracle/MySQL)

java -jar spring-cloud-dataflow-server-local-1.7.4.RELEASE.jar --spring.datasource.url=jdbc:mysql://linuxpc:3306/batchdbmultithread --spring.datasource.username=root --spring.datasource.password=root  --spring.datasource.driver-class-name=org.mariadb.jdbc.Driver --spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL55Dialect

Browse to http://localhost:9393/dashboard



Run the second jar:

java -jar spring-cloud-dataflow-shell-1.7.4.RELEASE.jar


You will get -- 

Dataflow Shell Prompt > .........Here you can register your app .........


For example,

app register --name <nameofyourapp> --type source --uri maven://<packagename>:yourlocal-source:jar:0.0.1-SNAPSHOT


Remote Maven:

app register --name batchdbmultithread --type task --uri maven://com.spring.batch:springbatch:jar:batchdbmultithread-0.0.1-SNAPSHOT


Local Maven:

app register --name batchdbmultithread --type task --uri file:///parth/to/your/batchdbmultithread-0.0.1-SNAPSHOT.jar


task create batchdbmultithread --definition batchdbmultithread
