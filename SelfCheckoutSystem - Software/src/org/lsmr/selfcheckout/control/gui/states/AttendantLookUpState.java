package org.lsmr.selfcheckout.control.gui.states;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.lsmr.selfcheckout.control.gui.GUIUtils;
import org.lsmr.selfcheckout.control.gui.StateHandler;
import org.lsmr.selfcheckout.control.gui.statedata.InsertBarcodedProductData;
import org.lsmr.selfcheckout.control.gui.statedata.InsertPLUProductData;
import org.lsmr.selfcheckout.control.gui.statedata.LookupStateData;
import org.lsmr.selfcheckout.control.gui.statedata.ProductStateData;
import org.lsmr.selfcheckout.control.gui.statedata.StateData;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;
import org.lsmr.selfcheckout.products.Product;

public class AttendantLookUpState extends LookupState implements GUIState, ActionListener {

	private StateHandler<GUIState> stateController;
	private JButton goBack;
	private JTextField input;
	private String text = "";
	private JLabel words;
	
	@Override
	public void init(StateHandler<GUIState> stateController, ReducedState reducedState) {
		this.stateController = stateController;

	}

	@Override
	public void onDataUpdate(StateData<?> data) {

		if (data instanceof ProductStateData) {
			Product inputProduct = (Product) data.obtain();
			
			if (inputProduct instanceof PLUCodedProduct) {
				// don't insert prpoduct - we pass it to the next state to get a weighing of it
				stateController.notifyListeners(new InsertPLUProductData((PLUCodedProduct) inputProduct));
				stateController.setState(new AttendantAccessState());
				
			} else if (inputProduct instanceof BarcodedProduct) {
				stateController.notifyListeners(new InsertBarcodedProductData((BarcodedProduct) inputProduct)); // directly insert product into checkout
				stateController.setState(new AttendantAccessState());
			}
		}
	}

	@Override
	public JPanel getPanel() {

		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 100, 50));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		//mainPanel = new LookupState().getPanel();
		//mainPanel = new LookupState().getMainPanel();
		mainPanel.setBackground(Color.RED.darker());
		JPanel topPanel = new LookupState().getTopPanel();
		topPanel.setBackground(Color.RED.darker());
		JLabel topStatement = new LookupState().getTopStatement();
		topStatement.setForeground(Color.white);
		topPanel.add(topStatement, BorderLayout.WEST);
		
		JPanel wordPanel = new LookupState().getwordPanel();
		wordPanel.setBackground(Color.RED.darker());
		words = new LookupState().getWords();
		words.setForeground(Color.WHITE);
		wordPanel.add(words);
		JPanel lookUpPanel = new LookupState().getLookUpPanel(this);
		lookUpPanel.setBackground(Color.RED.darker());
		JPanel goBackPanel = new LookupState().getGoBackPanel();
		goBackPanel.setBackground(Color.RED.darker());
		goBack = new LookupState().getGoBackButton();
		goBack.addActionListener(this);
		goBackPanel.add(goBack, BorderLayout.EAST);
		
		input = new LookupState().getInput();
		
		
		mainPanel.add(topPanel);
		mainPanel.add(wordPanel);
		mainPanel.add(input);
		mainPanel.add(lookUpPanel);
		mainPanel.add(goBackPanel, BorderLayout.EAST);
		
		return mainPanel;
	}

	@Override
	public ReducedState reduce() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 *  This reacts to button presses
	 *  @param arg0  the button being pressed
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		JButton button = (JButton) arg0.getSource();

		if(button == goBack) {
			stateController.setState(new AttendantAccessState());
		} else {
			String buttonText = button.getText();

			// Takes the text of the buttons to make a decision of what action to perform
			if (buttonText.length() == 1) {
				text += buttonText;

			} else if(buttonText.equals("OK")) {
				if (text.length() < 3) {
					GUIUtils
						.begin(words)
						.setText("At least three characters must be in the search query")
						.waitFor(1.0f)
						.restore()
						.execute();
				} else {
					stateController.notifyListeners(new LookupStateData(text));
				}
				//add in conditions for if valid? if not add to go back
				//stateController.setState(new BuyingState());

			} else if(buttonText.equals("Delete")) {
				if (text.length() > 0) {
					text = text.substring(0, text.length()-1);
				}

			} else if(buttonText.equals("Space")) {
				text += " ";
			}
		}
		input.setText(text);
	}

}
