package org.lsmr.selfcheckout.control.gui.states;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
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

import org.lsmr.selfcheckout.control.gui.StateHandler;
import org.lsmr.selfcheckout.control.gui.statedata.StateData;

public class WeightWrongState implements GUIState, ActionListener {

	private StateHandler<GUIState> stateController;
	private JButton logIn;

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
	 * This sets up the widgets for the weight wrong state
	 */
	@Override
	public JPanel getPanel() {

		// the main panel
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 100, 50));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		// set up statement at top of screen
		// image for coop logo downloaded from website below
		// https://www.google.com/search?q=coop+png&tbm=isch&ved=2ahUKEwjC9bTd-PHvAhU8AzQIHSiLAkkQ2-cCegQIABAA&oq=coop+png&gs_lcp=CgNpbWcQAzIECAAQQzICCAAyBggAEAcQHjIGCAAQBxAeMgYIABAHEB4yBggAEAcQHjIGCAAQBxAeMgYIABAHEB4yBggAEAcQHjIGCAAQBxAeUM-sA1jPrANg67IDaABwAHgAgAFDiAFDkgEBMZgBAKABAaoBC2d3cy13aXotaW1nwAEB&sclient=img&ei=MLFwYMKdCryG0PEPqJaKyAQ&bih=619&biw=1280#imgrc=EmUG01nblMHYrM
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout(250, 0));
		JLabel topStatement = new JLabel("Error");
		ImageIcon coopImg = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/cooplogo.png");
		Image coOpImg = coopImg.getImage() ;  
		Image newCoOpImg = coOpImg.getScaledInstance( 100, 100,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon coOpImgResized = new ImageIcon(newCoOpImg);
		JLabel coOpLogo = new JLabel(coOpImgResized);
		topStatement.setFont(new Font("Arial", Font.BOLD, 60));
		topPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 0, 30));
		topPanel.add(topStatement, BorderLayout.CENTER);
		topPanel.add(coOpLogo, BorderLayout.EAST);

		// panel with statement to place item in bagging area
		// image of guy carrying groceries downloaded from website below
		//https://www.clipartmax.com/middle/m2i8d3m2K9m2G6b1_vector-illustration-of-supermarket-grocery-store-shopper-carry-bags-clipart/		
		JPanel wordPanel = new JPanel();
		wordPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
		wordPanel.setLayout(new GridBagLayout());
		JLabel words = new JLabel("Unexpected weight in bagging area! Please wait for an attendant.");
		//JLabel words2 = new JLabel("Please wait for an attendant.");
		words.setFont(new Font("Arial", Font.BOLD, 35));
		//words2.setFont(new Font("Arial", Font.BOLD, 35));
		wordPanel.add(words);// panel with statement to input item's description
		//wordPanel.add(words2);


		// error icon panel
		// error icon image downloaded from website below
		//https://www.clipartmax.com/download/m2i8i8m2b1A0A0d3_png-file-error-icono/
		JPanel errorPanel = new JPanel();
		ImageIcon errorImg = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/error icon.png");
		Image errImg = errorImg.getImage() ;  
		Image newErrorImg = errImg.getScaledInstance( 375, 375,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon errorImgResized = new ImageIcon(newErrorImg);
		JLabel errorImgLabel = new JLabel(errorImgResized);
		errorPanel.add(errorImgLabel);
		
		Dimension buttonSize = new Dimension(250, 60);
		JPanel logInPanel = new JPanel();
		logInPanel.setLayout(new BorderLayout());
		logInPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 30));
		logIn = new JButton("Log In");
		logIn.setFont(new Font("Arial", Font.BOLD, 40));
		logIn.addActionListener(this);
		logIn.setSize(buttonSize);
		logIn.setPreferredSize(buttonSize);
		logIn.setMinimumSize(buttonSize);
		logIn.setMaximumSize(buttonSize);
		logInPanel.add(logIn, BorderLayout.EAST);

		mainPanel.add(topPanel);
		mainPanel.add(wordPanel);
		mainPanel.add(errorPanel);
		mainPanel.add(logInPanel);

		return mainPanel;
	}

	/**
	 * 
	 */
	@Override
	public ReducedState reduce() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		
		if(button == logIn) {
			stateController.setState(new AttendantLogInState());
		}
		
	}

}
