package main.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.joda.time.DateTime;

public class PeriodsTableModel extends AbstractTableModel {

	List<Period> periods = new ArrayList<Period>();
	private String[] columnNames = new String[]{
			"Start date", "End date", "Status"
	};
	
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	private DateTime currentDate;
	
	
	public PeriodsTableModel(List<Period> periods) {
		currentDate = new DateTime();
		this.periods = periods;
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
		
		Period p = periods.get(rowIndex);
		
		switch (columnIndex) {
		
			case 0:
				return p.getStartDate();
			
			case 1:
				return p.getEndDate();
				
			case 2:
				if (p.getStartDate().isBefore(currentDate) && p.getEndDate().isAfter(currentDate)){
					return "red";
				} else if (p.getEndDate().isBefore(currentDate)){
					return "yellow";
				} else if (p.getStartDate().isBefore(currentDate)){
					return "green";
				}
		}		
		return null;
	}
	
	

}
