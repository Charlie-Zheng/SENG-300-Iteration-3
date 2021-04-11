package org.lsmr.selfcheckout.control.gui.states;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;

import org.lsmr.selfcheckout.control.ProductTableModel;
import org.lsmr.selfcheckout.control.gui.StateHandler;
import org.lsmr.selfcheckout.control.gui.statedata.StateData;

public class AttendantState implements GUIState, ActionListener {

	private StateHandler<GUIState> stateController;

	private ProductTableModel tableModel;
	JButton tableButton;
	JButton logOut;
	
	
	/**
	 * 
	 */
	@Override
	public void init(StateHandler<GUIState> stateController, ReducedState reducedState) {
		this.stateController = stateController;

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
		
		int numOfStations = 6;
		
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 85, 0, 85));
		mainPanel.setLayout(new BorderLayout());

		// informing the user there is a $0.10 charge per bag, must enter a digit
		// even if no bags used
		JPanel wordPanel = new JPanel();
		wordPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 20, 50));
		wordPanel.setBorder(new LineBorder(Color.RED.darker()));
		wordPanel.setBackground(Color.RED.darker());
		JLabel words = new JLabel("Select a checkout");
		words.setFont(new Font("Arial", Font.BOLD, 50));
		words.setForeground(Color.WHITE);
		words.setBackground(Color.RED.darker());
		words.setBorder(new LineBorder(Color.RED.darker()));
		wordPanel.add(words);
		
		Dimension tableSize = new Dimension(494, 150);

		JPanel stationsPanel = new JPanel();
		stationsPanel.setLayout(new GridLayout(3, 2, 86, 30));
		stationsPanel.setBorder(new LineBorder(Color.RED.darker()));
		stationsPanel.setBackground(Color.RED.darker());

	
		
		for(int i = 0; i < numOfStations; i++) {
			JPanel checkoutWithNumPanel = new JPanel();
			checkoutWithNumPanel.setLayout(new BoxLayout(checkoutWithNumPanel, BoxLayout.Y_AXIS));
			checkoutWithNumPanel.setBorder(new LineBorder(Color.RED.darker()));
			checkoutWithNumPanel.setBackground(Color.RED.darker());
			
			JPanel stationNumPanel = new JPanel();
			//stationNumPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 20, 50));
			//stationNumPanel.setBorder(new LineBorder(Color.RED.darker()));
			//stationNumPanel.setBackground(Color.RED.darker());
			JLabel stationNum = new JLabel("Checkout #" + (i + 1));
			stationNum.setFont(new Font("Arial", Font.BOLD, 20));
			stationNum.setForeground(Color.WHITE);
			stationNum.setBackground(Color.RED.darker());
			stationNum.setBorder(new LineBorder(Color.RED.darker()));
			stationNum.add(words);
			//checkoutWithNumPanel.add(stationNum);
			
			tableButton = new JButton();
			tableButton.setLayout(new BorderLayout());
			JLabel buttonLabel = new JLabel();
			buttonLabel.setText("Checkout #" + (i + 1));
			buttonLabel.setForeground(Color.white);
			buttonLabel.setFont(new Font("Arial", Font.BOLD, 20));
			
			JComponent scannedTable = getProductPanel(tableSize, 50);
			tableButton.add(new JScrollPane(scannedTable), BorderLayout.SOUTH);
			tableButton.setBackground(Color.RED.darker());
			tableButton.setBorder(new LineBorder(Color.RED.darker()));
			tableButton.add(buttonLabel, BorderLayout.NORTH);
			tableButton.addActionListener(this);
			//checkoutWithNumPanel.add(tableButton);
			
			stationsPanel.add(tableButton);
		}
		
		
		JPanel logOutPanel = new JPanel();
		logOutPanel.setLayout(new BorderLayout());
		logOutPanel.setBorder(new LineBorder(Color.RED.darker()));
		logOutPanel.setBackground(Color.RED.darker());
		logOutPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
		
		logOut = new JButton("Log Out");
		logOut.setFont(new Font("Arial", Font.BOLD, 30));
		Dimension logOutSize = new Dimension(250, 50);
		logOut.setSize(logOutSize);
		logOut.setPreferredSize(logOutSize);
		logOut.setMinimumSize(logOutSize);
		logOut.setMaximumSize(logOutSize);
		logOut.addActionListener(this);
		
		logOutPanel.add(logOut, BorderLayout.EAST);
		
		
		
		wordPanel.setBackground(Color.BLUE);
		
		mainPanel.setBackground(Color.RED.darker());
	//	mainPanel.add(wordPanel, BorderLayout.PAGE_START);
		mainPanel.add(stationsPanel, BorderLayout.CENTER);
		mainPanel.add(logOutPanel, BorderLayout.PAGE_END);
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
		scannedTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
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
			//scannedTable.setFont(new Font("Arial", Font.PLAIN, 12));
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

	@Override
	public void actionPerformed(ActionEvent arg0) {
		JButton button = (JButton) arg0.getSource();
		if(button == logOut) {
			stateController.setState(new AttendantLogInState());
		} else if(button == tableButton) {
			
			String buttonText = button.getText();
			
			/*if(buttonText.equals("Checkout #1")) {
				stateController.setState(new AttendantAccessState()); 
			}*/
			
		
			
			//add bunch of if statements for specific checkout numbers
			stateController.setState(new AttendantAccessState()); //** not working **
			/*if(button.getComponent(0).equals(tableButton)) {

				stateController.setState(new AttendantAccessState());
			}*/
		}
		
	}

}
