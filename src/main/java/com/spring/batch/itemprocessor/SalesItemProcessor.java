package com.spring.batch.itemprocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

import com.spring.batch.model.Sales;

public class SalesItemProcessor implements ItemProcessor<Sales, Sales> {

	private static final Logger log = LoggerFactory.getLogger(SalesItemProcessor.class);

	@Override
	public Sales process(final Sales sales) throws Exception {
		final String region = sales.getRegion().toUpperCase();
		final String country = sales.getCountry().toUpperCase();
		final String itemType = sales.getItemtype().toUpperCase();
		final String salesChannel = sales.getSaleschannel().toUpperCase();
		final String orderPriority = sales.getOrderpriority().toUpperCase();
		final String orderDate = sales.getOrderdate().toUpperCase();
		final String orderID = sales.getOrderid().toUpperCase();
		final String shipDate = sales.getShipdate().toUpperCase();
		final String unitsSold = sales.getUnitssold().toUpperCase();
		final String unitPrice = sales.getUnitprice().toUpperCase();
		final String unitCost = sales.getUnitcost().toUpperCase();
		final String totalRevenue = sales.getTotalrevenue().toUpperCase();
		final String totalCost = sales.getTotalcost().toUpperCase();
		final String totalProfit = sales.getTotalprofit().toUpperCase();

		final Sales transformedSales = new Sales(region, country, itemType, salesChannel, orderPriority, orderDate,
				orderID, shipDate, unitsSold, unitPrice, unitCost, totalRevenue, totalCost, totalProfit);

		log.info("Converting (" + sales + ") into (" + transformedSales + ")");

		return transformedSales;
	}

}