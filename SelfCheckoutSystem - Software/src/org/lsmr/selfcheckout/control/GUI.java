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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

public class GUI implements ActionListener{
	
	private JFrame frame;
		
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
		
		JComponent scannedTable = getProductPanel(new Dimension(500, 300)); 
		gridConstraints.gridx = 0;
		gridConstraints.gridy = 1;
		gridConstraints.weighty = 0.9;
		centerPanel.add(new JScrollPane(scannedTable), gridConstraints);
		// ---- right panel of layout ------
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());
		
		// Checkout button
		final Dimension buttonSize = new Dimension(120, 40); // for custom checkout button size
		JButton checkoutButton = new JButton("Click Me");
		checkoutButton.setSize(buttonSize);
		checkoutButton.setPreferredSize(buttonSize);
		checkoutButton.addActionListener(this);
		
		rightPanel.add(checkoutButton, BorderLayout.SOUTH);
		
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(rightPanel, BorderLayout.LINE_END);
		
		frame.add(mainPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Co-op Self Checkout");
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
	}
	
	private JComponent getProductPanel(Dimension tableSize) {
		final int scrollBarWidth = 10;
		
		
		Object[][] fakeData = {
				{"A", "100.00"},
				{"Test2", "200.11"}
		};
		
		// table
		JTable scannedTable = new JTable(fakeData, new Object[]{"Product", "Cost"});
		scannedTable.setRowHeight(50);
		scannedTable.setPreferredScrollableViewportSize(scannedTable.getPreferredSize());
		scannedTable.setFillsViewportHeight(true);
		scannedTable.setPreferredSize(tableSize);
		scannedTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumn col1 = scannedTable.getColumnModel().getColumn(0);
		TableColumn col2 = scannedTable.getColumnModel().getColumn(1);
		col1.setPreferredWidth((int) (tableSize.width * 0.8f));
		col2.setPreferredWidth((int) (tableSize.width * 0.2f));
		
		// wrap it in a scroll pane to see table headers
		JScrollPane container = new JScrollPane(scannedTable);
		container.getVerticalScrollBar().setPreferredSize(new Dimension(scrollBarWidth, 0)); // want width to be predictable so we know the exact size for the table
		container.setPreferredSize(new Dimension(tableSize.width + scrollBarWidth, tableSize.height));
		return container;
	}

	public static void main(String[] args) {

		new GUI();
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
	}

}
