package org.lsmr.selfcheckout.control.gui.states;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.lsmr.selfcheckout.control.gui.StateHandler;
import org.lsmr.selfcheckout.control.gui.statedata.BalanceStateData;
import org.lsmr.selfcheckout.control.gui.statedata.StateData;

public class CardPaymentState implements GUIState {

	private StateHandler<GUIState> stateController;
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
		if (data instanceof BalanceStateData) {
			cost = (float) data.obtain();
			duePrintOut.setText(String.format("Amount Due: $%.2f", cost));
		}
	}

	/**
	 *  This sets up all of the widgets to be used on the keypad state screen
	 */
	@Override
	public JPanel getPanel() {

		//****** do we want to add something that will pop up that says authorizing please wait?

		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 100, 30));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		// set up statement at top of screen
		// image for coop logo downloaded from website below
		// https://www.google.com/search?q=coop+png&tbm=isch&ved=2ahUKEwjC9bTd-PHvAhU8AzQIHSiLAkkQ2-cCegQIABAA&oq=coop+png&gs_lcp=CgNpbWcQAzIECAAQQzICCAAyBggAEAcQHjIGCAAQBxAeMgYIABAHEB4yBggAEAcQHjIGCAAQBxAeMgYIABAHEB4yBggAEAcQHjIGCAAQBxAeUM-sA1jPrANg67IDaABwAHgAgAFDiAFDkgEBMZgBAKABAaoBC2d3cy13aXotaW1nwAEB&sclient=img&ei=MLFwYMKdCryG0PEPqJaKyAQ&bih=619&biw=1280#imgrc=EmUG01nblMHYrM
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout(275, 0));
		JLabel topStatement = new JLabel("PIN Pad Entry");
		ImageIcon coopImg = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/cooplogo.png");
		Image coOpImg = coopImg.getImage() ;  
		Image newCoOpImg = coOpImg.getScaledInstance( 100, 100,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon coOpImgResized = new ImageIcon(newCoOpImg);
		JLabel coOpLogo = new JLabel(coOpImgResized);
		topStatement.setFont(new Font("Arial", Font.BOLD, 60));
		topPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));
		topPanel.add(topStatement, BorderLayout.CENTER);
		topPanel.add(coOpLogo, BorderLayout.EAST);

		// panel with statement to input item's description
		JPanel wordPanel = new JPanel();
		wordPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 10, 50));
		JLabel words = new JLabel("Use PIN Pad to complete transaction.");
		words.setFont(new Font("Arial", Font.BOLD, 40));
		wordPanel.add(words);// panel with statement to input item's description

		// lets user know how to cancel payment
		JPanel wordPanel2 = new JPanel();
		wordPanel2.setBorder(BorderFactory.createEmptyBorder(0, 50, 200, 50));
		JLabel words2 = new JLabel("Press \"Cancel\" on PIN Pad to cancel payment.");
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
		paidPrintOut.setText("Amount Paid: $0.00");
		paidPrintOut.setFont(new Font("Arial", Font.BOLD, 30));
		JPanel paidPanel = new JPanel();
		paidPanel.add(paidPrintOut);

		mainPanel.add(topPanel);
		mainPanel.add(wordPanel);
		mainPanel.add(wordPanel2);
		mainPanel.add(duePanel);
		mainPanel.add(paidPanel);

		stateController.notifyListeners(new BalanceStateData(0)); // request balance

		return mainPanel;
	}

	/**
	 * 
	 */
	@Override
	public ReducedState reduce() {
		// return amount paid??
		return null;
	}

}
