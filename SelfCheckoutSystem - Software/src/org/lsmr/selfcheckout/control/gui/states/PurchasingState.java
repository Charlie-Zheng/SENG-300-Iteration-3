package org.lsmr.selfcheckout.control.gui.states;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.lsmr.selfcheckout.control.gui.StateHandler;
import org.lsmr.selfcheckout.control.gui.statedata.StateData;

public class PurchasingState implements GUIState, ActionListener {

	private StateHandler<GUIState> stateController;
	JButton goBack;
	JButton cash;
	JButton debit;
	JButton credit;
	JButton gift;
	

	public PurchasingState() {

	}

	@Override
	public void init(StateHandler<GUIState> stateController, ReducedState reducedState) {
		this.stateController = stateController;

	}

	@Override
	public void onDataUpdate(StateData<?> data) {
		// TODO Auto-generated method stub
	}

	@Override
	public JPanel getPanel() {
		
		// set up main panel
		JPanel mainPayPanel = new JPanel();
		mainPayPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
		
		// set up payment methods panel
		JPanel payPanel = new JPanel();
		Dimension size = new Dimension(1260, 400);
		payPanel.setMaximumSize(size);
		payPanel.setPreferredSize(size);
		payPanel.setSize(size);
		payPanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
		payPanel.setLayout(new GridLayout(0, 4, 20, 20));



		final Dimension buttonSize1 = new Dimension(200, 200); // for custom checkout button size

		// image for cash from below website
		// https://en.wikipedia.org/wiki/Canadian_dollar#/media/File:Canadian_Frontier_Banknotes_faces.png
		cash = new JButton();
		ImageIcon cashImg = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/Canadian_Frontier_Banknotes_faces.png");
		Image cshImg = cashImg.getImage() ;  
		Image newCshImg = cshImg.getScaledInstance( 150, 150,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon cshImgResized = new ImageIcon(newCshImg);
		cash.setSize(buttonSize1);
		cash.setPreferredSize(buttonSize1);
		cash.setMinimumSize(buttonSize1);
		cash.setMaximumSize(buttonSize1);
		cash.setLayout(new BorderLayout());
		JLabel cashImgLabel = new JLabel(cshImgResized);
		JLabel csh = new JLabel("Cash", SwingConstants.CENTER);
		csh.setFont(new Font("Arial", Font.BOLD, 30));
		cash.add(csh, BorderLayout.SOUTH);
		cash.add(cashImgLabel, BorderLayout.NORTH);
		payPanel.add(cash);

		// image for debit from website below
		// https://toppng.com/download-file/6436
		debit = new JButton();
		ImageIcon debitImg = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/interac-yellow-vector-logo.png");
		Image debImg = debitImg.getImage() ;  
		Image newDebImg = debImg.getScaledInstance( 150, 150,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon debImgResized = new ImageIcon(newDebImg);
		debit.setSize(buttonSize1);
		debit.setPreferredSize(buttonSize1);
		debit.setMinimumSize(buttonSize1);
		debit.setMaximumSize(buttonSize1);
		debit.setLayout(new BorderLayout());
		JLabel debImgLabel = new JLabel(debImgResized);
		JLabel deb = new JLabel("Debit", SwingConstants.CENTER);
		deb.setFont(new Font("Arial", Font.BOLD, 30));
		debit.add(deb, BorderLayout.SOUTH);
		debit.add(debImgLabel, BorderLayout.NORTH);
		payPanel.add(debit);

		// image for credit card below
		// https://www.seekpng.com/idown/u2q8t4r5o0r5r5q8_mastercard-clipart-credit-card-visa-and-master-card/
		credit = new JButton();
		ImageIcon creditImg = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/credit-card-logo.png");
		Image credImg = creditImg.getImage() ;  
		Image newCredImg = credImg.getScaledInstance( 150, 150,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon credImgResized = new ImageIcon(newCredImg);
		credit.setSize(buttonSize1);
		credit.setPreferredSize(buttonSize1);
		credit.setMinimumSize(buttonSize1);
		credit.setMaximumSize(buttonSize1);
		credit.setLayout(new BorderLayout());
		JLabel credImgLabel = new JLabel(credImgResized);
		JLabel cred = new JLabel("Credit", SwingConstants.CENTER);
		cred.setFont(new Font("Arial", Font.BOLD, 30));
		credit.add(cred, BorderLayout.SOUTH);
		credit.add(credImgLabel, BorderLayout.NORTH);
		payPanel.add(credit);

		//gift card
		// https://www.google.com/search?q=coop+giftcard+png&tbm=isch&ved=2ahUKEwiNnMz4-PHvAhXAFjQIHdo-AikQ2-cCegQIABAA&oq=coop+giftcard+png&gs_lcp=CgNpbWcQA1DqogtY9LALYKuzC2gAcAB4AIABdogB5QSSAQM4LjGYAQCgAQGqAQtnd3Mtd2l6LWltZ8ABAQ&sclient=img&ei=abFwYM3DCsCt0PEP2v2IyAI&bih=619&biw=1280#imgrc=lQRbSOTnh-SsTM
		gift = new JButton();
		ImageIcon giftImg = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/coop giftcard.png");
		Image gftImg = giftImg.getImage() ;  
		Image newGiftImg = gftImg.getScaledInstance( 150, 150,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon giftImgResized = new ImageIcon(newGiftImg);
		gift.setSize(buttonSize1);
		gift.setPreferredSize(buttonSize1);
		gift.setMinimumSize(buttonSize1);
		gift.setMaximumSize(buttonSize1);
		gift.setLayout(new BorderLayout());
		JLabel giftImgLabel = new JLabel(giftImgResized);
		JLabel gft = new JLabel("Gift Card", SwingConstants.CENTER);
		gft.setFont(new Font("Arial", Font.BOLD, 30));
		gift.add(gft, BorderLayout.SOUTH);
		gift.add(giftImgLabel, BorderLayout.NORTH);
		payPanel.add(gift);

		// set up statement at top of screen
		JPanel topPanel = new JPanel();
		JLabel topStatement = new JLabel("Select Payment Method");
		topStatement.setFont(new Font("Arial", Font.BOLD, 60));
		topPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 0, 30));
		topPanel.add(topStatement);


		// set up bottom panel with payment and back button
		// image of black arrow downloaded from below website
		// https://www.pikpng.com/downpngs/oxJooi_simpleicons-interface-undo-black-arrow-pointing-to-tanda/
		JPanel goBackPanel = new JPanel();
		goBackPanel.setLayout(new BorderLayout(275, 0));
		ImageIcon arrow = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/black arrow.png");
		Image img = arrow.getImage() ;  
		Image newimg = img.getScaledInstance( 50, 50,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon arrowResized = new ImageIcon(newimg);

		goBack = new JButton();
		goBack.setLayout(new BorderLayout()); //so we can add an icon
		JLabel iconLabel = new JLabel(arrowResized);
		JLabel back = new JLabel("Go Back", SwingConstants.CENTER);

		back.setFont(new Font("Arial", Font.BOLD, 30));
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

		
		JLabel total = new JLabel();
		total.setText("Total: $0.00");
		total.setFont(new Font("Arial", Font.BOLD, 40));
		goBackPanel.add(total, BorderLayout.WEST);
		

		mainPayPanel.add(topPanel);
		mainPayPanel.add(payPanel);
		mainPayPanel.add(goBackPanel);

		return mainPayPanel;
	}

	@Override
	public ReducedState reduce() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		JButton button = (JButton) arg0.getSource();
		
		if(button == goBack) {
			stateController.setState(new BuyingState());
			
		} else if(button == cash) {
			
		} else if(button == debit) { // i think we might be able to combine all the cards?
			
		} else if(button == credit) {
			
		} else if(button == gift) {
			
		}

	}

}
