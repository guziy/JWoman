package main.gui;

import com.toedter.calendar.JCalendar;
import org.joda.time.LocalDate;

import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;
import java.awt.*;

/**
 * Created by san on 15-03-08.
 *
 */
public class FirstTimeDialog extends JDialog {


    private LocalDate startDate;
    private LocalDate endDate;
    private boolean cancelled = true;


    private JButton okButton, cancelButton;

    public FirstTimeDialog(JFrame mainWindow){
        super(mainWindow);
        setModal(true);
        //setSize(new Dimension(800, 600));
        buildUI();
        pack();
        setVisible(true);

    }

    private void buildUI() {


        setTitle("Please indicate the start and end dates of one of your periods.");

        Container mainPanel = getContentPane();
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();

        mainPanel.setLayout(gbl);

        JLabel startDateLabel = new JLabel("Start date");
        JCalendar startDateCal = new JCalendar();
        startDateCal.setToolTipText("Select start date of the period");

        JLabel endDateLabel = new JLabel("End date");
        JCalendar endDateCal = new JCalendar();
        endDateCal.setToolTipText("Select end date of the period");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());

        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10,10,10,10);
        mainPanel.add(startDateLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10,10,10,10);
        mainPanel.add(endDateLabel, gbc);


        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(startDateCal, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(endDateCal, gbc);


        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(buttonPanel, gbc);


        // build button panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(okButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        buttonPanel.add(cancelButton, gbc);

    }


    public static void main(String[] args) {
        FirstTimeDialog ftd = new FirstTimeDialog(null);
    }


    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }



    public boolean isCancelled() {
        return cancelled;
    }
}
