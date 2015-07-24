package main.gui;

import main.model.Period;
import main.model.PeriodsTableModel;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class PeriodsTableRenderer extends DefaultTableCellRenderer{


    private PeriodsTableModel ptm;

    public PeriodsTableRenderer(PeriodsTableModel ptm){
        this.ptm = ptm;
    }

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, column);
		
		if (value == null) return c;

        JLabel jl = (JLabel) c;
		if (value.getClass() == LocalDate.class){
			DateTimeFormatter fmt = DateTimeFormat.forPattern("dd-MMM-yyyy");
			jl.setText(fmt.print( (LocalDate) value));
		}

        if (column == PeriodsTableModel.STATUS_COLUMN_INDEX){
            Period p = ptm.getPeriodAt(row);
            if (p.isPassed()){
                jl.setForeground(Color.GRAY);
            } else if(p.isFuture()){
                jl.setForeground(Color.MAGENTA);
            } else {
                jl.setForeground(Color.RED);
            }
        } else {
            jl.setForeground(Color.BLACK);
        }

		
		return c;
	}

	
}
