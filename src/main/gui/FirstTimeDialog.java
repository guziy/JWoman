package main.gui;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;
import org.joda.time.LocalDate;

import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

/**
 * Created by san on 15-03-08.
 *
 */
public class FirstTimeDialog extends JDialog {


    private LocalDate startDate;
    private LocalDate endDate;
    private boolean cancelled = true;
    JDateChooser startDateCal, endDateCal;

    private JButton okButton, cancelButton;

    public FirstTimeDialog(JFrame mainWindow){
        super(mainWindow);
        setModalityType(ModalityType.APPLICATION_MODAL);

        setSize(new Dimension(800, 300));

        buildUI();
        pack();
        startDate = new LocalDate();
        endDate = new LocalDate();
        setLocationRelativeTo(mainWindow);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void buildUI() {


        //Exit the application when the dialog is closed
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (isCancelled()) {
                    System.exit(0);
                }
            }
        });

        setTitle("Please indicate the start and end dates of one of your periods.");

        Container mainPanel = getContentPane();
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();

        mainPanel.setLayout(gbl);

        JLabel startDateLabel = new JLabel("Start date");
        startDateCal = new JDateChooser();
        startDateCal.setToolTipText("Select start date of the period");

        JLabel endDateLabel = new JLabel("End date");
        endDateCal = new JDateChooser();
        endDateCal.setToolTipText("Select end date of the period");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());

        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        okButton.setSize(cancelButton.getSize());
        okButton.setPreferredSize(cancelButton.getPreferredSize());


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10,10,10,10);
        mainPanel.add(startDateLabel, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10,10,10,10);
        mainPanel.add(endDateLabel, gbc);


        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        mainPanel.add(startDateCal, gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        mainPanel.add(endDateCal, gbc);


        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(buttonPanel, gbc);


        // build button panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        //gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        buttonPanel.add(okButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 10, 0, 0);
        buttonPanel.add(cancelButton, gbc);


        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOk(e);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel(e);
            }
        });


    }

    private void onCancel(ActionEvent e) {
        dispose();
        cancelled = true;
        System.exit(0);
    }

    private void onOk(ActionEvent e) {
        startDate = new LocalDate(startDateCal.getDate());
        endDate = new LocalDate(endDateCal.getDate());
        cancelled = false;
        dispose();
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
