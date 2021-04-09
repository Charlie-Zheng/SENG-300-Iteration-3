package org.lsmr.selfcheckout.control;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumn;

import org.lsmr.selfcheckout.devices.SelfCheckoutStation;

public class GUI implements ActionListener{
	
	private Checkout checkout;
	
	private JFrame frame;
	private ProductTableModel tableModel;
	//private JPanel keyPadPanel;
		
	public GUI() {
		
		//checkout = new Checkout(new SelfCheckoutStation(currency, banknoteDenominations, coinDenominations, scaleMaximumWeight, scaleSensitivity))

		
		frame = new JFrame();
		//frame = new MainFrame(this); // look into what the difference is
		//frame.setPreferredSize(new Dimension(600, 1150));
		
		// Initialize layout containers

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
		
		JComponent scannedTable = getProductPanel(new Dimension(500, 300), 50); 
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
		JButton checkoutButton = new JButton("Finish & Pay");
		checkoutButton.setSize(buttonSize);
		checkoutButton.setPreferredSize(buttonSize);
		checkoutButton.setMinimumSize(buttonSize);
		checkoutButton.setMaximumSize(buttonSize);
		checkoutButton.addActionListener(this); 
		
		// jpanel with gridlayout row: 1, col: 2, then add jpanel to buttonLayout
		JButton key = new JButton("Key In Code");
		//key.addActionListener(this);
		buttonLayout.add(key);
		key.setBackground(Color.GREEN);
		buttonLayout.add(Box.createHorizontalStrut(10)); //makes a space between the panes
		buttonLayout.add(newSpacing(1, 20));
		JButton look = new JButton("Look Up Item");
		buttonLayout.add(look);
		//look.addActionListener(this);
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
		
		frame.add(mainPanel);
		//frame.add(keyPadPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Co-op Self Checkout");
		//frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		// to make the frame fit the size of the screen being viewed on ** not working **
		//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		//int height = screenSize.height;
		//int width = screenSize.width;
		//frame.setSize(width, height);
		frame.pack();
		frame.setVisible(true);
		
		
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
	
	// keypad:  use GridLayout
	
	

	public static void main(String[] args) {

		new GUI();
		
	}
	
	// https://mkyong.com/swing/java-swing-joptionpane-showinputdialog-example/

	
	private void jButtonActionPerformed(java.awt.event.ActionEvent evt) {
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		//tableModel.setProductScannedList(scannedProducts);
	// ***************************** Key In Code **********************************//	
		JFrame keyFrame = new JFrame();
		
		JPanel keyPadPanel = new JPanel();
		keyPadPanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100)); // for margins
		//keyPadPanel.setLayout(new BorderLayout());
		keyPadPanel.setLayout(new GridLayout(4,3,10,10));
		keyPadPanel.add(new JButton("1"));
		keyPadPanel.add(new JButton("2"));
		keyPadPanel.add(new JButton("3"));
		keyPadPanel.add(new JButton("4"));
		keyPadPanel.add(new JButton("5"));
		keyPadPanel.add(new JButton("6"));
		keyPadPanel.add(new JButton("7"));
		keyPadPanel.add(new JButton("8"));
		keyPadPanel.add(new JButton("9"));
		keyPadPanel.add(new JButton("Back"));
		keyPadPanel.add(new JButton("0"));
		keyPadPanel.add(new JButton("OK"));
		

		keyFrame.add(keyPadPanel);
		//frame.add(keyPadPanel);
		keyFrame.setTitle("Co-op Self Checkout - Key In Code");
		//frame.setLocationRelativeTo(null);
		keyFrame.setResizable(true);
		// to make the frame fit the size of the screen being viewed on ** not working **
		//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		//int height = screenSize.height;
		//int width = screenSize.width;
		//frame.setSize(width, height);
		keyFrame.pack();
	//	frame.setVisible(fale);
		keyFrame.setVisible(false);
// ***************************** Look Up Item ********************************//		
		
		JFrame lookUpFrame = new JFrame();
		
		JTextArea codeInput = new JTextArea();
		JPanel inputPanel = new JPanel();
	/*	  editTextArea.setForeground(Color.WHITE);

	        //SET CONTENT PANE
	        Container c = getContentPane();

	        //ADD COMPONENTS TO CONTENT PANE        
	        c.add(uneditTextArea, BorderLayout.CENTER);
	        c.add(editTextArea, BorderLayout.SOUTH);
	        c.add(inputButton, BorderLayout.WEST);
		*/
		JPanel lookUpPanel = new JPanel();
		lookUpPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 100, 100)); // for margins
		//keyPadPanel.setLayout(new BorderLayout());
		lookUpPanel.setLayout(new GridLayout(3,10,10,10));
		lookUpPanel.add(new JButton("Q"));
		lookUpPanel.add(new JButton("W"));
		lookUpPanel.add(new JButton("E"));
		lookUpPanel.add(new JButton("R"));
		lookUpPanel.add(new JButton("T"));
		lookUpPanel.add(new JButton("Y"));
		lookUpPanel.add(new JButton("U"));
		lookUpPanel.add(new JButton("I"));
		lookUpPanel.add(new JButton("O"));
		lookUpPanel.add(new JButton("P"));
		lookUpPanel.add(new JButton("A"));
		lookUpPanel.add(new JButton("S"));
		lookUpPanel.add(new JButton("D"));
		lookUpPanel.add(new JButton("F"));
		lookUpPanel.add(new JButton("G"));
		lookUpPanel.add(new JButton("H"));
		lookUpPanel.add(new JButton("J"));
		lookUpPanel.add(new JButton("K"));
		lookUpPanel.add(new JButton("L"));
		lookUpPanel.add(new JButton("OK"));
		lookUpPanel.add(new JButton("Z"));
		lookUpPanel.add(new JButton("X"));
		lookUpPanel.add(new JButton("C"));
		lookUpPanel.add(new JButton("V"));
		lookUpPanel.add(new JButton("B"));
		lookUpPanel.add(new JButton("N"));
		lookUpPanel.add(new JButton("M"));
		lookUpPanel.add(new JButton("Back"));
		
		
		
		inputPanel.add(codeInput);
		codeInput.setBackground(Color.WHITE);
		codeInput.setForeground(Color.WHITE);
		inputPanel.setBackground(Color.BLUE);
		inputPanel.setBorder(BorderFactory.createEmptyBorder(100, 10, 10, 10));
		
		
		lookUpFrame.add(inputPanel, BorderLayout.PAGE_START);
		lookUpFrame.add(lookUpPanel);
		//frame.add(keyPadPanel);
		lookUpFrame.setTitle("Co-op Self Checkout - Look Up Item");
		//frame.setLocationRelativeTo(null);
		lookUpFrame.setResizable(true);
		// to make the frame fit the size of the screen being viewed on ** not working **
		//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		//int height = screenSize.height;
		//int width = screenSize.width;
		//frame.setSize(width, height);
		lookUpFrame.pack();
	//	frame.setVisible(false);
		lookUpFrame.setVisible(false);
		
// ******************************* Finish & Pay ********************************//
		
		JFrame payFrame = new JFrame();
		
		JPanel payPanel = new JPanel();
		payPanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
		lookUpPanel.setLayout(new GridLayout(0, 3, 50, 50));
		
		final Dimension buttonSize1 = new Dimension(150, 150); // for custom checkout button size

		JButton cash = new JButton("Cash");
		cash.setSize(buttonSize1);
		payPanel.add(cash);
		JButton debit = new JButton("Debit");
		debit.setSize(buttonSize1);
		payPanel.add(debit);
		JButton credit = new JButton("Credit");
		credit.setSize(buttonSize1);
		payPanel.add(credit);
		
		payFrame.add(payPanel);
		//frame.add(keyPadPanel);
		payFrame.setTitle("Co-op Self Checkout - Look Up Item");
		//frame.setLocationRelativeTo(null);
		payFrame.setResizable(true);
		// to make the frame fit the size of the screen being viewed on ** not working **
		//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		//int height = screenSize.height;
		//int width = screenSize.width;
		//frame.setSize(width, height);
		payFrame.pack();
	//	frame.setVisible(false);
		payFrame.setVisible(true);
		
		
	}

}
