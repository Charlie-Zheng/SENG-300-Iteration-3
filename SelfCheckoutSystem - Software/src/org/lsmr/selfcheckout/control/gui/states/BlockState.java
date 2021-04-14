package org.lsmr.selfcheckout.control.gui.states;

import java.awt.BorderLayout;
import java.awt.Color;
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

import org.lsmr.selfcheckout.control.PhysicalAttendantSimulatorWindow;
import org.lsmr.selfcheckout.control.gui.StateHandler;
import org.lsmr.selfcheckout.control.gui.statedata.AttendantLogInData;
import org.lsmr.selfcheckout.control.gui.statedata.StateData;

public class BlockState implements GUIState, ActionListener {

	private StateHandler<GUIState> stateController;
	private JButton logIn;
	private PhysicalAttendantSimulatorWindow attendantWindow;
	
	/**
	 * 
	 */
	@Override
	public void init(StateHandler<GUIState> stateController, ReducedState reducedState) {
		this.stateController = stateController;
		attendantWindow = new PhysicalAttendantSimulatorWindow();
		attendantWindow.createWindow();
	}

	/**
	 * 
	 */
	@Override
	public void onDataUpdate(StateData<?> data) {

	}

	/**
	 * This sets up the widgets for the block state
	 */
	@Override
	public JPanel getPanel() {

		// the main panel
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

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
		
		Dimension buttonSize = new Dimension(250, 75);
		JPanel logInPanel = new JPanel();
		logInPanel.setLayout(new BorderLayout());
		logIn = new JButton("Log In");
		logIn.setFont(new Font("Arial", Font.BOLD, 40));
		logIn.addActionListener(this);
		logIn.setSize(buttonSize);
		logIn.setPreferredSize(buttonSize);
		logIn.setMinimumSize(buttonSize);
		logIn.setMaximumSize(buttonSize);
		logInPanel.add(logIn, BorderLayout.EAST);
		

		mainPanel.add(blockPanel);
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
	public void actionPerformed(ActionEvent arg0) {
		JButton button = (JButton) arg0.getSource();
		
		if(button == logIn) {
			stateController.setState(new AttendantLogInState());
		}
		
	}

}
