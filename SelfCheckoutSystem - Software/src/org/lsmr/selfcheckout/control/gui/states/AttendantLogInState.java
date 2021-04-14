package org.lsmr.selfcheckout.control.gui.states;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.lsmr.selfcheckout.control.gui.GUIUtils;
import org.lsmr.selfcheckout.control.gui.StateHandler;
import org.lsmr.selfcheckout.control.gui.statedata.AttendantLogInData;
import org.lsmr.selfcheckout.control.gui.statedata.StateData;

public class AttendantLogInState implements GUIState, ActionListener {
	
	
	//reciept
	//https://www.cleanpng.com/png-receipt-invoice-computer-icons-cash-register-1154206/download-png.html
	
	//coin
	//https://www.cleanpng.com/png-pile-of-coins-png-picture-10144/download-png.html
	
	//banknotes
	//https://www.cleanpng.com/png-money-banknote-illustration-cartoon-hand-painted-b-141687/download-png.html
	
	
	//ink
	// https://www.seekpng.com/idown/u2e6e6u2q8r5i1o0_ink-bottle-ink-bottle-ink-clip-art/
	
	//white hand holding coins
	// https://www.cleanpng.com/png-thumb-money-currency-hand-hands-holding-gold-coins-364860/download-png.html
	
	//black icon hand remove coin
	//https://www.cleanpng.com/png-currency-symbol-money-dollar-sign-coin-2005269/download-png.html
	
	// other black hand icon for remove coins - two coins
	//https://www.pngfind.com/download/xoxTho_search-through-more-than-735000-free-icons-browse/
	
	
	// other black icon for coin
	// https://www.cleanpng.com/png-computer-icons-money-finance-1549437/download-png.html
	
	//black icon for banknore remove
	// https://www.pngkit.com/downpic/u2q8a9r5y3e6q8i1_vector-library-download-refund-icon-free-download-png/
	
	//guy at self checkout for customer options
	//https://www.pngitem.com/download/iwimxbR_supermarket-clipart-transparent-grocery-cart-clipart-png-png/
	
	//clerk in purple
	// https://www.clipartkey.com/downpng/iiwbimw_employee-clipart-shop-owner-grocery-store-employee-clipart/
	
	// disabled stamp
	// https://www.google.com/search?q=disabled+stamp+clipart+transparent+background&tbm=isch&ved=2ahUKEwjgnY__pvnvAhWKFTQIHQOOBBEQ2-cCegQIABAA&oq=disabled+stamp+clipart+transparent+background&gs_lcp=CgNpbWcQA1DArQJY87QCYPK6AmgAcAB4AIABsgGIAYoJkgEDMC44mAEAoAEBqgELZ3dzLXdpei1pbWfAAQE&sclient=img&ei=OY10YKCdA4qr0PEPg5ySiAE&bih=562&biw=1280#imgrc=geARyhpd-K9z2M
	
	
	private StateHandler<GUIState> stateController;
	private JTextField employeeNumber;
	private JTextField pin;
	private JTextField activeTextField;

	@Override
	public void init(StateHandler<GUIState> stateController, ReducedState reducedState) {
		this.stateController = stateController;

	}

	@Override
	public void onDataUpdate(StateData<?> data) {
		// TODO Auto-generated method stub
		if(data instanceof AttendantLogInData) {
			if(((AttendantLogInData) data).isSuccessful()) {
				stateController.setState(new AttendantState());
			}else {
				//display wrong pin message
			}

		}

	}

	@Override
	public JPanel getPanel() {

		final int keypadWidth = 500;
		Dimension size = new Dimension(keypadWidth, keypadWidth);
		//the main panel
		JPanel mainPanel = new JPanel();
		//BorderLayout mainLayout = new BorderLayout();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 50, 50));
		//mainLayout.setHgap(10);

		// set up statement at top of screen
		// image for coop logo downloaded from website below
		// https://www.google.com/search?q=coop+png&tbm=isch&ved=2ahUKEwjC9bTd-PHvAhU8AzQIHSiLAkkQ2-cCegQIABAA&oq=coop+png&gs_lcp=CgNpbWcQAzIECAAQQzICCAAyBggAEAcQHjIGCAAQBxAeMgYIABAHEB4yBggAEAcQHjIGCAAQBxAeMgYIABAHEB4yBggAEAcQHjIGCAAQBxAeUM-sA1jPrANg67IDaABwAHgAgAFDiAFDkgEBMZgBAKABAaoBC2d3cy13aXotaW1nwAEB&sclient=img&ei=MLFwYMKdCryG0PEPqJaKyAQ&bih=619&biw=1280#imgrc=EmUG01nblMHYrM
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout(275, 0));
		JLabel topStatement = new JLabel("Employee Log In");
		ImageIcon coopImg = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/cooplogo.png");
		Image coOpImg = coopImg.getImage() ;  
		Image newCoOpImg = coOpImg.getScaledInstance( 100, 100,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon coOpImgResized = new ImageIcon(newCoOpImg);
		JLabel coOpLogo = new JLabel(coOpImgResized);
		topStatement.setFont(new Font("Arial", Font.BOLD, 60));
		topPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 0, 50));
		topPanel.add(topStatement, BorderLayout.CENTER);
		topPanel.add(coOpLogo, BorderLayout.EAST);

		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new GridLayout(0, 2, 20, 0));
		middlePanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 20));

		JPanel inPanel = new JPanel();
		inPanel.setLayout(new GridBagLayout());
		Dimension inPanelSize = new Dimension(500, 250);
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
		inputPanel.setMaximumSize(inPanelSize);
		inputPanel.setPreferredSize(inPanelSize);
		inputPanel.setSize(inPanelSize);
		inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 97, 40, 0));
		inputPanel.setBackground(Color.RED.darker());
		inputPanel.setOpaque(true);


		JLabel employeeInputRequest = new JLabel("Employee Number");
		employeeInputRequest.setFont(new Font("Arial", Font.PLAIN, 30));
		employeeInputRequest.setForeground(Color.WHITE);



		// the text field that will display user input
		employeeNumber = new JTextField();
		employeeNumber.setMaximumSize(new Dimension(600, 50));
		employeeNumber.setEditable(false);
		employeeNumber.setFont(new Font("Arial", Font.PLAIN, 30));
		employeeNumber.setBackground(Color.WHITE);
		employeeNumber.addMouseListener(new MouseAdapter() {

			
			@Override
			public void mousePressed(MouseEvent e) {
				activeTextField = employeeNumber;
				GUIUtils.begin(activeTextField)
					.setBgColor(new Color(80, 255, 80))
					.waitFor(0.3f)
					.setBgColor(Color.white)
					.execute();
				
			}
			
		});



		JLabel pinRequest = new JLabel("Password");
		pinRequest.setFont(new Font("Arial", Font.PLAIN, 30));
		pinRequest.setForeground(Color.WHITE);
		pin = new JPasswordField();
		pin.setMaximumSize(new Dimension(600, 50));
		pin.setEditable(false);
		pin.setFont(new Font("Arial", Font.PLAIN, 30));
		pin.setBackground(Color.WHITE);
		pin.addMouseListener(new MouseAdapter() {

			
			@Override
			public void mousePressed(MouseEvent e) {
				activeTextField = pin;
				GUIUtils.begin(activeTextField)
				.setBgColor(new Color(80, 255, 80))
				.waitFor(0.3f)
				.setBgColor(Color.white)
				.execute();
			}
			
		});

		activeTextField = employeeNumber;
		
		inputPanel.add(employeeInputRequest);
		inputPanel.add(newSpacing(1, 10));
		inputPanel.add(employeeNumber);
		inputPanel.add(newSpacing(1, 30));
		inputPanel.add(pinRequest);
		inputPanel.add(newSpacing(1, 10));
		inputPanel.add(pin);
		inPanel.add(inputPanel);


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

		middlePanel.add(inPanel);
		middlePanel.add(keyPadPanel);

		mainPanel.add(topPanel);
		mainPanel.add(middlePanel);

		return mainPanel;
	}

	/**
	 * Creates a spacing placeholder to place certain elements at specific locations
	 * @param width the spacing width
	 * @param height the spacing height
	 * @return a new JComponent that acts as a spacer
	 */
	private JComponent newSpacing(int width, int height) {
		JPanel spacing = new JPanel();
		spacing.setBackground(null);
		Dimension size = new Dimension(width, height);

		// setting all these makes it work
		spacing.setSize(size);
		spacing.setPreferredSize(size);
		spacing.setMinimumSize(size);
		spacing.setMaximumSize(size);

		return spacing;
	}

	/**
	 * This returns only the necessary data needed to be known about the state
	 */
	@Override
	public ReducedState reduce() {
	//	return new LogInReducedState(employNumText, pinText);
		return null;
	}


	/**
	 * This reacts to button presses
	 * @param e This is the button that is being pushed
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

	
		
			JButton button = (JButton) e.getSource();

			String buttonText = button.getText();


		// Takes the text of the buttons to make a decision of what action to perform
			if (Character.isDigit(buttonText.charAt(0))) {
				activeTextField.setText(activeTextField.getText() + buttonText);
				
			} else if (buttonText.equals("OK")) {
				stateController.notifyListeners(new AttendantLogInData(Integer.parseInt(employeeNumber.getText()), Integer.parseInt(pin.getText())));

				//else pop up screen with wrong pin message?
			
		} else if (buttonText.equals("Delete")) {
			activeTextField.setText(activeTextField.getText().substring(0, activeTextField.getText().length() - 1));
		}

	}

}

/**
 * 
 * This allows to reduce the state of the key pad state to only the input text
 *
 */
/*class LogInReducedState extends ReducedState {
	// ** i need help with this
	private String employeeNumber;
	private String pin;

	public LogInReducedState(String employeeNumber, String pin) {
		this.employeeNumber = employeeNumber;
		this.pin = pin;
	}

	@Override
	public Object getData() {
		return employeeNumber;
	}
}*/
