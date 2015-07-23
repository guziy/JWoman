package main.model;

import org.joda.time.Days;
import org.joda.time.LocalDate;


public class Period {
	
	private LocalDate startDate, endDate;
	private int recurrenceDays = 30;
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
	

	public Period(LocalDate startDate, int durationDays, int recurrenceDays) {
		this.setStartDate(startDate);
		this.setEndDate(startDate.plusDays(durationDays));
		if (recurrenceDays > 0) this.setRecurrenceDays(recurrenceDays);
	}
	
	public Period(LocalDate startDate, LocalDate endDate) {
		this.setStartDate(startDate);
		this.setEndDate(endDate);
		this.setRecurrenceDays(30);
	}
	
	public Period(LocalDate startDate, LocalDate endDate, int recurrenceDays) {
		this.setStartDate(startDate);
		this.setEndDate(endDate);
		if (recurrenceDays > 0) {
			this.setRecurrenceDays(recurrenceDays);
		}		
	}
	
	public String toString() {
		return "Period: "+ startDate + ", " + endDate;
	}
	
	public Period createNextPeriod(){
		LocalDate newStart = startDate.plusDays(recurrenceDays);
		LocalDate newEnd = endDate.plusDays(recurrenceDays);
        return new Period(newStart, newEnd, recurrenceDays);
	}

	public int getRecurrenceDays() {
		return recurrenceDays;
	}


	public void setRecurrenceDays(int recurrenceDays) {
		this.recurrenceDays = recurrenceDays;
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

    /**
     *
     * @return true if the periods have passed
     */
    public boolean isPassed(){
        return this.endDate.isBefore(new LocalDate());
    }


    public boolean isFuture(){
        return this.startDate.isAfter(new LocalDate());
    }

    /**
     *
     * @return true if the periods are in progress
     */
    public boolean isActive(){
        return !(isPassed() || isFuture());
    }


    public int getDurationDays() {
        return Days.daysBetween(startDate, endDate).getDays();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }

        if (!(obj instanceof Period)){
            return false;
        }

        Period another = (Period) obj;
        return startDate.equals(another.getStartDate()) && endDate.equals(another.getEndDate());
    }
}
