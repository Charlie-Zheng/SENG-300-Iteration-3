package org.lsmr.selfcheckout.control;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUI implements ActionListener{
	
	private JFrame frame;
		
	public GUI() {
		frame = new JFrame();
		//frame.setPreferredSize(new Dimension(600, 1150));
		
		// Initialize layout containers

		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // for margins
		mainPanel.setLayout(new BorderLayout());

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
		
		mainPanel.add(rightPanel, BorderLayout.LINE_END);
		
		frame.add(mainPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Co-op Self Checkout");
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {

		new GUI();
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
	}

}
