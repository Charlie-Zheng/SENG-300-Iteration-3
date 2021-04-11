package org.lsmr.selfcheckout.control.gui.states;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.lsmr.selfcheckout.control.gui.StateHandler;
import org.lsmr.selfcheckout.control.gui.statedata.InsertPLUProductData;
import org.lsmr.selfcheckout.control.gui.statedata.ScaleStateData;
import org.lsmr.selfcheckout.control.gui.statedata.StateData;
import org.lsmr.selfcheckout.products.PLUCodedProduct;

public class ScaleState implements GUIState {

	private StateHandler<GUIState> stateController;
	private PLUCodedProduct product;

	@Override
	public void init(StateHandler<GUIState> stateController, ReducedState reducedState) {
		this.stateController = stateController;

		product = (PLUCodedProduct) reducedState.getData();
	}

	@Override
	public void onDataUpdate(StateData<?> data) {
		if (data instanceof ScaleStateData) {
			// scan product then go back to buying
			stateController.notifyListeners(new InsertPLUProductData(product));
			stateController.setState(new BuyingState());
		}
	}

	@Override
	public JPanel getPanel() {
		//*************** somehow has to listen for weight on scale to change so we can go
		//back to the buying state screen **********
		
		// the main panel
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 100, 50));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		// set up statement at top of screen
		// image for coop logo downloaded from website below
		// https://www.google.com/search?q=coop+png&tbm=isch&ved=2ahUKEwjC9bTd-PHvAhU8AzQIHSiLAkkQ2-cCegQIABAA&oq=coop+png&gs_lcp=CgNpbWcQAzIECAAQQzICCAAyBggAEAcQHjIGCAAQBxAeMgYIABAHEB4yBggAEAcQHjIGCAAQBxAeMgYIABAHEB4yBggAEAcQHjIGCAAQBxAeUM-sA1jPrANg67IDaABwAHgAgAFDiAFDkgEBMZgBAKABAaoBC2d3cy13aXotaW1nwAEB&sclient=img&ei=MLFwYMKdCryG0PEPqJaKyAQ&bih=619&biw=1280#imgrc=EmUG01nblMHYrM
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout(250, 0));
		JLabel topStatement = new JLabel("Weigh Item");
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
		// vegetables on scale image downloaded from website below
		// https://imgbin.com/download/qUuzRrxf
		JPanel wordPanel = new JPanel();
		wordPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 10, 50));
		JLabel words = new JLabel("Please place item on the scale.");
		words.setFont(new Font("Arial", Font.BOLD, 40));
		wordPanel.add(words);// panel with statement to input item's description

		// panel for put item on scale image
		JPanel scalePanel = new JPanel();
		ImageIcon scaleImg = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/veggies on scale.png");
		Image scaLeImg = scaleImg.getImage() ;  
		Image newScaleImg = scaLeImg.getScaledInstance( 450, 350,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon scaleImgResized = new ImageIcon(newScaleImg);
		JLabel scaleLogo = new JLabel(scaleImgResized);
		scalePanel.add(scaleLogo);


		mainPanel.add(topPanel);
		mainPanel.add(wordPanel);
		mainPanel.add(scalePanel);

		return mainPanel;
	}

	@Override
	public ReducedState reduce() {
		// TODO Auto-generated method stub
		return null;
	}

}
