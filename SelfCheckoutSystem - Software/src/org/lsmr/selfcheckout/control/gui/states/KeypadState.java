package org.lsmr.selfcheckout.control.gui.states;

import java.awt.BorderLayout;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.lsmr.selfcheckout.control.gui.StateHandler;
import org.lsmr.selfcheckout.control.gui.statedata.StateData;

public class KeypadState implements GUIState, ActionListener {

	private StateHandler<GUIState> stateController;
	private JTextField input;
	private String text = "";
	private JButton goBack;

	/*
	 * 
	 */
	@Override
	public void init(StateHandler<GUIState> stateController, ReducedState reducedState) {
		this.stateController = stateController;
	}

	/*
	 * 
	 */
	@Override
	public void onDataUpdate(StateData<?> data) {
		// TODO Auto-generated method stub

	}

	/**
	 *  This sets up all of the widgets to be used on the keypad state screen
	 */
	@Override
	public JPanel getPanel() {
		final int keypadWidth = 500;
		Dimension size = new Dimension(keypadWidth, keypadWidth);

		// main panel components to be added to
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 50, 50));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout(200, 0));
		topPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 10, 50));
		JLabel topStatement = new JLabel("Key In Item's Code");
		topStatement.setFont(new Font("Arial", Font.BOLD, 60));
		ImageIcon coopImg = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/cooplogo.png");
		Image coOpImg = coopImg.getImage() ;  
		Image newCoOpImg = coOpImg.getScaledInstance( 100, 100,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon coOpImgResized = new ImageIcon(newCoOpImg);
		JLabel coOpLogo = new JLabel(coOpImgResized);
		topPanel.add(topStatement, BorderLayout.CENTER);
		topPanel.add(coOpLogo, BorderLayout.EAST);

		// the text field that will display user input
		input = new JTextField();
		input.setMaximumSize(new Dimension(keypadWidth, 50));
		input.setEditable(false);
		input.setFont(new Font("Arial", Font.PLAIN, 30));
		input.setBackground(Color.WHITE);
		//topPanel.add(input, BorderLayout.CENTER);


		// the keypad that user will enter codes with
		JPanel keyPadPanel = new JPanel();
		keyPadPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100)); // for margins

		// set size of key pad
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

		// image of black arrow downloaded from below website
		// https://www.pikpng.com/downpngs/oxJooi_simpleicons-interface-undo-black-arrow-pointing-to-tanda/
		JPanel goBackPanel = new JPanel();
		ImageIcon arrow = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/black arrow.png");
		Image img = arrow.getImage() ;  
		Image newimg = img.getScaledInstance( 50, 50,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon arrowResized = new ImageIcon(newimg);

		goBack = new JButton();
		goBack.setLayout(new BorderLayout()); //so we can add an icon
		JLabel iconLabel = new JLabel(arrowResized);
		JLabel back = new JLabel("Go Back", SwingConstants.CENTER);

		back.setFont(new Font("Arial", Font.BOLD, 36));
		goBack.add(back, BorderLayout.CENTER);
		goBack.add(iconLabel, BorderLayout.WEST);
		goBack.addActionListener(this);

		// set size of go back button
		Dimension backSize = new Dimension(300, 75);
		goBack.setSize(backSize);
		goBack.setPreferredSize(backSize);
		goBack.setMinimumSize(backSize);
		goBack.setMaximumSize(backSize);

		goBackPanel.add(goBack);
		goBackPanel.setBorder(BorderFactory.createEmptyBorder(0, 100, 0, 100)); // for margins

		mainPanel.add(topPanel);
		mainPanel.add(input);
		mainPanel.add(keyPadPanel);
		mainPanel.add(goBackPanel);

		return mainPanel;
	}

	/**
	 * This returns only the necessary data needed to be known about the state
	 */
	@Override
	public ReducedState reduce() {
		return new KeypadReducedState(text);
	}


	/**
	 * This reacts to button presses
	 * @param e This is the button that is being pushed
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		// The go back button takes user back to buying screen
		if(button == goBack) {
			stateController.setState(new BuyingState());

		} else {
			String buttonText = button.getText();

			// Takes the text of the buttons to make a decision of what action to perform
			if (Character.isDigit(buttonText.charAt(0))) {
				text += buttonText;

			} else if (buttonText.equals("Delete")) {
				if (text.length() > 0) {
					text = text.substring(0, text.length()-1);
				}

			} else if (buttonText.equals("OK")) {
				// Add an error for if not a valid barcode .. where are we listening to the scale?
				//stateController.setState(new BuyingState());
				stateController.setState(new ScaleState());

			} 
		} 

		input.setText(text);
	}
}

/**
 * 
 * This allows to reduce the state of the key pad state to only the input text
 *
 */
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
