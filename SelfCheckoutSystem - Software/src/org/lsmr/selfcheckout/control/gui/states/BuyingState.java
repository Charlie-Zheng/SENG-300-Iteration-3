package org.lsmr.selfcheckout.control.gui.states;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

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



		if (reducedState instanceof BaseReducedState) {
			System.out.println("Counter received is: " + reducedState.getData());
		}
	}

	@Override
	public JPanel getPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20)); // for margins
		mainPanel.setLayout(new BorderLayout());


		// set up statement at top of screen
		// image for coop logo downloaded from website below
		// https://www.google.com/search?q=coop+png&tbm=isch&ved=2ahUKEwjC9bTd-PHvAhU8AzQIHSiLAkkQ2-cCegQIABAA&oq=coop+png&gs_lcp=CgNpbWcQAzIECAAQQzICCAAyBggAEAcQHjIGCAAQBxAeMgYIABAHEB4yBggAEAcQHjIGCAAQBxAeMgYIABAHEB4yBggAEAcQHjIGCAAQBxAeUM-sA1jPrANg67IDaABwAHgAgAFDiAFDkgEBMZgBAKABAaoBC2d3cy13aXotaW1nwAEB&sclient=img&ei=MLFwYMKdCryG0PEPqJaKyAQ&bih=619&biw=1280#imgrc=EmUG01nblMHYrM
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout(275, 0));
		JLabel topStatement = new JLabel("Scan & Bag Items");
		ImageIcon coopImg = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/cooplogo.png");
		Image coOpImg = coopImg.getImage() ;  
		Image newCoOpImg = coOpImg.getScaledInstance( 100, 100,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon coOpImgResized = new ImageIcon(newCoOpImg);
		JLabel coOpLogo = new JLabel(coOpImgResized);
		topStatement.setFont(new Font("Arial", Font.BOLD, 60));
		topPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 0, 30));
		topPanel.add(topStatement, BorderLayout.CENTER);
		topPanel.add(coOpLogo, BorderLayout.EAST);



		// ----- center panel of layout ----
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridConstraints = new GridBagConstraints(); // to align views

		JComponent scannedTable = getProductPanel(new Dimension(650, 500), 50); 
		gridConstraints.gridx = 0;
		gridConstraints.gridy = 1;
		gridConstraints.weighty = 0.9;
		centerPanel.add(new JScrollPane(scannedTable), gridConstraints);
		// ---- checkout button panel of layout ------

		// dollar sign image downloaded from below website
		// https://www.cleanpng.com/png-dollar-sign-united-states-dollar-symbol-us-dollars-1327518/download-png.html		
		JPanel checkoutButtonPanel = new JPanel();
		checkoutButtonPanel.setLayout(new BorderLayout());
		checkoutButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 35, 20));

		JPanel buttonLayout = new JPanel();
		buttonLayout.setLayout(new BoxLayout(buttonLayout, BoxLayout.Y_AXIS));

		// Checkout button
		JPanel checkoutPanel = new JPanel();
		ImageIcon check = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/dollar sign.png");
		Image checkImg = check.getImage() ;  
		Image newCheckImg = checkImg.getScaledInstance( 75, 75,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon checkImgResized = new ImageIcon(newCheckImg);
		
		checkoutButton = new JButton();
		checkoutButton.setLayout(new BorderLayout());
		JLabel checkIcon = new JLabel(checkImgResized);
		JLabel checkLabel = new JLabel("Finish & Pay", SwingConstants.CENTER);
		checkLabel.setFont(new Font("Arial", Font.BOLD, 40));
		checkoutButton.add(checkLabel, BorderLayout.CENTER);
		checkoutButton.add(checkIcon, BorderLayout.WEST);
		checkoutButton.addActionListener(this);
		final Dimension buttonSize = new Dimension(400, 100); // for custom checkout button size
		checkoutButton.setSize(buttonSize);
		checkoutButton.setPreferredSize(buttonSize);
		checkoutButton.setMinimumSize(buttonSize);
		checkoutButton.setMaximumSize(buttonSize);
		
		checkoutPanel.add(checkoutButton);

		// jpanel with gridlayout row: 1, col: 2, then add jpanel to buttonLayout
		// keypad image downloaded from website below
		// https://pngio.com/PNG/a79545-numeric-keypad-png.html
		JPanel keyPanel = new JPanel();
		ImageIcon keyPad = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/number pad.png");
		Image keyImg = keyPad.getImage() ;  
		Image newKeyImg = keyImg.getScaledInstance( 50, 50,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon keyImgResized = new ImageIcon(newKeyImg);
		
		key = new JButton();
		key.setLayout(new BorderLayout());
		JLabel keyIcon = new JLabel(keyImgResized);
		JLabel keyLabel = new JLabel("Key In Code", SwingConstants.CENTER);
		keyLabel.setFont(new Font("Arial", Font.BOLD, 30));
		key.add(keyLabel, BorderLayout.CENTER);
		key.add(keyIcon, BorderLayout.WEST);
		key.addActionListener(this);
		Dimension buttonSize1 = new Dimension(300, 75);
		key.setSize(buttonSize1);
		key.setPreferredSize(buttonSize1);
		key.setMinimumSize(buttonSize1);
		key.setMaximumSize(buttonSize1);
		keyPanel.add(key);
		buttonLayout.add(keyPanel);
		buttonLayout.add(newSpacing(1, 20));
		
		// magnifying glass image downloaded from below website
		// https://www.cleanpng.com/png-magnifying-glass-light-clip-art-loupe-650531/download-png.html		
		JPanel lookPanel = new JPanel();
		ImageIcon lookUp = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/magnifying glass.png");
		Image lookImg = lookUp.getImage() ;  
		Image newLookImg = lookImg.getScaledInstance( 50, 50,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon lookImgResized = new ImageIcon(newLookImg);
		
		look = new JButton();
		look.setLayout(new BorderLayout());
		JLabel lookIcon = new JLabel(lookImgResized);
		JLabel lookLabel = new JLabel("Look Up Item", SwingConstants.CENTER);
		lookLabel.setFont(new Font("Arial", Font.BOLD, 30));
		look.add(lookLabel, BorderLayout.CENTER);
		look.add(lookIcon, BorderLayout.WEST);
		look.addActionListener(this);
		
		look.setSize(buttonSize1);
		look.setPreferredSize(buttonSize1);
		look.setMinimumSize(buttonSize1);
		look.setMaximumSize(buttonSize1);
		lookPanel.add(look);
		buttonLayout.add(lookPanel);
		
		buttonLayout.add(newSpacing(1, 70));
		JLabel balancePrintOut = new JLabel();
		balancePrintOut.setText("Total: $0.00");
		balancePrintOut.setFont(new Font("Arial", Font.BOLD, 40));
		JPanel labelPanel = new JPanel();
		labelPanel.add(balancePrintOut);
		
		//balancePrintOut.setText("Total: $" + checkout.getBalance()); //causes error right now
		buttonLayout.add(labelPanel);

		buttonLayout.add(newSpacing(1, 10));
		buttonLayout.add(checkoutPanel);
		checkoutButtonPanel.add(buttonLayout, BorderLayout.SOUTH);

		mainPanel.add(topPanel, BorderLayout.PAGE_START);
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


	@Override
	public void actionPerformed(ActionEvent event) {
		JComponent view = (JComponent) event.getSource();
		if (view == key) {
			stateController.setState(new KeypadState());
			//stateController.setState(new RedState());
		} else if (view == look) {
			stateController.setState(new LookupState());
		} else if(view == checkoutButton) {
			stateController.setState(new PurchasingState());
		}
	}
}
