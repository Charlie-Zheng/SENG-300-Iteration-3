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

import org.lsmr.selfcheckout.control.gui.GUIUtils;
import org.lsmr.selfcheckout.control.gui.StateHandler;
import org.lsmr.selfcheckout.control.gui.GUIUtils.Property;
import org.lsmr.selfcheckout.control.gui.statedata.BooleanStateData;
import org.lsmr.selfcheckout.control.gui.statedata.MemberStateData;
import org.lsmr.selfcheckout.control.gui.statedata.StateData;

public class MemberCardState implements GUIState, ActionListener {

	private StateHandler<GUIState> stateController;
	private JTextField input;
	private JLabel words;
	private String text = "";
	private JButton cancel;
	private JButton nonMember;
	private JButton scanCard;
	
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
		if (data instanceof BooleanStateData) {
			boolean result = (boolean) data.obtain();
			
			if (result) { // success on membership input
				stateController.setState(new BuyingState());

			} else { // unknown input
				GUIUtils
					.begin(words)
					.setText("Invalid membership number!")
					.waitFor(1.0f)
					.restore()
					.execute();

				GUIUtils
					.begin(input)
					.setError()
					.waitFor(0.3f)
					.restore()
					.execute();

			}
		}
	}

	/**
	 * This sets up all the widgets to be used by the membercard state
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
		JLabel topStatement = new JLabel("Welcome!");
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
		words = new JLabel("Please enter your membership number.");
		words.setBackground(Color.RED);
		words.setFont(new Font("Arial", Font.BOLD, 40));
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
		centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 330, 0, 20));
		
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
		
		nonMember = new JButton("Non-Member");
		nonMember.setFont(new Font("Arial", Font.BOLD, 36));
		nonMember.addActionListener(this);

		// set size of non member button
		Dimension buttonSize = new Dimension(300, 75);
		nonMember.setSize(buttonSize);
		nonMember.setPreferredSize(buttonSize);
		nonMember.setMinimumSize(buttonSize);
		nonMember.setMaximumSize(buttonSize);
		
		scanCard = new JButton("Scan Member Card");
		scanCard.setFont(new Font("Arial", Font.BOLD, 26));
		scanCard.addActionListener(this);

		// set size of non member button
		scanCard.setSize(buttonSize);
		scanCard.setPreferredSize(buttonSize);
		scanCard.setMinimumSize(buttonSize);
		scanCard.setMaximumSize(buttonSize);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.weightx = 0.9;
		centerPanel.add(keyPadPanel, constraints);
		
		constraints.gridx = 1;
		constraints.weightx = 0.1;
		constraints.anchor = GridBagConstraints.LINE_END;
//		centerPanel.add(scanCard, constraints);
		
		constraints.gridx = 1;
		constraints.weightx = 0.1;
		constraints.anchor = GridBagConstraints.LAST_LINE_END;
		centerPanel.add(nonMember, constraints);
		
		JPanel cancelPanel = new JPanel();
		cancel = new JButton("Cancel");
		cancel.setFont(new Font("Arial", Font.BOLD, 36));
		cancel.addActionListener(this);

		// set size of cancel button
		cancel.setSize(buttonSize);
		cancel.setPreferredSize(buttonSize);
		cancel.setMinimumSize(buttonSize);
		cancel.setMaximumSize(buttonSize);
		cancelPanel.add(cancel);
		
		
		mainPanel.add(topPanel);
		mainPanel.add(wordPanel);
		mainPanel.add(input);
		mainPanel.add(centerPanel);
		mainPanel.add(cancelPanel);

		return mainPanel;
	}

	/**
	 * 
	 */
	@Override
	public ReducedState reduce() {
		return new MembershipReducedState(text);
	}

	/**
	 * This reacts to button presses
	 * @param e the button being pressed
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		// The go back button takes user back to buying screen
		if(button == nonMember || button == scanCard) {
			stateController.setState(new BuyingState());
			
		} else if (button == scanCard) {
		
		} else if(button == cancel) {
			stateController.setState(new StartState());
			
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
				stateController.notifyListeners(new MemberStateData(text));
				/*
				if(text.length() > 0) {
					stateController.setState(new BuyingState());
				}
				*/
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
class MembershipReducedState extends ReducedState {

	private String data;

	public MembershipReducedState(String accountNumber) {
		this.data = accountNumber;
	}

	@Override
	public Object getData() {
		return data;
	}

}
