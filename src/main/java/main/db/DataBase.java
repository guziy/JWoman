package main.db;


import main.model.Period;
import org.joda.time.LocalDate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBase {

	private String path;

	private Connection connection;

	
	
	private static final String RECCURENCE_DAYS_COLNAME = "reccurrence_days";
	private static final String PEROIDS_TABLE_NAME = "periods";
	
	public DataBase(String path) {
		this.path = path;
		createDbIfNeeded();
	}

	/**
	 * Automatically creates the db file if it does not exist
	 */
	private void createDbIfNeeded() {
		connection = null;
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + path);
			
			Statement stmt = connection.createStatement();			
			String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='"+ PEROIDS_TABLE_NAME +"';";
			
			ResultSet res = stmt.executeQuery(query);
			
			//If the table does not exist -> create it
			if (!res.next()){
				query = "CREATE TABLE periods(" 
					   + "start_date date," 
					   + "end_date date," 
					   + RECCURENCE_DAYS_COLNAME + " int);";
				stmt.executeUpdate(query);
			}
			res.close();
			stmt.close();
			
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Opened database successfully");
	}
	
	
	
	
	
	
	/**
	 * Execute when the application exits
	 * @throws SQLException
	 */
	public void close() throws SQLException{
		connection.close();
	}
	
	
	public int getTotalNumberOfPeriods() throws SQLException{
		
		String query = "select count(*) as rowcount from " + PEROIDS_TABLE_NAME;
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		return rs.getInt("rowcount");
		
		
	}
	
	
	/**
	 * Get nPeriods of last periods sorted in descending order (i.e latest first)
	 * @param nPeriods
	 * @return
	 * @throws SQLException
	 */
	public List<Period> getNLastPeriods(int nPeriods) throws SQLException{
		Statement stmt = connection.createStatement();
			
		ResultSet rs;
		List<Period> results = new ArrayList<Period>();
		
		String query = "select rowid,* from "
				+ PEROIDS_TABLE_NAME
				+ " order by start_date DESC limit " + nPeriods + 
				";";
		
		rs = stmt.executeQuery(query);
		
		LocalDate start, end;
		int row;
		Period p;
		while (rs.next()){
			row = rs.getRow();
			start = new LocalDate(rs.getDate("start_date").getTime());
			end = new LocalDate(rs.getDate("end_date").getTime());
			
			p = new Period(start, end, rs.getInt(RECCURENCE_DAYS_COLNAME));
			//System.out.println("Got " + p + " from a DB.");
			p.setDbRow(row);
			results.add(p);
		}
		rs.close();
		stmt.close();		
		return results;	
	}	
	
	
	/**
	 * Save the period to db
	 * @param p
	 * @throws SQLException
	 */
	public void saveNewPeriod(Period p) throws SQLException{
		PreparedStatement pstmt = connection.prepareStatement(
                "insert into " + PEROIDS_TABLE_NAME + "(start_date, end_date, " + RECCURENCE_DAYS_COLNAME + ")" +
                        "values(?,?,?)");
		
		pstmt.setDate(1, new java.sql.Date(p.getStartDate().toDate().getTime()));
		pstmt.setDate(2, new java.sql.Date(p.getEndDate().toDate().getTime()));
		pstmt.setInt(3, p.getRecurrenceDays());
		
		
		System.out.println("Saving " + p);
		pstmt.executeUpdate();
        //Set the flag that the period is already saved into the db
        System.out.println("Before: id = " + p.getDbRow());
        p.setDbRow(pstmt.getGeneratedKeys().getRow());
        System.out.println("After: id = " + p.getDbRow());
	}

	/**
	 * If new periods were added update them
	 * @param currentPeriodsList - current list of periods in memory, some might be new and some might already exist
	 * @throws SQLException
	 */
	public void updatePeriodsInDb(List<Period> currentPeriodsList) throws SQLException {
		if (currentPeriodsList == null) return;

		PreparedStatement pstmt = connection.prepareStatement(
				"update " + PEROIDS_TABLE_NAME + " set start_date=?, end_date=?, " + RECCURENCE_DAYS_COLNAME + "=?" + " where rowid=?;");
		
		for (Period p: currentPeriodsList){
			if (p.getDbRow() == -1){
				saveNewPeriod(p);
			} else {
				pstmt.setDate(1, new java.sql.Date(p.getStartDate().toDate().getTime()));
				pstmt.setDate(2, new java.sql.Date(p.getEndDate().toDate().getTime()));
				pstmt.setInt(3, p.getRecurrenceDays());
				pstmt.setInt(4, p.getDbRow());
				pstmt.executeUpdate();
			}
		}
		pstmt.close();
	}

    /**
     *
     * @param period - remove the period object from the database
     * @throws SQLException
     */
	public void removePeriod(Period period) throws SQLException {
		// The period is not in the database (only in memory)
        if (period.getDbRow() == -1) {
            return;
        }

        PreparedStatement pstmt = connection.prepareStatement(
                "delete from " + PEROIDS_TABLE_NAME + " where rowid=?;");

        pstmt.setInt(1, period.getDbRow());
        pstmt.execute();
        pstmt.close();
	}

    public List<Period> getPeriodsForDate(LocalDate localDate) throws SQLException {

        PreparedStatement pstmt = connection.prepareStatement(
                "select * from " + PEROIDS_TABLE_NAME + " where start_date=?;");

        pstmt.setDate(1, new java.sql.Date(localDate.toDate().getTime()));

        ResultSet rs = pstmt.executeQuery();

        List<Period> results = new ArrayList<Period>();
        Period p;
        int dbRow;
        LocalDate start, end;
        while (rs.next()){
            dbRow = rs.getRow();
            start = new LocalDate(rs.getDate("start_date").getTime());
            end = new LocalDate(rs.getDate("end_date").getTime());

            p = new Period(start, end, rs.getInt(RECCURENCE_DAYS_COLNAME));
            //System.out.println("Got " + p + " from a DB.");
            p.setDbRow(dbRow);
            results.add(p);
        }
        rs.close();
        pstmt.close();
        return results;

    }
}
