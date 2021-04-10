package org.lsmr.selfcheckout.control.gui.states;

import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.lsmr.selfcheckout.control.gui.StateHandler;
import org.lsmr.selfcheckout.control.gui.statedata.StateData;

public class BlockState implements GUIState {

	private StateHandler<GUIState> stateController;
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
	 * This sets up the widgets for the block state
	 */
	@Override
	public JPanel getPanel() {

		// the main panel
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));

		// the block image panel
		// image for block icon downloaded from below website
		// http://clipart-library.com/clip-art/no-sign-transparent-background-4.htm
		JPanel blockPanel = new JPanel();
		ImageIcon blockImg = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/no-sign.png");
		Image blocKImg = blockImg.getImage() ;  
		Image newBlockImg = blocKImg.getScaledInstance( 500, 500,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon blockImgResized = new ImageIcon(newBlockImg);
		JLabel blockImgLabel = new JLabel(blockImgResized);
		blockPanel.add(blockImgLabel);

		mainPanel.add(blockPanel);

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

}
