package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.text.PlainDocument;

import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import main.gui.FirstTimeDialog;
import main.gui.PeriodsTableEditor;
import main.gui.PeriodsTableRenderer;
import main.gui.PosIntInputVerifier;
import main.model.ModelController;
import main.model.PeriodsTableModel;

public class ApplicationWindow {

    private JFrame frame;
	private MainViewController mvc;
	
	//TODO: improve the caching later
	private int totalNumOfPeriodsCached = -1;
	
	public ApplicationWindow() throws SQLException {
		mvc = new MainViewController();
		buildUI();

        //If there is no periods yet
        if (mvc.getTotalNumberOfPeriods() == 0) {
            FirstTimeDialog ftd = new FirstTimeDialog(frame);
        }
    }
	
	/**
	 * 
	 * @return Customized main frame
	 */
	private JFrame getMainFrame(){
		JFrame frame = new JFrame("JWoman");

		frame.setSize(new Dimension(800, 600));
        Toolkit kit = Toolkit.getDefaultToolkit();
        Image image = kit.getImage(ClassLoader.getSystemResource("icons/heart-64px.png"));
        frame.setIconImage(image);
        return frame;
	}


    private void setLookAndFeel(){
        try {
            // Set L&F
            UIManager.setLookAndFeel(
                    new Plastic3DLookAndFeel());

            for (UIManager.LookAndFeelInfo s: UIManager.getInstalledLookAndFeels()){
                System.out.println(s.getClassName());
            }

        }
        catch (UnsupportedLookAndFeelException e) {
            // handle exception
        }
    }
	
	private void buildUI(){

        setLookAndFeel();


		//1. Create the frame.
		frame = getMainFrame();
		mvc.setMainFrame(frame);
		Container mainPane = frame.getContentPane();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		
		mainPane.setLayout(gbl);
		
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
		
	
		//2. Optional: What happens when the frame closes?
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		//When application is closed
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	mvc.onExitApp(frame);
		    }
		});
		
		
		
	
		//3. Create components and put them in the frame.
		
		//add the table containing the periods
		final PeriodsTableModel ptm = mvc.getPeriodsTableModel();
		JTable table = new JTable(ptm);
        table.setRowHeight(20);

		table.setDefaultRenderer(Object.class, new PeriodsTableRenderer(ptm));
		//table.setPreferredSize(new Dimension(600, 400));
        table.setDefaultEditor(LocalDate.class, new PeriodsTableEditor());



		GridBagLayout gblForFilterPanel = new GridBagLayout();
		JPanel filterPanel = new JPanel();
		
		
		filterPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), 
				"Filter periods"));
		filterPanel.setLayout(gblForFilterPanel);

		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.7;
		gbc.weighty = 1;
		gbc.insets = new Insets(10, 10, 10, 10);
		mainPane.add(new JScrollPane(table), gbc);
		
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.insets = new Insets(10,0,10,10);
		gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
		gbc.weightx = 0.3;
		
		mainPane.add(filterPanel, gbc);

		
		
		//Add the text field where the number of last periods to show is specified
		final JTextField numPeriodsField = new JTextField();
		numPeriodsField.setColumns(8);
		
		//((PlainDocument) numPeriodsField.getDocument()).setDocumentFilter(new PositiveIntDocumentFilter());
		//numPeriodsField.getDocument().addDocumentListener((DocumentListener) ptm);
		numPeriodsField.addActionListener(ptm);
		numPeriodsField.setText(Integer.toString(ptm.getRowCount()));
		
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.weighty = 0;
		gbc.insets = new Insets(10,10,0,0);
		gbc.fill = GridBagConstraints.VERTICAL;
		gbc.anchor = GridBagConstraints.NORTH;
		filterPanel.add(numPeriodsField, gbc);
		
		//Add the label for numPeriodsField
		JLabel numPeriodsLabel = new JLabel("Last periods to show: ");
		gbc.gridx = 0;
		gbc.insets = new Insets(10,10,0,0);
		filterPanel.add(numPeriodsLabel, gbc);
		
		
		final JButton filterButton = new JButton(new ImageIcon(this.getClass().getResource("/icons/filter-24px.png")));
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                e.setSource(numPeriodsField);
                ptm.actionPerformed(e);
            }
        });

        filterButton.setToolTipText("Apply the filter");
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.insets = new Insets(10,0,0,0);
		filterPanel.add(filterButton, gbc);
		
		
		final JCheckBox showAllCheckBox = new JCheckBox("Show all periods");
		showAllCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(10,10,10,0);
		filterPanel.add(showAllCheckBox, gbc);
		
		//Show all checkbox actions
		showAllCheckBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				numPeriodsField.setEnabled(!showAllCheckBox.isSelected());
				filterButton.setEnabled(!showAllCheckBox.isSelected());
				
				if (showAllCheckBox.isSelected()){
					try {

						if (totalNumOfPeriodsCached < 0) { //Go to the database only once
							totalNumOfPeriodsCached = mvc.getTotalNumberOfPeriods();
						}
						ptm.onChangeOfNumOfPeriodsToShow(totalNumOfPeriodsCached);
						numPeriodsField.setText(Integer.toString(totalNumOfPeriodsCached));
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}				
				}
			}
			
		});
		
		
		
		//4. Size the frame.
		frame.pack();
	
		//5. Show it.
		frame.setVisible(true);


    }
	
	
	public static void main(String[] args) {
		// The entry point of the application
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				try {
					ApplicationWindow aw = new ApplicationWindow();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}});
	}



}
