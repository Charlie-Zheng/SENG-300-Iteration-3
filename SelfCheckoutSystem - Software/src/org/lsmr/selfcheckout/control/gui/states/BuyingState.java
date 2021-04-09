package org.lsmr.selfcheckout.control.gui.states;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.lsmr.selfcheckout.control.ProductTableModel;
import org.lsmr.selfcheckout.control.ReceiptItem;
import org.lsmr.selfcheckout.control.gui.StateHandler;
import org.lsmr.selfcheckout.control.gui.statedata.ProductStateData;
import org.lsmr.selfcheckout.control.gui.statedata.StateData;
import org.lsmr.selfcheckout.control.gui.statedata.StringStateData;

/**
 * The state that displays the checkout screen
 */
public class BuyingState implements GUIState, ActionListener{
	
	private StateHandler<GUIState> stateController;
	
	private ProductTableModel tableModel;
	
	private JButton key;
	private JButton look;
	private JButton checkoutButton;
	
	@Override
	public void init(StateHandler<GUIState> stateController, ReducedState reducedState) {
		this.stateController = stateController;
		
		// previous state was keypad
		if (reducedState instanceof KeypadReducedState) {
			System.out.println("In");
			String barcode = (String) reducedState.getData();
			stateController.notifyListeners(new StringStateData(barcode));
		}
	}

	@Override
	public JPanel getPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // for margins
		mainPanel.setLayout(new BorderLayout());
		
		// ----- center panel of layout ----
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridConstraints = new GridBagConstraints(); // to align views
		
		// title
		JLabel title = new JLabel("Bag Item");
		title.setBackground(Color.RED);
		//title.setSize(500, 10);
		//title.setPreferredSize(new Dimension(500,10));
		
	//	keyPadPanel = new JPanel();
	//	keyPadPanel.setVisible(false);
		
		gridConstraints.gridx = 0;
		gridConstraints.gridy = 0;
		gridConstraints.weighty = 0.1;
		centerPanel.add(title, gridConstraints);
		
		JComponent scannedTable = getProductPanel(new Dimension(800, 640), 50); 
		gridConstraints.gridx = 0;
		gridConstraints.gridy = 1;
		gridConstraints.weighty = 0.9;
		centerPanel.add(new JScrollPane(scannedTable), gridConstraints);
		// ---- checkout button panel of layout ------
		
		JPanel checkoutButtonPanel = new JPanel();
		checkoutButtonPanel.setLayout(new BorderLayout());
		
		JPanel buttonLayout = new JPanel();
		buttonLayout.setLayout(new BoxLayout(buttonLayout, BoxLayout.Y_AXIS));
		
		// Checkout button
		final Dimension buttonSize = new Dimension(150, 40); // for custom checkout button size
		checkoutButton = new JButton("Finish & Pay");
		checkoutButton.setSize(buttonSize);
		checkoutButton.setPreferredSize(buttonSize);
		checkoutButton.setMinimumSize(buttonSize);
		checkoutButton.setMaximumSize(buttonSize);
		checkoutButton.addActionListener(this); 
		
		// jpanel with gridlayout row: 1, col: 2, then add jpanel to buttonLayout
		key = new JButton("Key In Code");
		key.addActionListener(this);
		buttonLayout.add(key);
		key.setBackground(Color.GREEN);
		buttonLayout.add(Box.createHorizontalStrut(10)); //makes a space between the panes
		buttonLayout.add(newSpacing(1, 20));
		look = new JButton("Look Up Item");
		buttonLayout.add(look);
		look.addActionListener(this);
		buttonLayout.add(newSpacing(1, 20));
		JLabel balancePrintOut = new JLabel();
		balancePrintOut.setText("Total: $0.00");
		//balancePrintOut.setText("Total: $" + checkout.getBalance()); //causes error right now
		//balancePrintOut.setHorizontalTextPosition(JLabel.RIGHT);
		//System.out.println(balancePrintOut.getHorizontalAlignment());
		//balancePrintOut.setHorizontalAlignment(JLabel.RIGHT);
		//System.out.println(balancePrintOut.getHorizontalAlignment());
		//balancePrintOut.setHorizontalAlignment(JLabel.CENTER);
		//balancePrintOut.setVerticalAlignment(JLabel.CENTER);
		buttonLayout.add(balancePrintOut, BorderLayout.CENTER);
		
		buttonLayout.add(newSpacing(1, 5));
		//buttonLayout.add(new JLabel("Total: $" + checkout.getBalance()));
		buttonLayout.add(checkoutButton);
		buttonLayout.setBackground(Color.RED);
		checkoutButtonPanel.add(buttonLayout, BorderLayout.SOUTH);
		
		//
		

		
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(checkoutButtonPanel, BorderLayout.LINE_END);
		
		return mainPanel;
	}

	@Override
	public ReducedState reduce() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onDataUpdate(StateData<?> data) {
		// if only java can support functional programming and Maybe types. would be a one-line code
		if (data instanceof ProductStateData) {
			tableModel.setProductScannedList(((ProductStateData) data).obtain());
		}
	}
	
	/**
	 * Creates a spacing placeholder to place certain elements at specific locations
	 * @param width the spacing width
	 * @param height the spacing height
	 * @return a new JComponent that acts as a spacer
	 */
	private JComponent newSpacing(int width, int height) {
		JPanel spacing = new JPanel();
		Dimension size = new Dimension(width, height);
		
		// setting all these makes it work
		spacing.setSize(size);
		spacing.setPreferredSize(size);
		spacing.setMinimumSize(size);
		spacing.setMaximumSize(size);
		
		return spacing;
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
		
		// dynamic, so we only adjust the model and everything should work
		for (int i = 0; i < tableModel.getColumnCount(); i++) {
			// set column width
			float sizeWeight = tableModel.getColumnWidthWeight(i);
			scannedTable.getColumnModel().getColumn(i).setPreferredWidth((int) (tableSize.width * sizeWeight));
		}
		
		// wrap it in a scroll pane to see table headers
		JScrollPane container = new JScrollPane(scannedTable);
		container.setHorizontalScrollBar(null);
		container.getVerticalScrollBar().setPreferredSize(new Dimension(scrollBarWidth, 0)); // want width to be predictable so we know the exact size for the table
		container.setPreferredSize(new Dimension(tableSize.width + scrollBarWidth, tableSize.height));
		return container;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent event) {
		JComponent view = (JComponent) event.getSource();
		if (view == key) {
			stateController.setState(new KeypadState());
		} else if (view == look) {
			stateController.setState(new LookupState());
		} else if(view == checkoutButton) {
			stateController.setState(new PurchasingState());
		}
	}
}
