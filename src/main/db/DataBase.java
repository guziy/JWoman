package main.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import main.model.Period;

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
		
		String query = "select * from (select * from periods order by start_date ASC limit " + nPeriods + 
				") order by start_date DESC;";
		
		rs = stmt.executeQuery(query);
		
		DateTime start, end;
		while (rs.next()){
			start = new DateTime(rs.getDate("start_date").getTime());
			end = new DateTime(rs.getDate("end_date").getTime());
			results.add(new Period(start, end, rs.getInt(RECCURENCE_DAYS_COLNAME)));
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
	public void savePeriod(Period p) throws SQLException{
		PreparedStatement pstmt = connection.prepareStatement(
				"insert into "+ PEROIDS_TABLE_NAME +"(start_date, end_date, " + RECCURENCE_DAYS_COLNAME + ")" +
				"values(?,?,?)");
		
		pstmt.setDate(1, new java.sql.Date(p.getStartDate().toDate().getTime()));
		pstmt.setDate(2, new java.sql.Date(p.getEndDate().toDate().getTime()));
		pstmt.setInt(3, p.getReccurrenceDays());
		
		
		System.out.println("Saving " + p);
		pstmt.executeUpdate();
	}
	
	

}