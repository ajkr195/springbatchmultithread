package com.spring.batch.model;

public class Sales {
	String region;
	String country;
	String itemtype;
	String saleschannel;
	String orderpriority;
	String orderdate;
	String orderid;
	String shipdate;
	String unitssold;
	String unitprice;
	String unitcost;
	String totalrevenue;
	String totalcost;
	String totalprofit;

	

	public Sales(String region, String country, String itemtype, String saleschannel, String orderpriority,
			String orderdate, String orderid, String shipdate, String unitssold, String unitprice, String unitcost,
			String totalrevenue, String totalcost, String totalprofit) {
		super();
		this.region = region;
		this.country = country;
		this.itemtype = itemtype;
		this.saleschannel = saleschannel;
		this.orderpriority = orderpriority;
		this.orderdate = orderdate;
		this.orderid = orderid;
		this.shipdate = shipdate;
		this.unitssold = unitssold;
		this.unitprice = unitprice;
		this.unitcost = unitcost;
		this.totalrevenue = totalrevenue;
		this.totalcost = totalcost;
		this.totalprofit = totalprofit;
	}



	public Sales() {

	}



	public String getRegion() {
		return region;
	}



	public void setRegion(String region) {
		this.region = region;
	}



	public String getCountry() {
		return country;
	}



	public void setCountry(String country) {
		this.country = country;
	}



	public String getItemtype() {
		return itemtype;
	}



	public void setItemtype(String itemtype) {
		this.itemtype = itemtype;
	}



	public String getSaleschannel() {
		return saleschannel;
	}



	public void setSaleschannel(String saleschannel) {
		this.saleschannel = saleschannel;
	}



	public String getOrderpriority() {
		return orderpriority;
	}



	public void setOrderpriority(String orderpriority) {
		this.orderpriority = orderpriority;
	}



	public String getOrderdate() {
		return orderdate;
	}



	public void setOrderdate(String orderdate) {
		this.orderdate = orderdate;
	}



	public String getOrderid() {
		return orderid;
	}



	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}



	public String getShipdate() {
		return shipdate;
	}



	public void setShipdate(String shipdate) {
		this.shipdate = shipdate;
	}



	public String getUnitssold() {
		return unitssold;
	}



	public void setUnitssold(String unitssold) {
		this.unitssold = unitssold;
	}



	public String getUnitprice() {
		return unitprice;
	}



	public void setUnitprice(String unitprice) {
		this.unitprice = unitprice;
	}



	public String getUnitcost() {
		return unitcost;
	}



	public void setUnitcost(String unitcost) {
		this.unitcost = unitcost;
	}



	public String getTotalrevenue() {
		return totalrevenue;
	}



	public void setTotalrevenue(String totalrevenue) {
		this.totalrevenue = totalrevenue;
	}



	public String getTotalcost() {
		return totalcost;
	}



	public void setTotalcost(String totalcost) {
		this.totalcost = totalcost;
	}



	public String getTotalprofit() {
		return totalprofit;
	}



	public void setTotalprofit(String totalprofit) {
		this.totalprofit = totalprofit;
	}



	@Override
	public String toString() {
		return "Sales [region=" + region + ", country=" + country + ", itemtype=" + itemtype + ", saleschannel="
				+ saleschannel + ", orderpriority=" + orderpriority + ", orderdate=" + orderdate + ", orderid="
				+ orderid + ", shipdate=" + shipdate + ", unitssold=" + unitssold + ", unitprice=" + unitprice
				+ ", unitcost=" + unitcost + ", totalrevenue=" + totalrevenue + ", totalcost=" + totalcost
				+ ", totalprofit=" + totalprofit + "]";
	}

	
}
