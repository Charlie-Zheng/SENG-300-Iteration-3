package org.lsmr.selfcheckout.control;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class GUI implements ActionListener{
	
	private int count = 0;
	private JLabel label;
	private JFrame frame;
	private JPanel mainPanel;
	
	public GUI() {
		frame = new JFrame();
		//frame.setPreferredSize(new Dimension(600, 1150));
		
		// Initialize layout containers

		mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		mainPanel.setLayout(new BorderLayout());

		// right panel of layout
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());
		
		// Checkout button
		
		final Dimension buttonSize = new Dimension(120, 40); // for custom checkout button size
		JButton checkoutButton = new JButton("Click Me");
		checkoutButton.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		checkoutButton.setSize(buttonSize);
		checkoutButton.setPreferredSize(buttonSize);
		checkoutButton.addActionListener(this);
		
		rightPanel.add(checkoutButton, BorderLayout.SOUTH);
		
		
		mainPanel.add(rightPanel, BorderLayout.LINE_END);
		
		frame.add(mainPanel, BorderLayout.CENTER);
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
		// TODO Auto-generated method stub
		count++;
		label.setText("Number of clicks: " + count);
	}

}
