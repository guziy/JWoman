package main.model;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import main.Configuration;
import main.db.DataBase;

public class ModelController {
	
	private List<Period> currentPeriodsList;
	private Date showPeriodsAfter, showPeriodsBefore;
	
	
	private DataBase db;
	private Configuration configuration = Configuration.getConfiguration();
	
	
	public ModelController() throws SQLException {
		db = configuration.getDb();
		currentPeriodsList = db.getNLastPeriods(30);
	}
	
	public List<Period> getPeriodsForDates(Date start, Date end){	
		return null;
	}
	
	
	/**
	 * Save all the periods to db (update if needed)
	 * @throws SQLException 
	 */
	public void save() throws SQLException {
		//TODO: ...
		db.updatePeriodsInDb(currentPeriodsList);
	}
	
	
	private void addNewPeriodIfNeeded(List<Period> periods){
		//Add a new period if necessary
		LocalDate currentDate = new LocalDate();
		Period previous, next;
		if (periods.size() == 0){
			previous = new Period(currentDate);
			periods.add(previous);
			periods.add(0, previous.createNextPeriod());
		} else{
			previous = periods.get(0);
			next = previous;
			while (next.getStartDate().isBefore(currentDate) || next.getStartDate().isEqual(currentDate)){
				next = next.createNextPeriod();
			}
			if (next != previous) {
				periods.add(0, next);
			}
		}		
	}
	
	/**
	 * Get nPeriods of the latest periods sorted to have the latest period first
	 * @param nPeriods
	 * @return
	 * @throws SQLException
	 */
	public List<Period> getNLastPeriods(int nPeriods) throws SQLException{
		
		if (nPeriods <= currentPeriodsList.size()){
			addNewPeriodIfNeeded(currentPeriodsList);
			return currentPeriodsList.subList(0, nPeriods);		
		} 
		
		currentPeriodsList = db.getNLastPeriods(nPeriods);
		addNewPeriodIfNeeded(currentPeriodsList);		
		return currentPeriodsList;
	}
	
	
	public void createTestData() throws SQLException{
		int reccurrence = 30; //days
		LocalDate start = new LocalDate(1980, 1, 1);
		Period p0 = new Period(start, start.plusDays(5), reccurrence);
		Period p = p0;
		for (int i = 0; i < 100; i++){
			db.saveNewPeriod(p);
			p = p.createNextPeriod();
		}
	}
	
	
	public void close() throws SQLException{
		db.close();
	}
	
	
	
	

}
