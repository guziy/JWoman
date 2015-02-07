package main.model;

import org.joda.time.DateTime;


public class Period {
	
	private DateTime startDate, endDate;
	private int reccurrenceDays = 30;
	private static final int defaultPeriodDurationDays = 5;
	
	private int dbRow = -1;
	
	public int getDbRow() {
		return dbRow;
	}

	public void setDbRow(int dbRow) {
		this.dbRow = dbRow;
	}
	
	
	public Period(DateTime startDate){
		this.setStartDate(startDate);
		this.setEndDate(startDate.plusDays(defaultPeriodDurationDays));
	}
	

	public Period(DateTime startDate, int durationDays, int reccurrenceDays) {
		this.setStartDate(startDate);
		this.setEndDate(startDate.plusDays(durationDays));
		if (reccurrenceDays > 0) this.setReccurrenceDays(reccurrenceDays);
	}
	
	public Period(DateTime startDate, DateTime endDate) {
		this.setStartDate(startDate);
		this.setEndDate(endDate);
		this.setReccurrenceDays(30);
	}
	
	public Period(DateTime startDate, DateTime endDate, int reccurrenceDays) {
		this.setStartDate(startDate);
		this.setEndDate(endDate);
		if (reccurrenceDays > 0) {
			this.setReccurrenceDays(reccurrenceDays);
		}		
	}
	
	public String toString() {
		return "Period: "+ startDate + ", " + endDate;
	}
	
	public Period createNextPeriod(){
		DateTime newStart = startDate.plusDays(reccurrenceDays);
		DateTime newEnd = endDate.plusDays(reccurrenceDays);
		
		Period p = new Period(newStart, newEnd, reccurrenceDays);
		return p;
	}

	public int getReccurrenceDays() {
		return reccurrenceDays;
	}


	public void setReccurrenceDays(int reccurrenceDays) {
		this.reccurrenceDays = reccurrenceDays;
	}


	public DateTime getStartDate() {
		return startDate;
	}


	public void setStartDate(DateTime startDate) {
		this.startDate = startDate;
	}


	public DateTime getEndDate() {
		return endDate;
	}


	public void setEndDate(DateTime endDate) {
		this.endDate = endDate;
	}
	
	
	
	
}
