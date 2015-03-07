package main.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import main.Configuration;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class PeriodsTableModel extends AbstractTableModel implements ActionListener{

	List<Period> periods = new ArrayList<Period>();
	private String[] columnNames = new String[]{
			"Start date", "End date", "Status"
	};
	
	ModelController mc;
	
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	private LocalDate currentDate;
	
	
	public PeriodsTableModel(ModelController mc, int nPeriodsToShow) throws SQLException {		
		this.mc = mc;
		this.periods = mc.getNLastPeriods(nPeriodsToShow);
	}
	
	@Override
	public int getRowCount() {
		return periods.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		currentDate = new LocalDate();
		
		Period p = periods.get(rowIndex);
		
		switch (columnIndex) {
		
			case 0:
				return p.getStartDate();
			
			case 1:
				return p.getEndDate();
				
			case 2:
				if (p.getStartDate().isBefore(currentDate) && p.getEndDate().isAfter(currentDate)){
					return Configuration.CURRENT;
				} else if (p.getEndDate().isBefore(currentDate)){
					return Configuration.PAST;
				} else if (p.getStartDate().isAfter(currentDate)){
					return Configuration.UPCOMING;
				}
				
				if (p.getStartDate().equals(currentDate) || p.getEndDate().equals(currentDate)){
					return Configuration.CURRENT;
				}
				
		}		
		return null;
	}

	
	/**
	 * Does not do anything if the newNumOfPeriods is equal to the current one
	 * @param newNumOfPeriods
	 * @throws SQLException
	 */
	public void onChangeOfNumOfPeriodsToShow(int newNumOfPeriods) throws SQLException{
		if (newNumOfPeriods != this.periods.size() && newNumOfPeriods >= 0){
			this.periods = mc.getNLastPeriods(newNumOfPeriods);
			this.fireTableDataChanged();
		}		
		
	}
	
	private void onChangeOfNumsOfPeriodsToShow(JTextField textField) throws SQLException{
			String text = textField.getText();
			text = text.trim();
			if (text.isEmpty()){
				return;
			}
			
			int nPeriods = 0;
			try{
				nPeriods = Integer.parseInt(text);			
			} catch (NumberFormatException nfe){
				nPeriods = this.periods.size();
			}
				
				
			onChangeOfNumOfPeriodsToShow(nPeriods);
			
			if (nPeriods < 0) {
				textField.setText(Integer.toString(this.periods.size()));
			}
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() instanceof JTextField){
			try {
				onChangeOfNumsOfPeriodsToShow((JTextField) e.getSource());
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	

}
