package main.gui;


import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;
import javafx.scene.control.DatePicker;
import main.model.PeriodsTableModel;
import org.joda.time.LocalDate;
import org.joda.time.ReadableDateTime;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.Date;

public class PeriodsTableEditor extends AbstractCellEditor implements TableCellEditor{

    JDateChooser editor;

    public PeriodsTableEditor(){
        editor = new JDateChooser();
    }




    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

        Date date;
        if (value instanceof LocalDate){
           date = ((LocalDate) value).toDate();
        } else {
            return null;
        }

        editor.setDate(date);
        return editor;
    }

    @Override
    public Object getCellEditorValue() {
        return new LocalDate(editor.getDate());
    }
}
