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

import org.lsmr.selfcheckout.control.gui.StateHandler;
import org.lsmr.selfcheckout.control.gui.statedata.StateData;

public class StartState implements GUIState, ActionListener {

	private StateHandler<GUIState> stateController;
	private JButton startButton;
	private JButton ownBag;

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
	 * This sets up all the widgets for the start state screen
	 */
	@Override
	public JPanel getPanel() {
		
		// the main panel
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 100, 50));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		// top welcome panel with logo
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

		// panel with statement on how to start
		JPanel wordPanel = new JPanel();
		wordPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
		JLabel words = new JLabel("Please touch \"Start\" to begin.");
		words.setFont(new Font("Arial", Font.BOLD, 40));
		wordPanel.add(words);


		// image panel
		// image of self checkout downloaded from the website below
		// https://www.hiclipart.com/free-transparent-background-png-clipart-jvgub/download
		JPanel imagePanel = new JPanel();
		ImageIcon startImg = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/self checkout.png");
		Image strtImg = startImg.getImage() ;  
		Image newStartImg = strtImg.getScaledInstance( 375, 375,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon startImgResized = new ImageIcon(newStartImg);
		JLabel startLogo = new JLabel(startImgResized);
		imagePanel.add(startLogo);


		// set size of  buttons
		Dimension startSize = new Dimension(300, 75);
		Dimension ownBagSize = new Dimension(500, 75);
		
		// panel for buttons
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BorderLayout());
		buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 100));
		startButton = new JButton("Start");
		startButton.setFont(new Font("Arial", Font.BOLD, 36));
		startButton.addActionListener(this);
		startButton.setSize(startSize);
		startButton.setPreferredSize(startSize);
		startButton.setMinimumSize(startSize);
		startButton.setMaximumSize(startSize);
		ownBag = new JButton("I brought my own bag");
		ownBag.setFont(new Font("Arial", Font.BOLD, 36));
		ownBag.addActionListener(this);
		ownBag.setSize(ownBagSize);
		ownBag.setPreferredSize(ownBagSize);
		ownBag.setMinimumSize(ownBagSize);
		ownBag.setMaximumSize(ownBagSize);
		buttonsPanel.add(startButton, BorderLayout.EAST);
		buttonsPanel.add(ownBag, BorderLayout.WEST);

		mainPanel.add(topPanel);
		mainPanel.add(wordPanel);
		mainPanel.add(imagePanel);
		mainPanel.add(buttonsPanel);

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

	/**
	 * This reacts to the buttons being pressed
	 * @param arg0 The button being pressed
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		JButton button = (JButton) arg0.getSource();

		if(button == startButton) {
			stateController.setState(new MemberCardState());
			
		} else if(button == ownBag) {
			stateController.setState(new AddBagState());
			
		}

	}

}
