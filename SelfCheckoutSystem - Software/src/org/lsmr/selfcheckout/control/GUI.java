package org.lsmr.selfcheckout.control;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUI implements ActionListener{
	
	private int count = 0;
	private JLabel label;
	private JFrame frame;
	private JPanel panel;
	
	public GUI() {
		frame = new JFrame();
		frame.setPreferredSize(new Dimension(600, 1150));
		JButton button = new JButton("Click Me");
		//button.setPreferredSize(new Dimension(10, 30));
		button.addActionListener(this);
		label = new JLabel();
		
		panel = new JPanel();
		//panel.setBorder(BorderFactory.createEmptyBorder(600, 1150, 10, 30));
		panel.setLayout(new BorderLayout());
		panel.add(button, BorderLayout.LINE_END);
		panel.add(label, BorderLayout.CENTER);
		
		frame.add(panel, BorderLayout.CENTER);
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
