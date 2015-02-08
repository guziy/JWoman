package main.gui;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class PeriodsTableRenderer extends DefaultTableCellRenderer{

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		// TODO Auto-generated method stub
		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, column);
		
		if (value == null) return c;
		
		if (value.getClass() == DateTime.class){
			DateTimeFormatter fmt = DateTimeFormat.forPattern("E, d-MMMM-yyyy");
			
			JLabel jl = (JLabel) c;
			jl.setText(fmt.print((DateTime) value));
			
		}
		
		return c;
	}

	
}
