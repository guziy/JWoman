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
    private final DateTimeFormatter dateTimeFormatter;

    public PeriodsTableRenderer(PeriodsTableModel ptm){
        this.ptm = ptm;
        dateTimeFormatter = DateTimeFormat.forPattern("dd-MMM-yyyy");
    }

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, column);
		
		if (value == null) return c;


        JLabel jl = (JLabel) c;

		if (value.getClass() == LocalDate.class){
            jl.setText(dateTimeFormatter.print( (LocalDate) value));
		}


        if (!isSelected) {
            if (row % 2 == 1) {
                c.setBackground(Color.decode("0xdddddd"));
            } else {
                c.setBackground(Color.white);
            }
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

        jl.setHorizontalAlignment(JLabel.CENTER);
        jl.setFont(jl.getFont().deriveFont(Font.PLAIN, 14));
		
		return c;
	}

	
}
