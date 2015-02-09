package main.model;

import org.joda.time.LocalDate;
import org.joda.time.ReadableInstant;


public class Period {
	
	private LocalDate startDate, endDate;
	private int reccurrenceDays = 30;
	private static final int defaultPeriodDurationDays = 5;
	
	private int dbRow = -1;
	
	public int getDbRow() {
		return dbRow;
	}

	public void setDbRow(int dbRow) {
		this.dbRow = dbRow;
	}
	
	
	public Period(LocalDate startDate){
		this.setStartDate(startDate);
		this.setEndDate(startDate.plusDays(defaultPeriodDurationDays));
	}
	

	public Period(LocalDate startDate, int durationDays, int reccurrenceDays) {
		this.setStartDate(startDate);
		this.setEndDate(startDate.plusDays(durationDays));
		if (reccurrenceDays > 0) this.setReccurrenceDays(reccurrenceDays);
	}
	
	public Period(LocalDate startDate, LocalDate endDate) {
		this.setStartDate(startDate);
		this.setEndDate(endDate);
		this.setReccurrenceDays(30);
	}
	
	public Period(LocalDate startDate, LocalDate endDate, int reccurrenceDays) {
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
		LocalDate newStart = startDate.plusDays(reccurrenceDays);
		LocalDate newEnd = endDate.plusDays(reccurrenceDays);
		
		Period p = new Period(newStart, newEnd, reccurrenceDays);
		return p;
	}

	public int getReccurrenceDays() {
		return reccurrenceDays;
	}


	public void setReccurrenceDays(int reccurrenceDays) {
		this.reccurrenceDays = reccurrenceDays;
	}


	public LocalDate getStartDate() {
		return startDate;
	}


	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}


	public LocalDate getEndDate() {
		return endDate;
	}


	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
}
