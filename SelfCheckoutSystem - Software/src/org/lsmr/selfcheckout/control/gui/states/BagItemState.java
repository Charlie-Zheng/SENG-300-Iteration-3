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
import org.lsmr.selfcheckout.control.gui.statedata.ScaleStateData;
import org.lsmr.selfcheckout.control.gui.statedata.StateData;

public class BagItemState implements GUIState, ActionListener {

	private StateHandler<GUIState> stateController;
	private JButton skipBag;

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
		if (data instanceof ScaleStateData) {
			stateController.setState(new BuyingState());
		}
	}

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
		JLabel topStatement = new JLabel("Bag Item");
		ImageIcon coopImg = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/cooplogo.png");
		Image coOpImg = coopImg.getImage() ;  
		Image newCoOpImg = coOpImg.getScaledInstance( 100, 100,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon coOpImgResized = new ImageIcon(newCoOpImg);
		JLabel coOpLogo = new JLabel(coOpImgResized);
		topStatement.setFont(new Font("Arial", Font.BOLD, 60));
		topPanel.setBorder(BorderFactory.createEmptyBorder(50, 30, 10, 30));
		topPanel.add(topStatement, BorderLayout.CENTER);
		topPanel.add(coOpLogo, BorderLayout.EAST);

		// panel with statement to place item in bagging area
		// image of lady bagging groceries downloaded from website below
		//https://www.clipartmax.com/middle/m2i8N4H7d3i8H7d3_vector-illustration-of-grocery-store-clerk-packs-groceries-bricklayer/		
		JPanel wordPanel = new JPanel();
		wordPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 10, 50));
		JLabel words = new JLabel("Please place item in the bagging area.");
		words.setFont(new Font("Arial", Font.BOLD, 40));
		wordPanel.add(words);// panel with statement to input item's description

		// panel for bag item image
		JPanel baggingPanel = new JPanel();
		ImageIcon bagImg = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/lady bagging.png");
		Image baGImg = bagImg.getImage() ;  
		Image newBagImg = baGImg.getScaledInstance( 450, 350,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon bagImgResized = new ImageIcon(newBagImg);
		JLabel bagLogo = new JLabel(bagImgResized);
		baggingPanel.add(bagLogo);

		// skip bagging button
		JPanel skipBagPanel = new JPanel();
		skipBagPanel.setLayout(new BorderLayout());
		skipBagPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 50));
		skipBag = new JButton("Skip Bagging Item");
		skipBag.setFont(new Font("Arial", Font.BOLD, 36));
		skipBag.addActionListener(this);

		// set size of skip bagging button
		Dimension skipSize = new Dimension(400, 75);
		skipBag.setSize(skipSize);
		skipBag.setPreferredSize(skipSize);
		skipBag.setMinimumSize(skipSize);
		skipBag.setMaximumSize(skipSize);
		skipBagPanel.add(skipBag, BorderLayout.EAST);

		mainPanel.add(topPanel);
		mainPanel.add(wordPanel);
		mainPanel.add(baggingPanel);
		mainPanel.add(skipBagPanel, BorderLayout.EAST);

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
	 * This reacts to buttons being pressed
	 * @param arg0 The button that is being pressed
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		JButton button = (JButton) arg0.getSource();

		if(button == skipBag) {
			//where we have to connect to code for scale weight??
			stateController.setState(new BuyingState());
		}
	}

}
