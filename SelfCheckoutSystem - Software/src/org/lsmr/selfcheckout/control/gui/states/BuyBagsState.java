package org.lsmr.selfcheckout.control.gui.states;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

public class BuyBagsState implements GUIState, ActionListener {

	private StateHandler<GUIState> stateController;
	private JTextField input;
	private String text = "";
	private JButton goBack;
	
	/**
	 * 
	 */
	@Override
	public void init(StateHandler<GUIState> stateController, ReducedState reducedState) {
		this.stateController = stateController;

	}

	/**
	 * 
	 */
	@Override
	public void onDataUpdate(StateData<?> data) {
		// TODO Auto-generated method stub

	}

	/**
	 * This sets up all the widgets on the buy bags screen
	 */
	@Override
	public JPanel getPanel() {
		final int keypadWidth = 500;
		Dimension size = new Dimension(keypadWidth, keypadWidth);

		// main panel components to be added to
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 50, 50));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		// title panel with logo
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout(200, 0));
		topPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 10, 50));
		JLabel topStatement = new JLabel("Compost Bag Charge");
		topStatement.setFont(new Font("Arial", Font.BOLD, 60));
		ImageIcon coopImg = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/cooplogo.png");
		Image coOpImg = coopImg.getImage() ;  
		Image newCoOpImg = coOpImg.getScaledInstance( 100, 100,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon coOpImgResized = new ImageIcon(newCoOpImg);
		JLabel coOpLogo = new JLabel(coOpImgResized);
		topPanel.add(topStatement, BorderLayout.CENTER);
		topPanel.add(coOpLogo, BorderLayout.EAST);
		
		// informing the user there is a $0.10 charge per bag, must enter a digit
		// even if no bags used
		JPanel wordPanel = new JPanel();
		wordPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 20, 50));
		JLabel words = new JLabel("Enter the number of bags you used for $0.10 each or 0 if no bags were used.");
		words.setFont(new Font("Arial", Font.BOLD, 30));
		wordPanel.add(words);

		// the text field that will display user input
		input = new JTextField();
		input.setMaximumSize(new Dimension(keypadWidth, 50));
		input.setEditable(false);
		input.setFont(new Font("Arial", Font.PLAIN, 30));
		input.setBackground(Color.WHITE);

		// center panel with keypad and back button
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 325, 0, 20));
		
		// the keypad that user will enter codes with
		JPanel keyPadPanel = new JPanel();
		keyPadPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50)); // for margins

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

		// amount due panel
		JLabel duePrintOut = new JLabel();
		duePrintOut.setText("Amount Due: $0.00");
		duePrintOut.setFont(new Font("Arial", Font.BOLD, 40));
		JPanel duePanel = new JPanel();
		duePanel.add(duePrintOut);

		// amount paid panel
		JLabel paidPrintOut = new JLabel();
		paidPrintOut.setText("Amount Paid: $0.00");
		paidPrintOut.setFont(new Font("Arial", Font.BOLD, 30));
		JPanel paidPanel = new JPanel();
		paidPanel.add(paidPrintOut);
		
		// back button
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

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.weightx = 0.9;
		centerPanel.add(keyPadPanel, constraints);
		constraints.gridx = 1;
		constraints.weightx = 0.1;
		constraints.anchor = GridBagConstraints.LAST_LINE_END;
		centerPanel.add(goBack, constraints);
		
		
		mainPanel.add(topPanel);
		mainPanel.add(wordPanel);
		mainPanel.add(input);
		mainPanel.add(centerPanel);
		mainPanel.add(duePanel);
		mainPanel.add(paidPanel);

		return mainPanel;
	}

	/**
	 * Reduces screen to only user input
	 */
	@Override
	public ReducedState reduce() {
		//int shouldBeReturn = Integer.parseInt(text);
		return new BuyBagsReducedState(text);
	}

	/*
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		JButton button = (JButton) arg0.getSource();
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
				if(text.length() > 0) {
					stateController.setState(new PurchasingState());
				}

			} 
		} 

		input.setText(text);
	}

}

/**
 * 
 * This allows to reduce the state of the buy bags state to only the input text
 *
 */
class BuyBagsReducedState extends ReducedState {

	private String data;

	public BuyBagsReducedState(String amount) {
		this.data = amount;
	}

	@Override
	public Object getData() {
		return data;
	}

}
