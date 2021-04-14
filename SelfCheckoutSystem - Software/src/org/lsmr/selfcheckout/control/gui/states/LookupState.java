package org.lsmr.selfcheckout.control.gui.states;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import org.lsmr.selfcheckout.control.gui.statedata.InsertBarcodedProductData;
import org.lsmr.selfcheckout.control.gui.statedata.InsertPLUProductData;
import org.lsmr.selfcheckout.control.gui.statedata.LookupStateData;
import org.lsmr.selfcheckout.control.gui.statedata.ProductStateData;
import org.lsmr.selfcheckout.control.gui.statedata.StateData;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;
import org.lsmr.selfcheckout.products.Product;

public class LookupState implements GUIState, ActionListener {

	private StateHandler<GUIState> stateController;
	private JTextField input;
	private String text = "";
	private JButton goBack;
	private JLabel words;
	private Product inputProduct;
	private JPanel mainPanel;
	private JLabel topStatement;
	private JPanel topPanel;
	private JPanel wordPanel;
	private int keypadWidth = 975;
	private JPanel lookUpPanel;
	private JPanel goBackPanel;

	/**
	 * This sets up all of the widgets to be used on the look up state screen
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
		if (data == null) {
			GUIUtils
				.begin(words)
				.setText("Unknown item!")
				.waitFor(1.0f)
				.setText("Type in the item's description")
				.execute();
			GUIUtils
				.begin(input)
				.setError()
				.waitFor(0.5f)
				.restore()
				.execute();
		} else if (data instanceof ProductStateData) {
			inputProduct = (Product) data.obtain();
			
			if (inputProduct instanceof PLUCodedProduct) {
				// don't insert prpoduct - we pass it to the next state to get a weighing of it
				stateController.notifyListeners(new InsertPLUProductData((PLUCodedProduct) inputProduct));
				stateController.setState(new BuyingState());
			} else if (inputProduct instanceof BarcodedProduct) {
				stateController.notifyListeners(new InsertBarcodedProductData((BarcodedProduct) inputProduct)); // directly insert product into checkout
				stateController.setState(new BuyingState());
			}
		}
	}

	/**
	 * 
	 */
	@Override
	public JPanel getPanel() {

		mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 50, 50));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		// top panel with title and logo
		setTopPanel();
		setTopStatement();
		ImageIcon coopImg = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/cooplogo.png");
		Image coOpImg = coopImg.getImage() ;  
		Image newCoOpImg = coOpImg.getScaledInstance( 100, 100,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon coOpImgResized = new ImageIcon(newCoOpImg);
		JLabel coOpLogo = new JLabel(coOpImgResized);
		topPanel.add(topStatement, BorderLayout.CENTER);
		topPanel.add(coOpLogo, BorderLayout.EAST);

		// panel with statement to input item's description
		setWordPanel();
		setWords();
		wordPanel.add(words);


		// the text field that will display user input
		setInput();
		// the panel with the keyboard for user input
		setLookUpPanel(this);

		//numbers?
		
		setGoBackPanel();
		setGoBackButton();
		goBack.addActionListener(this);
		goBackPanel.add(goBack, BorderLayout.EAST);

		mainPanel.add(topPanel);
		mainPanel.add(wordPanel);
		mainPanel.add(input);
		mainPanel.add(lookUpPanel);
		mainPanel.add(goBackPanel, BorderLayout.EAST);

		return mainPanel;
	}
	
	private void setMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 50, 50));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
	}
	
	protected JPanel getMainPanel() {
		setMainPanel();
		return mainPanel;
	}
	
	private void setTopStatement() {
		topStatement = new JLabel("Look Up Item");
		topStatement.setFont(new Font("Arial", Font.BOLD, 60));
	}
	
	protected JLabel getTopStatement() {
		setTopStatement();
		return topStatement;
	}
	
	private void setTopPanel() {
		topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout(200, 0));
		topPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 0, 50));
	}
	
	protected JPanel getTopPanel() {
		setTopPanel();
		return topPanel;
	}
	
	
	private void setWordPanel() {
		wordPanel = new JPanel();
		wordPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 50, 70));
	}
	
	protected JPanel getwordPanel() {
		setWordPanel();
		return wordPanel;
	}
	
	private void setWords() {
		words = new JLabel("Type in the item's description.");
		words.setFont(new Font("Arial", Font.BOLD, 40));
	}
	
	protected JLabel getWords() {
		setWords();
		return words;
	}
	
	private void setKeypadWidth(int keypadWidth) {
		this.keypadWidth = keypadWidth;
	}
	
	private int getKeypadWidth() {
		return keypadWidth;
	}
	
	private void setInput() {
		input = new JTextField();
		input.setMaximumSize(new Dimension(keypadWidth, 50));
		input.setEditable(false);
		input.setFont(new Font("Arial", Font.PLAIN, 30));
		input.setBackground(Color.WHITE);
	}
	
	protected JTextField getInput() {
		setInput();
		return input;
	}
	
	private void setLookUpPanel(ActionListener actionListener) {
		lookUpPanel = new JPanel();
		lookUpPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 0, 100));
		lookUpPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridConstraints = new GridBagConstraints();

		// grid constraints for first row
		gridConstraints.gridy = 0;
		gridConstraints.ipadx = 10;
		gridConstraints.ipady = 10;
		gridConstraints.weightx = 0.25;
		gridConstraints.weighty = 0.25;

		gridConstraints.gridx = 0;
		lookUpPanel.add(new JButton("Q"), gridConstraints);
		gridConstraints.gridx = 1;
		lookUpPanel.add(new JButton("W"), gridConstraints);
		gridConstraints.gridx = 2;
		lookUpPanel.add(new JButton("E"), gridConstraints);
		gridConstraints.gridx = 3;
		lookUpPanel.add(new JButton("R"), gridConstraints);
		gridConstraints.gridx = 4;
		lookUpPanel.add(new JButton("T"), gridConstraints);
		gridConstraints.gridx = 5;
		lookUpPanel.add(new JButton("Y"), gridConstraints);
		gridConstraints.gridx = 6;
		lookUpPanel.add(new JButton("U"), gridConstraints);
		gridConstraints.gridx = 7;
		lookUpPanel.add(new JButton("I"), gridConstraints);
		gridConstraints.gridx = 8;
		lookUpPanel.add(new JButton("O"), gridConstraints);
		gridConstraints.gridx = 9;
		lookUpPanel.add(new JButton("P"), gridConstraints);

		//grid constraints for the second row
		gridConstraints.gridy = 1;
		gridConstraints.ipadx = 10;
		gridConstraints.ipady = 10;
		gridConstraints.weightx = 0.25;
		gridConstraints.weighty = 0.25;

		gridConstraints.gridx = 0;
		lookUpPanel.add(new JButton("A"), gridConstraints);
		gridConstraints.gridx = 1;
		lookUpPanel.add(new JButton("S"), gridConstraints);
		gridConstraints.gridx = 2;
		lookUpPanel.add(new JButton("D"), gridConstraints);
		gridConstraints.gridx = 3;
		lookUpPanel.add(new JButton("F"), gridConstraints);
		gridConstraints.gridx = 4;
		lookUpPanel.add(new JButton("G"), gridConstraints);
		gridConstraints.gridx = 5;
		lookUpPanel.add(new JButton("H"), gridConstraints);
		gridConstraints.gridx = 6;
		lookUpPanel.add(new JButton("J"), gridConstraints);
		gridConstraints.gridx = 7;
		lookUpPanel.add(new JButton("K"), gridConstraints);
		gridConstraints.gridx = 8;
		lookUpPanel.add(new JButton("L"), gridConstraints);
		JButton ok = new JButton("OK");
		gridConstraints.gridx = 9;
		lookUpPanel.add(ok, gridConstraints);

		//grid constraints for the third row
		gridConstraints.gridy = 2;
		gridConstraints.ipadx = 10;
		gridConstraints.ipady = 10;
		gridConstraints.weightx = 0.25;
		gridConstraints.weighty = 0.25;

		gridConstraints.gridx = 0;
		lookUpPanel.add(new JButton("Z"), gridConstraints);
		gridConstraints.gridx = 1;
		lookUpPanel.add(new JButton("X"), gridConstraints);
		gridConstraints.gridx = 2;
		lookUpPanel.add(new JButton("C"), gridConstraints);
		gridConstraints.gridx = 3;
		lookUpPanel.add(new JButton("V"), gridConstraints);
		gridConstraints.gridx = 4;
		lookUpPanel.add(new JButton("B"), gridConstraints);
		gridConstraints.gridx = 5;
		lookUpPanel.add(new JButton("N"), gridConstraints);
		gridConstraints.gridx = 6;
		lookUpPanel.add(new JButton("M"), gridConstraints);
		JButton delete = new JButton("Delete");
		gridConstraints.gridx = 7;
		gridConstraints.gridwidth = 2;
		lookUpPanel.add(delete, gridConstraints);

		//grid constraints for forth row (space bar)
		gridConstraints.gridy = 3;
		gridConstraints.ipadx = 10;
		gridConstraints.ipady = 10;
		gridConstraints.weightx = 0.25;
		gridConstraints.weighty = 0.25;

		JButton space = new JButton("Space");
		gridConstraints.gridx = 3;
		gridConstraints.gridwidth = 4;
		lookUpPanel.add(space, gridConstraints);

		for (Component button : lookUpPanel.getComponents()) {
			((JButton) button).addActionListener(actionListener);
			if(button != delete && button != space) {
				Dimension keySize = new Dimension(80, 40);
				button.setSize(keySize);
				button.setPreferredSize(keySize);
				button.setMinimumSize(keySize);
				button.setMaximumSize(keySize);
				if(button != ok) {
					((JButton) button).setFont(new Font("Arial", Font.BOLD, 40));
				} else {
					((JButton) button).setFont(new Font("Arial", Font.BOLD, 36));
				}
			} else if (button == delete){
				Dimension delSize = new Dimension(180, 40);
				button.setSize(delSize);
				button.setPreferredSize(delSize);
				button.setMinimumSize(delSize);
				button.setMaximumSize(delSize);
				((JButton) button).setFont(new Font("Arial", Font.BOLD, 36));

			} else if(button == space) {
				Dimension spaceSize = new Dimension(300, 40);
				button.setSize(spaceSize);
				button.setPreferredSize(spaceSize);
				button.setMinimumSize(spaceSize);
				button.setMaximumSize(spaceSize);
				((JButton) button).setFont(new Font("Arial", Font.BOLD, 36));
			}
		}

	}
	
	protected JPanel getLookUpPanel(ActionListener actionListener) {
		setLookUpPanel(actionListener);
		return lookUpPanel;
	}

	private void setGoBackPanel() {
		goBackPanel = new JPanel();
		goBackPanel.setLayout(new BorderLayout());
	}
	
	protected JPanel getGoBackPanel() {
		setGoBackPanel();
		return goBackPanel;
	}
	
	private void setGoBackButton() {
	
				// image of black arrow downloaded from below website
				// https://www.pikpng.com/downpngs/oxJooi_simpleicons-interface-undo-black-arrow-pointing-to-tanda/
				
				setGoBackPanel();
				ImageIcon arrow = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/black arrow.png");
				Image img = arrow.getImage() ;  
				Image newimg = img.getScaledInstance( 50, 50,  java.awt.Image.SCALE_SMOOTH) ;  
				ImageIcon arrowResized = new ImageIcon(newimg);

				goBack = new JButton();
				goBack.setLayout(new BorderLayout()); //so we can add an icon
				JLabel iconLabel = new JLabel(arrowResized);
				JLabel back = new JLabel("Go Back", SwingConstants.CENTER);

				back.setFont(new Font("Arial", Font.BOLD, 40));
				goBack.add(back, BorderLayout.CENTER);
				goBack.add(iconLabel, BorderLayout.WEST);

				// set size of go back button
				Dimension backSize = new Dimension(300, 40);
				goBack.setSize(backSize);
				goBack.setPreferredSize(backSize);
				goBack.setMinimumSize(backSize);
				goBack.setMaximumSize(backSize);
	}
	
	protected JButton getGoBackButton() {
			setGoBackButton();
			return goBack;
	}
	/**
	 * This reduces the screen to only its pertinent information which is just the text
	 * @return the reduced state of the look up items screen
	 */
	@Override
	public ReducedState reduce() {
		return new LookupReducedState(inputProduct);

	}

	/**
	 *  This reacts to button presses
	 *  @param arg0  the button being pressed
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		JButton button = (JButton) arg0.getSource();

		if(button == goBack) {
			stateController.setState(new BuyingState());
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

/**
 * 
 * This allows to reduce the state of the key pad state to only the input text
 *
 */
class LookupReducedState extends ReducedState {

	private Product data;

	public LookupReducedState(Product p) {
		this.data = p;
	}

	@Override
	public Object getData() {
		return data;
	}

}