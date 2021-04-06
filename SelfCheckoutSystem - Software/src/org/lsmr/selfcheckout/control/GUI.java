package org.lsmr.selfcheckout.control;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

public class GUI implements ActionListener{
	
	private JFrame frame;
	private ProductTableModel tableModel;
		
	public GUI() {

		
		frame = new JFrame();
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
		JButton checkoutButton = new JButton("Click Me");
		checkoutButton.setSize(buttonSize);
		checkoutButton.setPreferredSize(buttonSize);
		checkoutButton.setMinimumSize(buttonSize);
		checkoutButton.setMaximumSize(buttonSize);
		checkoutButton.addActionListener(this);
		
		// jpanel with gridlayout row: 1, col: 2, then add jpanel to buttonLayout
		buttonLayout.add(new JButton("1"));
		buttonLayout.add(newSpacing(1, 20));
		buttonLayout.add(new JButton("2"));
		buttonLayout.add(new JButton("3"));
		buttonLayout.add(checkoutButton);
		buttonLayout.setBackground(Color.RED);
		checkoutButtonPanel.add(buttonLayout, BorderLayout.SOUTH);
		
		//
		

		
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(checkoutButtonPanel, BorderLayout.LINE_END);
		
		frame.add(mainPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Co-op Self Checkout");
		frame.setResizable(false);
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

	public static void main(String[] args) {

		new GUI();
		
	}
	
	// https://mkyong.com/swing/java-swing-joptionpane-showinputdialog-example/

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		//tableModel.setProductScannedList(scannedProducts);
	}

}
