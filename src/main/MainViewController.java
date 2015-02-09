package main;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

import main.model.ModelController;
import main.model.Period;
import main.model.PeriodsTableModel;

public class MainViewController {

	private ModelController modelController;
	private PeriodsTableModel ptm;

	public MainViewController() throws SQLException {
		modelController = new ModelController();
		ptm = new PeriodsTableModel(modelController, Configuration.getConfiguration().getDefaultNumberOfPeriodsToShow());
	}

	public TableModel getPeriodsTableModel() {
		return ptm;
	}

	private List<Period> getNLastPeriods(int nPeriods) throws SQLException {
		return modelController.getNLastPeriods(nPeriods);
	}

	/**
	 * Closes db connection, called when application is closing
	 * 
	 * @throws SQLException
	 */
	public void close() throws SQLException {
		// modelController.createTestData();
		modelController.close();
	}

	public void onExitApp(JFrame frame) {
		if (JOptionPane.showConfirmDialog(frame,
				"Are you sure to close this window?", "Really Closing?",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

			try {
				modelController.save();
				close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.exit(0);
		}

	}

}
