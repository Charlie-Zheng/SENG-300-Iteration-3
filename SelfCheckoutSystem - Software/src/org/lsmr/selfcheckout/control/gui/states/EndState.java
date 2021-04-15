package org.lsmr.selfcheckout.control.gui.states;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.lsmr.selfcheckout.control.gui.StateHandler;
import org.lsmr.selfcheckout.control.gui.statedata.BalanceStateData;
import org.lsmr.selfcheckout.control.gui.statedata.EmitChangeStateData;
import org.lsmr.selfcheckout.control.gui.statedata.PurchaseCompleteData;
import org.lsmr.selfcheckout.control.gui.statedata.StateData;

public class EndState implements GUIState {

	private StateHandler<GUIState> stateController;
	private JLabel duePrintOut;
	private JLabel paidPrintOut;
	Timer timer = new Timer();
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
			float balance = - (float) data.obtain();
			if (balance > 0) {
				duePrintOut.setText(String.format("Change Due: $%.2f", balance));
			} else {
//				// wait then swap back
//				new Thread(new Runnable() {
//					@Override
//					public void run() {
//						try {
//							Thread.sleep(2000);
//						} catch (InterruptedException e) {
//						}
//						stateController.setState(new StartState());
//					}
//				}).start();
			}
		} else if (data instanceof PurchaseCompleteData) {
			TimerTask switchToStart = new TimerTask() {

				@Override
				public void run() {
					stateController.setState(new StartState());
					this.cancel();
				}
				
			};
	
			timer.scheduleAtFixedRate(switchToStart, 3000, 1000);
		
		}
	}

	/**
	 * This sets up all of the widgets for the screen
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
		JLabel topStatement = new JLabel("Take Receipt");
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
		// image of guy carrying groceries downloaded from website below
		//https://www.clipartmax.com/middle/m2i8d3m2K9m2G6b1_vector-illustration-of-supermarket-grocery-store-shopper-carry-bags-clipart/		
		JPanel wordPanel = new JPanel();
		wordPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 10, 50));
		JLabel words = new JLabel("Remove bags and take your receipt.");
		words.setFont(new Font("Arial", Font.BOLD, 40));
		wordPanel.add(words);// panel with statement to input item's description

		// panel for take items image
		JPanel removePanel = new JPanel();
		ImageIcon remImg = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/guy carrying.png");
		Image reMImg = remImg.getImage() ;  
		Image newRemImg = reMImg.getScaledInstance( 350, 250,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon remImgResized = new ImageIcon(newRemImg);
		JLabel removeLogo = new JLabel(remImgResized);
		removePanel.add(removeLogo);


		// thank you panel
		JLabel thanks = new JLabel();
		thanks.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		thanks.setText("Thank You For Shopping At Co-op!");
		thanks.setFont(new Font("Arial", Font.BOLD, 55));
		JPanel thanksPanel = new JPanel();
		thanksPanel.add(thanks);

		// change due panel
		duePrintOut = new JLabel();
		duePrintOut.setText("Change Due: $0.00");
		duePrintOut.setFont(new Font("Arial", Font.BOLD, 40));
		JPanel duePanel = new JPanel();
		duePanel.add(duePrintOut);

		// amount paid panel
		paidPrintOut = new JLabel();
		paidPrintOut.setText("Amount Paid: $0.00");
		paidPrintOut.setFont(new Font("Arial", Font.BOLD, 30));
		JPanel paidPanel = new JPanel();
		//paidPanel.add(paidPrintOut);


		mainPanel.add(topPanel);
		mainPanel.add(wordPanel);
		mainPanel.add(removePanel);
		mainPanel.add(thanksPanel);
		mainPanel.add(duePanel);
	//	mainPanel.add(paidPanel);

		// request balance
		stateController.notifyListeners(new BalanceStateData());
		
		//request change to be emitted
		stateController.notifyListeners(new EmitChangeStateData());
		return mainPanel;
	}

	@Override
	public ReducedState reduce() {
		// TODO Auto-generated method stub
		return null;
	}

}
