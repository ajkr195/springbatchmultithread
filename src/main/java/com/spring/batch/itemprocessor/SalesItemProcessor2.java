package com.spring.batch.itemprocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

import com.spring.batch.model.Sales;

public class SalesItemProcessor2 implements ItemProcessor<Sales, Sales> {

	private static final Logger log = LoggerFactory.getLogger(SalesItemProcessor2.class);

	@Override
	public Sales process(final Sales sales) throws Exception {
		final String region = sales.getRegion().toLowerCase();
		final String country = sales.getCountry().toLowerCase();
		final String itemType = sales.getItemtype().toLowerCase();
		final String salesChannel = sales.getSaleschannel().toLowerCase();
		final String orderPriority = sales.getOrderpriority().toLowerCase();
		final String orderDate = sales.getOrderdate().toLowerCase();
		final String orderID = sales.getOrderid().toLowerCase();
		final String shipDate = sales.getShipdate().toLowerCase();
		final String unitsSold = sales.getUnitssold().toLowerCase();
		final String unitPrice = sales.getUnitprice().toLowerCase();
		final String unitCost = sales.getUnitcost().toLowerCase();
		final String totalRevenue = sales.getTotalrevenue().toLowerCase();
		final String totalCost = sales.getTotalcost().toLowerCase();
		final String totalProfit = sales.getTotalprofit().toLowerCase();

		final Sales transformedSales = new Sales(region, country, itemType, salesChannel, orderPriority, orderDate,
				orderID, shipDate, unitsSold, unitPrice, unitCost, totalRevenue, totalCost, totalProfit);

		log.info("Converting (" + sales + ") into (" + transformedSales + ")");

		return transformedSales;
	}

}