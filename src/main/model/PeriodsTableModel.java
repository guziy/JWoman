package main.model;

import main.Configuration;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PeriodsTableModel extends AbstractTableModel implements ActionListener{

	private List<Period> periods = new ArrayList<Period>();
	private String[] columnNames = new String[]{
			"Start date", "End date", "Status"
	};


    private LocalDate currentDate;

    public static final int STATUS_COLUMN_INDEX = 2;

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == STATUS_COLUMN_INDEX){
            return String.class;
        } else {
            return java.time.LocalDate.class;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        boolean notValid = false;
        Period p = periods.get(rowIndex);
        LocalDate newDate = null;
        if (columnIndex == 0 || columnIndex == 1){
            newDate = (LocalDate) aValue;
            if (columnIndex == 0){
                if (p.getStartDate().equals(newDate)) return;
                notValid = p.getEndDate().isBefore(newDate);
            } else {
                if (p.getEndDate().equals(newDate)) return;
                notValid = p.getStartDate().isAfter(newDate);
            }
        }
        if (notValid){
            JOptionPane.showMessageDialog(mc.getMainWindow(), "The date is not valid. " +
                    "Please make sure that the start date of the period is before the end date.");
        } else {

            if (columnIndex == 0){

                p.setStartDate(newDate);

                if (rowIndex == 0) {
                    if (periods.size() >= 2) {
                        int newRecDays = Days.daysBetween(periods.get(rowIndex + 1).getStartDate(), newDate).getDays();
                        p.setRecurrenceDays(newRecDays);
                        p.setEndDate(periods.get(rowIndex + 1).getEndDate().plusDays(p.getRecurrenceDays()));
                    }
                } else {
                    Period pNext = periods.get(rowIndex - 1);

                    if (pNext.isFuture()){
                        pNext.setStartDate(p.getStartDate().plusDays(p.getRecurrenceDays()));
                        pNext.setEndDate(p.getEndDate().plusDays(p.getRecurrenceDays()));
                        pNext.setRecurrenceDays(p.getRecurrenceDays());
                    }
                    fireTableRowsUpdated(rowIndex - 1, rowIndex);
                }
            } else if(columnIndex == 1){

                p.setEndDate(newDate);
                //Change the duration for all future periods
                int durationDays = p.getDurationDays();
                Period thePeriod;
                for (int index = rowIndex - 1; rowIndex > 0; rowIndex--){
                    thePeriod = periods.get(index);
                    if (thePeriod.isFuture()) {
                        thePeriod.setEndDate(thePeriod.getStartDate().plusDays(durationDays));
                    }
                }
                fireTableRowsUpdated(0, rowIndex);
            }

            fireTableCellUpdated(rowIndex, columnIndex);
        }

    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != STATUS_COLUMN_INDEX;
    }

    ModelController mc;

    public Period getPeriodAt(int row){
        return periods.get(row);
    }

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}



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
			
			int nPeriods;
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

        try {
            if (e.getSource() instanceof JTextField) {
                onChangeOfNumsOfPeriodsToShow((JTextField) e.getSource());
            }
        }catch (SQLException se){
            se.printStackTrace();
        }
	}


    public void addPeriod(Period period) throws SQLException {
        //periods.add(period);
        mc.addNewPeriod(period);
        fireTableDataChanged();
        //fireTableRowsInserted(0, 0);
    }
}
