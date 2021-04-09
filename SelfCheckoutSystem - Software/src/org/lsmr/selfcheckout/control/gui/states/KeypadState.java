package org.lsmr.selfcheckout.control.gui.states;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
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
		mainPanel.setBorder(BorderFactory.createEmptyBorder(100, 16, 50, 16));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		input = new JTextField();
		input.setMaximumSize(new Dimension(keypadWidth, 50));
		input.setEditable(false);
		input.setFont(new Font("Arial", Font.PLAIN, 30));
		input.setBackground(Color.WHITE);
		//JPanel inputPanel = new JPanel();
		//inputPanel.add(input);
		//inputPanel.setBorder(BorderFactory.createEmptyBorder(100, 10, 10, 10));
		
		JPanel keyPadPanel = new JPanel();
		keyPadPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100)); // for margins
		keyPadPanel.setMaximumSize(size);
		keyPadPanel.setPreferredSize(size);
		keyPadPanel.setSize(size);
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
		JButton delete = new JButton("Delete");
		keyPadPanel.add(delete);
		keyPadPanel.add(new JButton("0"));
		keyPadPanel.add(new JButton("OK"));
		for (Component button : keyPadPanel.getComponents()) {
			((JButton) button).addActionListener(this);
			if(button != delete) {
			((JButton) button).setFont(new Font("Arial", Font.PLAIN, 40));
			} else {
				((JButton) button).setFont(new Font("Arial", Font.BOLD, 20));
			}
		}
		
		//https://www.pikpng.com/downpngs/oxJooi_simpleicons-interface-undo-black-arrow-pointing-to-tanda/
		JPanel goBackPanel = new JPanel();
		ImageIcon arrow = new ImageIcon("src/org.lsmr.selfcheckout.gui.icons/black arrow.png");
		Image img = arrow.getImage() ;  
		Image newimg = img.getScaledInstance( 30, 30,  java.awt.Image.SCALE_SMOOTH ) ;  
		ImageIcon arrowResized = new ImageIcon( newimg );

		JButton goBack = new JButton("Go Back",arrowResized);
		//goBack.setVerticalTextPosition(SwingConstants.TOP);
		goBack.addActionListener(this);
		Dimension backSize = new Dimension(300, 75);
		goBack.setSize(backSize);
		goBack.setPreferredSize(backSize);
		goBack.setMinimumSize(backSize);
		goBack.setMaximumSize(backSize);
		goBack.setFont(new Font("Arial", Font.PLAIN, 30));
		goBack.setHorizontalTextPosition(JButton.CENTER);
		goBack.setVerticalTextPosition(JButton.CENTER);
		goBackPanel.add(goBack);
		goBackPanel.setBorder(BorderFactory.createEmptyBorder(0, 100, 0, 100)); // for margins
		
		mainPanel.add(input);
		mainPanel.add(keyPadPanel);
		mainPanel.add(goBackPanel);
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
			
		} else if (buttonText.equals("Delete")) {
			if (text.length() > 0) {
				text = text.substring(0, text.length()-1);
			}
			
		} else if (buttonText.equals("OK")) {
			stateController.setState(new BuyingState());
		
		} else if(buttonText.equals("Go Back")) {
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
