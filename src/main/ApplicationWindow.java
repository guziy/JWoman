package main;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;

import main.gui.PeriodsTableRenderer;
import main.model.ModelController;
import main.model.PeriodsTableModel;

public class ApplicationWindow {

	
	private MainViewController mvc;
	
	
	public ApplicationWindow() throws SQLException {
		mvc = new MainViewController();
		buildUI();
		
	}
	
	private void buildUI(){
		//1. Create the frame.
		JFrame frame = new JFrame("JWooman");
		frame.setSize(800, 600);
		
		
		Container mainPane = frame.getContentPane();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		
		mainPane.setLayout(gbl);
		
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
		
	
		//2. Optional: What happens when the frame closes?
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//When application is closed
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(frame, 
		            "Are you sure to close this window?", "Really Closing?", 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){

			        try {
						mvc.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        	
		        	System.exit(0);
		        }
		    }
		});
		
		
		
	
		//3. Create components and put them in the frame.
		
		//add the table containing the periods
		TableModel ptm = mvc.getPeriodsTableModel();
		JTable table = new JTable(ptm);
		table.setDefaultRenderer(Object.class, new PeriodsTableRenderer());
		//table.setPreferredSize(new Dimension(600, 400));
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 2;
		gbc.fill = GridBagConstraints.VERTICAL;
		mainPane.add(new JScrollPane(table), gbc);
		
		
		
		//Add the text field where the number of last periods to show is specified
		JTextField numPeriodsField = new JTextField(6);
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.insets = new Insets(5,10,0,0);
		mainPane.add(numPeriodsField, gbc);
		
		//Add the label for numPeriodsField
		JLabel numPeriodsLabel = new JLabel("Last periods to show: ");
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.insets = new Insets(10,10,0,0);
		mainPane.add(numPeriodsLabel, gbc);	
		
		
		
		//4. Size the frame.
		//frame.pack();
	
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
