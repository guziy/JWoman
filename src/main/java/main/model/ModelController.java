package main.model;

import main.Configuration;
import main.MainViewController;
import main.db.DataBase;
import org.joda.time.LocalDate;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.sql.SQLException;
import java.util.List;

public class ModelController implements TableModelListener {
	
	private List<Period> currentPeriodsList;

	
	private DataBase db;
    private MainViewController mvc;


    public JFrame getMainWindow(){
       return mvc.getMainFrame();
    }


	public ModelController(MainViewController mvc) throws SQLException {
        this.mvc = mvc;
        Configuration configuration = Configuration.getConfiguration();
        db = configuration.getDb();
		currentPeriodsList = db.getNLastPeriods(30);
	}
	

	
	/**
	 * Save all the periods to db (update if needed)
	 * @throws SQLException 
	 */
	public void save() throws SQLException {
		//TODO: ...
		db.updatePeriodsInDb(currentPeriodsList);
	}
	
	
	public int getTotalNumberOfPeriods() throws SQLException{
        save();
		return db.getTotalNumberOfPeriods();
	}
	
	
	private void createNewPeriodsIfNeeded(List<Period> periods){
		// Create a new period if necessary
		LocalDate currentDate = new LocalDate();
		Period previous, next;
        if (periods.size() != 0) {
			previous = periods.get(0);
			next = previous;
			while (!next.getStartDate().isAfter(currentDate)){
				next = next.createNextPeriod();
                periods.add(0, next);
			}
		}
    }
	
	/**
	 * Get nPeriods of the latest periods sorted to have the latest period first
	 * @param nPeriods - number of latest periods to fetch
	 * @return list of Period objects representing the latest n periods
	 * @throws SQLException
	 */
	public List<Period> getNLastPeriods(int nPeriods) throws SQLException{
		
		if (nPeriods <= currentPeriodsList.size()){
			// createNewPeriodsIfNeeded(currentPeriodsList);
			return currentPeriodsList.subList(0, nPeriods);		
		} 
		
		currentPeriodsList = db.getNLastPeriods(nPeriods);
		createNewPeriodsIfNeeded(currentPeriodsList);
		return currentPeriodsList;
	}
	
	
	public void createTestData() throws SQLException{
		int recurrence = 30; //days
		LocalDate start = new LocalDate(1980, 1, 1);
        Period p = new Period(start, start.plusDays(5), recurrence);
		for (int i = 0; i < 100; i++){
			db.saveNewPeriod(p);
			p = p.createNextPeriod();
		}
	}
	
	
	public void close() throws SQLException{
		db.close();
	}


    /**
     *
     * @param period to be added
     *               Note: if a period with the same start date already exists in the
     *               system, then nothing will be changed or added to the model
     */
    public void addNewPeriod(Period period) throws SQLException {

        // check at least if the period we are trying to add
        // is not already in the cache

        boolean toAdd = true;

        // the start date of the added period
        LocalDate startDate = period.getStartDate();

        for (Period p: currentPeriodsList) {
            if (p.getStartDate().equals(startDate)) {
                toAdd = false;
                break;
            }
        }


        if (toAdd) {
            // More thoroughly now check if there is a period with the same date in the database
            List<Period> pList = db.getPeriodsForStartDate(startDate);

            if (pList.size() == 0) {
                currentPeriodsList.add(period);
            }
        }

        // Probably should not hit the db on each new period
        // save();
    }

    public void tableChanged(TableModelEvent e) {
        PeriodsTableModel ptm = mvc.getPeriodsTableModel();
        if (ptm.getRowCount() > currentPeriodsList.size()){
            currentPeriodsList.add(0, ptm.getPeriodAt(0));
        }
    }


    /**
     *
     * @param periods - the list of periods to be removed
     * @throws SQLException
     */
	public void removePeriods(List<Period> periods) throws SQLException {
        for (Period p : periods) {
            removePeriod(p);
        }
    }

	public void removePeriod(Period period) throws SQLException {

        // database interface
        db.removePeriod(period);

        // In-memory persistence
		currentPeriodsList.remove(period);
	}


    /**
     *
     * @return Periods sorted by start date in the descending order
     * @throws SQLException
     */
    public List<Period> getAllPeriods() throws SQLException {
        return db.getNLastPeriods(db.getTotalNumberOfPeriods());
    }

    public List<Period> getPeriodsForStartDate(LocalDate startDate) throws SQLException {
        return db.getPeriodsForStartDate(startDate);
    }
}
