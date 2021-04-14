package org.lsmr.selfcheckout.control.gui.states;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.lsmr.selfcheckout.control.gui.StateHandler;
import org.lsmr.selfcheckout.control.gui.statedata.BalanceStateData;
import org.lsmr.selfcheckout.control.gui.statedata.StateData;

public class CashPaymentState implements GUIState, ActionListener {

	private StateHandler<GUIState> stateController;
	private JButton goBack;
	private JLabel duePrintOut;
	private float cost;

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
		if (data instanceof BalanceStateData) {
			cost = (float) data.obtain();
			duePrintOut.setText(String.format("Amount Due: $%.2f", cost));

			if (cost <= 0) {
				stateController.setState(new EndState());
			}
		}
	}

	/**
	 *  This sets up all of the widgets to be used on the keypad state screen
	 */
	@Override
	public JPanel getPanel() {

		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 100, 30));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		// set up statement at top of screen
		// image for coop logo downloaded from website below
		// https://www.google.com/search?q=coop+png&tbm=isch&ved=2ahUKEwjC9bTd-PHvAhU8AzQIHSiLAkkQ2-cCegQIABAA&oq=coop+png&gs_lcp=CgNpbWcQAzIECAAQQzICCAAyBggAEAcQHjIGCAAQBxAeMgYIABAHEB4yBggAEAcQHjIGCAAQBxAeMgYIABAHEB4yBggAEAcQHjIGCAAQBxAeUM-sA1jPrANg67IDaABwAHgAgAFDiAFDkgEBMZgBAKABAaoBC2d3cy13aXotaW1nwAEB&sclient=img&ei=MLFwYMKdCryG0PEPqJaKyAQ&bih=619&biw=1280#imgrc=EmUG01nblMHYrM
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout(275, 0));
		JLabel topStatement = new JLabel("Cash Payment");
		ImageIcon coopImg = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/cooplogo.png");
		Image coOpImg = coopImg.getImage() ;  
		Image newCoOpImg = coOpImg.getScaledInstance( 100, 100,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon coOpImgResized = new ImageIcon(newCoOpImg);
		JLabel coOpLogo = new JLabel(coOpImgResized);
		topStatement.setFont(new Font("Arial", Font.BOLD, 60));
		topPanel.setBorder(BorderFactory.createEmptyBorder(50, 30, 10, 30));
		topPanel.add(topStatement, BorderLayout.CENTER);
		topPanel.add(coOpLogo, BorderLayout.EAST);

		// panel with statement to insert cash
		JPanel wordPanel = new JPanel();
		wordPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
		JLabel words = new JLabel("Insert Cash");
		words.setFont(new Font("Arial", Font.BOLD, 40));
		wordPanel.add(words);// panel with statement to input item's description

		// panel with statement on how user can go back to scanning
		JPanel wordPanel2 = new JPanel();
		wordPanel2.setBorder(BorderFactory.createEmptyBorder(0, 50, 150, 50));
		JLabel words2 = new JLabel("Press \"Go Back\" to continue scanning.");
		words2.setFont(new Font("Arial", Font.BOLD, 40));
		wordPanel2.add(words2);

		// amount due panel
		duePrintOut = new JLabel();
		duePrintOut.setText("Amount Due: $0.00");
		duePrintOut.setFont(new Font("Arial", Font.BOLD, 40));
		JPanel duePanel = new JPanel();
		duePanel.add(duePrintOut);

		// amount paid panel
		JLabel paidPrintOut = new JLabel();
		paidPrintOut.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		paidPrintOut.setText("Amount Paid: $0.00");
		paidPrintOut.setFont(new Font("Arial", Font.BOLD, 30));
		JPanel paidPanel = new JPanel();
		paidPanel.add(paidPrintOut);

		// go back button
		// image of black arrow downloaded from below website
		// https://www.pikpng.com/downpngs/oxJooi_simpleicons-interface-undo-black-arrow-pointing-to-tanda/
		JPanel goBackPanel = new JPanel();
		goBackPanel.setLayout(new BorderLayout());
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

		goBackPanel.add(goBack, BorderLayout.EAST);
		goBackPanel.setBorder(BorderFactory.createEmptyBorder(0, 100, 0, 100)); // for margins


		mainPanel.add(topPanel);
		mainPanel.add(wordPanel);
		mainPanel.add(wordPanel2);
		mainPanel.add(duePanel);
		mainPanel.add(paidPanel);
		mainPanel.add(goBackPanel, BorderLayout.EAST);

		stateController.notifyListeners(new BalanceStateData(0));

		return mainPanel;
	}

	@Override
	public ReducedState reduce() {
		// should return the amount paid??
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		JButton button = (JButton) arg0.getSource();
		
		// The go back button takes user back to buying screen
		if(button == goBack) {
			stateController.setState(new BuyingState());

		}

	}

}
