package org.lsmr.selfcheckout.control.gui.states;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.lsmr.selfcheckout.control.ProductTableModel;
import org.lsmr.selfcheckout.control.gui.StateHandler;
import org.lsmr.selfcheckout.control.gui.statedata.StateData;

public class AttendantState implements GUIState {

	private StateHandler<GUIState> stateController;

	private ProductTableModel tableModel;
	
	/**
	 * 
	 */
	@Override
	public void init(StateHandler<GUIState> stateController, ReducedState reducedState) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 */
	@Override
	public void onDataUpdate(StateData<?> data) {
		// TODO Auto-generated method stub

	}

	/**
	 * This sets up the widgets to be used in the attendant state
	 * 
	 */
	@Override
	public JPanel getPanel() {
		
		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(Color.BLUE);
		// TODO Auto-generated method stub
		return mainPanel;
	}
	

	private JComponent getProductPanel(Dimension tableSize, int rowHeight) {
		final int scrollBarWidth = 10;

		tableModel = new ProductTableModel();

		// table
		JTable scannedTable = new JTable(tableModel);

		scannedTable.setRowHeight(rowHeight);
		scannedTable.setPreferredScrollableViewportSize(scannedTable.getPreferredSize());
		scannedTable.setFillsViewportHeight(true);
		scannedTable.setPreferredSize(tableSize);
		scannedTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scannedTable.getTableHeader().setReorderingAllowed(false);
		scannedTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 20));
		// should dynamically change font in all columns we add but seeing if
		// code in for loop will work first
		/*Object dce = scannedTable.getDefaultEditor(Object.class);
		if(dce instanceof DefaultCellEditor) {
		    ((DefaultCellEditor) dce).getComponent().setFont(new Font("Arial", Font.PLAIN, 18));
		}*/

		// dynamic, so we only adjust the model and everything should work
		for (int i = 0; i < tableModel.getColumnCount(); i++) {
			// set column width
			float sizeWeight = tableModel.getColumnWidthWeight(i);
			scannedTable.getColumnModel().getColumn(i).setPreferredWidth((int) (tableSize.width * sizeWeight));
			scannedTable.setFont(new Font("Arial", Font.PLAIN, 18));
		}

		// wrap it in a scroll pane to see table headers
		JScrollPane container = new JScrollPane(scannedTable);
		container.setHorizontalScrollBar(null);
		container.getVerticalScrollBar().setPreferredSize(new Dimension(scrollBarWidth, 0)); // want width to be predictable so we know the exact size for the table
		container.setPreferredSize(new Dimension(tableSize.width + scrollBarWidth, tableSize.height));
		return container;
	}

	/**
	 * 
	 */
	@Override
	public ReducedState reduce() {
		// TODO Auto-generated method stub
		return null;
	}

}
