package org.lsmr.selfcheckout.control.gui.states;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.lsmr.selfcheckout.control.gui.StateHandler;
import org.lsmr.selfcheckout.control.gui.statedata.StateData;

public class KeypadState implements GUIState, ActionListener {
	
	StateHandler<GUIState> stateController;
	JTextField input;
	String text = "";

	@Override
	public void init(StateHandler<GUIState> stateController, ReducedState reducedState) {
		this.stateController = stateController;
	}

	@Override
	public void onDataUpdate(StateData<?> data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JPanel getPanel() {
		final int keypadWidth = 500;
		Dimension size = new Dimension(keypadWidth, keypadWidth);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		input = new JTextField();
		input.setMaximumSize(new Dimension(keypadWidth, 50));
		input.setEditable(false);
		
		JPanel keyPadPanel = new JPanel();
		keyPadPanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100)); // for margins
		keyPadPanel.setMaximumSize(size);
		keyPadPanel.setPreferredSize(size);
		keyPadPanel.setSize(size);
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
		for (Component button : keyPadPanel.getComponents()) {
			((JButton) button).addActionListener(this);
		}
		
		mainPanel.add(input);
		mainPanel.add(keyPadPanel);
		return mainPanel;
	}

	@Override
	public ReducedState reduce() {
		return new KeypadReducedState(text);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		String buttonText = button.getText();
		
		if (Character.isDigit(buttonText.charAt(0))) {
			text += buttonText;
			
		} else if (buttonText.equals("Back")) {
			if (text.length() > 0) {
				text = text.substring(0, text.length()-1);
			}
			
		} else if (buttonText.equals("OK")) {
			stateController.setState(new BuyingState());
		}
		
		input.setText(text);
	}
}

class KeypadReducedState extends ReducedState {
	
	private String data;
	
	public KeypadReducedState(String barcode) {
		this.data = barcode;
	}

	@Override
	public Object getData() {
		return data;
	}
	
}
