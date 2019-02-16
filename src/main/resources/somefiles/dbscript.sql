drop database if exists batchdbmultithread;
create database batchdbmultithread;
use batchdbmultithread;

DROP TABLE  IF EXISTS salesreport;

create table salesreport(
id BIGINT NOT NULL AUTO_INCREMENT,
region VARCHAR(150) NOT NULL,
country VARCHAR(150) NOT NULL,
itemtype VARCHAR(150) NOT NULL,
saleschannel VARCHAR(150) NOT NULL,
orderpriority VARCHAR(150) NOT NULL,
orderdate VARCHAR(150) NOT NULL,
orderid VARCHAR(150) NOT NULL,
shipdate VARCHAR(150) NOT NULL,
unitssold VARCHAR(150) NOT NULL,
unitprice VARCHAR(150) NOT NULL,
unitcost VARCHAR(150) NOT NULL,
totalrevenue VARCHAR(150) NOT NULL,
totalcost VARCHAR(150) NOT NULL,
totalprofit VARCHAR(150) NOT NULL,
PRIMARY KEY (id)) ENGINE=InnoDB;
