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
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.lsmr.selfcheckout.control.gui.StateHandler;
import org.lsmr.selfcheckout.control.gui.statedata.StateData;

public class AttendantOptionsState implements GUIState, ActionListener {

	private StateHandler<GUIState> stateController;

	// attendant refills banknote dispenser
	private JButton refill5;
	private JButton refill10;
	private JButton refill20;
	private JButton refill50;
	private JButton refill100;

	//attendant empties banknote dispenser
	private JButton empty5;
	private JButton empty10;
	private JButton empty20;
	private JButton empty50;
	private JButton empty100;

	// attendant refills coin dispenser
	private JButton refillNickel;
	private JButton refillDime;
	private JButton refillQuarter;
	private JButton refillLoonie;
	private JButton refillTwoonie;

	// attendant empties coin dispenser
	private JButton emptyNickel;
	private JButton emptyDime;
	private JButton emptyQuarter;
	private JButton emptyLoonie;
	private JButton emptyTwoonie;

	// attendant refills receipt printer	
	private JButton refillInk;
	private JButton refillPaper;

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
		// the main panel
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 10, 30));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBackground(Color.RED.darker());


		// set up statement at top of screen
		JPanel topPanel = new JPanel();
		//topPanel.setLayout(new BorderLayout());
		topPanel.setBackground(Color.RED.darker());
		JLabel topStatement = new JLabel("Choose an Option");
		topStatement.setForeground(Color.WHITE);
		topStatement.setFont(new Font("Arial", Font.BOLD, 60));
		topPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 10, 30));
		topPanel.add(topStatement);

		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new GridLayout(2, 3, 20, 0));
		middlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		middlePanel.setBackground(Color.RED.darker());

		// panel for put item on scale image
		JPanel imagePanel = new JPanel();
		imagePanel.setBackground(Color.RED.darker());
		imagePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		ImageIcon clerkImg = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/grocery clerk.png");
		Image clerKImg = clerkImg.getImage() ;  
		Image newClerkImg = clerKImg.getScaledInstance( 250, 250,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon clerkImgResized = new ImageIcon(newClerkImg);
		JLabel clerkLogo = new JLabel(clerkImgResized);
		imagePanel.add(clerkLogo);


		Dimension buttonSize = new Dimension(250, 34);

		JPanel banknoteRefillPanel = new JPanel();
		banknoteRefillPanel.setBackground(Color.RED.darker());
		banknoteRefillPanel.setLayout(new BoxLayout(banknoteRefillPanel, BoxLayout.Y_AXIS));
		banknoteRefillPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
		JLabel banknoteRefillLabel = new JLabel("Refill Banknote Storage Unit");
		banknoteRefillLabel.setForeground(Color.WHITE);
		banknoteRefillLabel.setFont(new Font("Arial", Font.BOLD, 26));
		JPanel bankNoteRefillPanel = new JPanel();
		bankNoteRefillPanel.setBackground(Color.RED.darker());
		bankNoteRefillPanel.add(banknoteRefillLabel);
		banknoteRefillPanel.add(bankNoteRefillPanel);

		// image for all banknotes downloaded from website below - cropped the image
		// used for both refilling and emptying the banknote dispenser
		// https://en.wikipedia.org/wiki/Canadian_dollar#/media/File:Canadian_Frontier_Banknotes_faces.png		
		ImageIcon five = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/5 bill.png");
		Image fiveImg = five.getImage() ;  
		Image newFiveImg = fiveImg.getScaledInstance( 50, 25,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon fiveImgResized = new ImageIcon(newFiveImg);

		refill5 = new JButton();
		refill5.setLayout(new BorderLayout());
		JLabel refillFiveIcon = new JLabel(fiveImgResized);
		JLabel refillFiveLabel = new JLabel("Refill $5.00s", SwingConstants.CENTER);
		refillFiveLabel.setFont(new Font("Arial", Font.BOLD, 22));
		refill5.add(refillFiveLabel, BorderLayout.CENTER);
		refill5.add(refillFiveIcon, BorderLayout.WEST);
		refill5.setSize(buttonSize);
		refill5.setPreferredSize(buttonSize);
		refill5.setMinimumSize(buttonSize);
		refill5.setMaximumSize(buttonSize);
		refill5.addActionListener(this);
		JPanel refill5Panel = new JPanel();
		refill5Panel.setBackground(Color.RED.darker());
		refill5Panel.add(refill5);
		banknoteRefillPanel.add(refill5Panel);


		ImageIcon ten = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/10 bill.png");
		Image tenImg = ten.getImage() ;  
		Image newTenImg = tenImg.getScaledInstance( 50, 25,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon tenImgResized = new ImageIcon(newTenImg);

		refill10 = new JButton();
		refill10.setLayout(new BorderLayout());
		JLabel refillTenIcon = new JLabel(tenImgResized);
		JLabel refillTenLabel = new JLabel("Refill $10.00s", SwingConstants.CENTER);
		refillTenLabel.setFont(new Font("Arial", Font.BOLD, 22));
		refill10.add(refillTenLabel, BorderLayout.CENTER);
		refill10.add(refillTenIcon, BorderLayout.WEST);
		refill10.setSize(buttonSize);
		refill10.setPreferredSize(buttonSize);
		refill10.setMinimumSize(buttonSize);
		refill10.setMaximumSize(buttonSize);
		refill10.addActionListener(this);
		JPanel refill10Panel = new JPanel();
		refill10Panel.setBackground(Color.RED.darker());
		refill10Panel.add(refill10);
		banknoteRefillPanel.add(refill10Panel);

		ImageIcon twenty = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/20 bill.png");
		Image twentyImg = twenty.getImage() ;  
		Image newTwentyImg = twentyImg.getScaledInstance( 50, 25,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon twentyImgResized = new ImageIcon(newTwentyImg);

		refill20 = new JButton();
		refill20.setLayout(new BorderLayout());
		JLabel refillTwentyIcon = new JLabel(twentyImgResized);
		JLabel refillTwentyLabel = new JLabel("Refill $20.00s", SwingConstants.CENTER);
		refillTwentyLabel.setFont(new Font("Arial", Font.BOLD, 22));
		refill20.add(refillTwentyLabel, BorderLayout.CENTER);
		refill20.add(refillTwentyIcon, BorderLayout.WEST);
		refill20.setSize(buttonSize);
		refill20.setPreferredSize(buttonSize);
		refill20.setMinimumSize(buttonSize);
		refill20.setMaximumSize(buttonSize);
		refill20.addActionListener(this);
		JPanel refill20Panel = new JPanel();
		refill20Panel.setBackground(Color.RED.darker());
		refill20Panel.add(refill20);
		banknoteRefillPanel.add(refill20Panel);


		ImageIcon fifty = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/50 bill.png");
		Image fiftyImg = fifty.getImage() ;  
		Image newFiftyImg = fiftyImg.getScaledInstance( 50, 25,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon fiftyImgResized = new ImageIcon(newFiftyImg);

		refill50 = new JButton();
		refill50.setLayout(new BorderLayout());
		JLabel refillFiftyIcon = new JLabel(fiftyImgResized);
		JLabel refillFiftyLabel = new JLabel("Refill $50.00s", SwingConstants.CENTER);
		refillFiftyLabel.setFont(new Font("Arial", Font.BOLD, 22));
		refill50.add(refillFiftyLabel, BorderLayout.CENTER);
		refill50.add(refillFiftyIcon, BorderLayout.WEST);
		refill50.setSize(buttonSize);
		refill50.setPreferredSize(buttonSize);
		refill50.setMinimumSize(buttonSize);
		refill50.setMaximumSize(buttonSize);
		refill50.addActionListener(this);
		JPanel refill50Panel = new JPanel();
		refill50Panel.setBackground(Color.RED.darker());
		refill50Panel.add(refill50);
		banknoteRefillPanel.add(refill50Panel);

		ImageIcon hundred = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/100 bill.png");
		Image hundredImg = hundred.getImage() ;  
		Image newHundredImg = hundredImg.getScaledInstance( 50, 25,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon hundredImgResized = new ImageIcon(newHundredImg);

		refill100 = new JButton();
		refill100.setLayout(new BorderLayout());
		JLabel refillHundredIcon = new JLabel(hundredImgResized);
		JLabel refillHundredLabel = new JLabel("Refill $100.00s", SwingConstants.CENTER);
		refillHundredLabel.setFont(new Font("Arial", Font.BOLD, 22));
		refill100.add(refillHundredLabel, BorderLayout.CENTER);
		refill100.add(refillHundredIcon, BorderLayout.WEST);
		refill100.setSize(buttonSize);
		refill100.setPreferredSize(buttonSize);
		refill100.setMinimumSize(buttonSize);
		refill100.setMaximumSize(buttonSize);
		refill100.addActionListener(this);
		JPanel refill100Panel = new JPanel();
		refill100Panel.setBackground(Color.RED.darker());
		refill100Panel.add(refill100);
		banknoteRefillPanel.add(refill100Panel);

		JPanel banknoteEmptyPanel = new JPanel();
		banknoteEmptyPanel.setBackground(Color.RED.darker());
		banknoteEmptyPanel.setLayout(new BoxLayout(banknoteEmptyPanel, BoxLayout.Y_AXIS));
		banknoteEmptyPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
		JLabel banknoteEmptyLabel = new JLabel("Empty Banknote Storage Unit");
		banknoteEmptyLabel.setForeground(Color.WHITE);
		banknoteEmptyLabel.setFont(new Font("Arial", Font.BOLD, 26));
		JPanel bankNoteEmptyPanel = new JPanel();
		bankNoteEmptyPanel.setBackground(Color.RED.darker());
		bankNoteEmptyPanel.add(banknoteEmptyLabel);
		banknoteEmptyPanel.add(bankNoteEmptyPanel);

		empty5 = new JButton();
		empty5.setLayout(new BorderLayout());
		JLabel emptyFiveIcon = new JLabel(fiveImgResized);
		JLabel emptyFiveLabel = new JLabel("Empty $5.00s", SwingConstants.CENTER);
		emptyFiveLabel.setFont(new Font("Arial", Font.BOLD, 22));
		empty5.add(emptyFiveLabel, BorderLayout.CENTER);
		empty5.add(emptyFiveIcon, BorderLayout.WEST);
		empty5.setSize(buttonSize);
		empty5.setPreferredSize(buttonSize);
		empty5.setMinimumSize(buttonSize);
		empty5.setMaximumSize(buttonSize);
		empty5.addActionListener(this);
		JPanel empty5Panel = new JPanel();
		empty5Panel.setBackground(Color.RED.darker());
		empty5Panel.add(empty5);
		banknoteEmptyPanel.add(empty5Panel);

		empty10 = new JButton();
		empty10.setLayout(new BorderLayout());
		JLabel emptyTenIcon = new JLabel(tenImgResized);
		JLabel emptyTenLabel = new JLabel("Empty $10.00s", SwingConstants.CENTER);
		emptyTenLabel.setFont(new Font("Arial", Font.BOLD, 22));
		empty10.add(emptyTenLabel, BorderLayout.CENTER);
		empty10.add(emptyTenIcon, BorderLayout.WEST);
		empty10.setSize(buttonSize);
		empty10.setPreferredSize(buttonSize);
		empty10.setMinimumSize(buttonSize);
		empty10.setMaximumSize(buttonSize);
		empty10.addActionListener(this);
		JPanel empty10Panel = new JPanel();
		empty10Panel.setBackground(Color.RED.darker());
		empty10Panel.add(empty10);
		banknoteEmptyPanel.add(empty10Panel);

		empty20 = new JButton();
		empty20.setLayout(new BorderLayout());
		JLabel emptyTwentyIcon = new JLabel(twentyImgResized);
		JLabel emptyTwentyLabel = new JLabel("Empty $20.00s", SwingConstants.CENTER);
		emptyTwentyLabel.setFont(new Font("Arial", Font.BOLD, 22));
		empty20.add(emptyTwentyLabel, BorderLayout.CENTER);
		empty20.add(emptyTwentyIcon, BorderLayout.WEST);
		empty20.setSize(buttonSize);
		empty20.setPreferredSize(buttonSize);
		empty20.setMinimumSize(buttonSize);
		empty20.setMaximumSize(buttonSize);
		empty20.addActionListener(this);
		JPanel empty20Panel = new JPanel();
		empty20Panel.setBackground(Color.RED.darker());
		empty20Panel.add(empty20);
		banknoteEmptyPanel.add(empty20Panel);

		empty50 = new JButton();
		empty50.setLayout(new BorderLayout());
		JLabel emptyFiftyIcon = new JLabel(fiftyImgResized);
		JLabel emptyFiftyLabel = new JLabel("Empty $50.00s", SwingConstants.CENTER);
		emptyFiftyLabel.setFont(new Font("Arial", Font.BOLD, 22));
		empty50.add(emptyFiftyLabel, BorderLayout.CENTER);
		empty50.add(emptyFiftyIcon, BorderLayout.WEST);
		empty50.setSize(buttonSize);
		empty50.setPreferredSize(buttonSize);
		empty50.setMinimumSize(buttonSize);
		empty50.setMaximumSize(buttonSize);
		empty50.addActionListener(this);
		JPanel empty50Panel = new JPanel();
		empty50Panel.setBackground(Color.RED.darker());
		empty50Panel.add(empty50);
		banknoteEmptyPanel.add(empty50Panel);

		empty100 = new JButton();
		empty100.setLayout(new BorderLayout());
		JLabel emptyHundredIcon = new JLabel(hundredImgResized);
		JLabel emptyHundredLabel = new JLabel("Empty $100.00s", SwingConstants.CENTER);
		emptyHundredLabel.setFont(new Font("Arial", Font.BOLD, 22));
		empty100.add(emptyHundredLabel, BorderLayout.CENTER);
		empty100.add(emptyHundredIcon, BorderLayout.WEST);
		empty100.setSize(buttonSize);
		empty100.setPreferredSize(buttonSize);
		empty100.setMinimumSize(buttonSize);
		empty100.setMaximumSize(buttonSize);
		empty100.addActionListener(this);
		JPanel empty100Panel = new JPanel();
		empty100Panel.setBackground(Color.RED.darker());
		empty100Panel.add(empty100);
		banknoteEmptyPanel.add(empty100Panel);


		JPanel coinRefillPanel = new JPanel();
		coinRefillPanel.setBackground(Color.RED.darker());
		coinRefillPanel.setLayout(new BoxLayout(coinRefillPanel, BoxLayout.Y_AXIS));
		coinRefillPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
		JLabel coinRefillLabel = new JLabel("Refill Coin Storage Unit");
		coinRefillLabel.setFont(new Font("Arial", Font.BOLD, 26));
		coinRefillLabel.setForeground(Color.WHITE);
		JPanel coiNRefillPanel = new JPanel();
		coiNRefillPanel.setBackground(Color.RED.darker());
		coiNRefillPanel.add(coinRefillLabel);
		coinRefillPanel.add(coiNRefillPanel);

		// image for nickel downloaded from website below
		// used for both refilling and emptying the nickel storage unit
		// https://www.cleanpng.com/png-canada-nickel-coin-dime-loonie-6720907/download-png.html
		ImageIcon nickel = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/nickel.png");
		Image nickelImg = nickel.getImage() ;  
		Image newNickelImg = nickelImg.getScaledInstance( 25, 25,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon nickelImgResized = new ImageIcon(newNickelImg);

		refillNickel = new JButton();
		refillNickel.setLayout(new BorderLayout());
		JLabel refillNickelIcon = new JLabel(nickelImgResized);
		JLabel refillNickelLabel = new JLabel("Refill $0.05s", SwingConstants.CENTER);
		refillNickelLabel.setFont(new Font("Arial", Font.BOLD, 22));
		refillNickel.add(refillNickelLabel, BorderLayout.CENTER);
		refillNickel.add(refillNickelIcon, BorderLayout.WEST);
		refillNickel.setSize(buttonSize);
		refillNickel.setPreferredSize(buttonSize);
		refillNickel.setMinimumSize(buttonSize);
		refillNickel.setMaximumSize(buttonSize);
		refillNickel.addActionListener(this);
		JPanel refillNickelPanel = new JPanel();
		refillNickelPanel.setBackground(Color.RED.darker());
		refillNickelPanel.add(refillNickel);
		coinRefillPanel.add(refillNickelPanel);

		// image for dime downloaded from website below
		// used for both refilling and emptying dime storage unit
		// https://www.cleanpng.com/png-150th-anniversary-of-canada-coin-set-royal-canadia-1655179/download-png.html
		ImageIcon dime = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/dime.png");
		Image dimeImg = dime.getImage() ;  
		Image newDimeImg = dimeImg.getScaledInstance( 25, 25,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon dimeImgResized = new ImageIcon(newDimeImg);

		refillDime = new JButton();
		refillDime.setLayout(new BorderLayout());
		JLabel refillDimeIcon = new JLabel(dimeImgResized);
		JLabel refillDimeLabel = new JLabel("Refill $0.10s", SwingConstants.CENTER);
		refillDimeLabel.setFont(new Font("Arial", Font.BOLD, 22));
		refillDime.add(refillDimeLabel, BorderLayout.CENTER);
		refillDime.add(refillDimeIcon, BorderLayout.WEST);
		refillDime.setSize(buttonSize);
		refillDime.setPreferredSize(buttonSize);
		refillDime.setMinimumSize(buttonSize);
		refillDime.setMaximumSize(buttonSize);
		refillDime.addActionListener(this);
		JPanel refillDimePanel = new JPanel();
		refillDimePanel.setBackground(Color.RED.darker());
		refillDimePanel.add(refillDime);
		coinRefillPanel.add(refillDimePanel);

		// image of quarter downloaded from website below
		// used for both refilling and emptying quarter storage unit
		// https://www.cleanpng.com/png-150th-anniversary-of-canada-canadian-coins-quarter-2268512/download-png.html
		ImageIcon quarter = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/quarter.png");
		Image quarterImg = quarter.getImage() ;  
		Image newQuarterImg = quarterImg.getScaledInstance( 25, 25,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon quarterImgResized = new ImageIcon(newQuarterImg);

		refillQuarter = new JButton();
		refillQuarter.setLayout(new BorderLayout());
		JLabel refillQuarterIcon = new JLabel(quarterImgResized);
		JLabel refillQuarterLabel = new JLabel("Refill $0.25s", SwingConstants.CENTER);
		refillQuarterLabel.setFont(new Font("Arial", Font.BOLD, 22));
		refillQuarter.add(refillQuarterLabel, BorderLayout.CENTER);
		refillQuarter.add(refillQuarterIcon, BorderLayout.WEST);
		refillQuarter.setSize(buttonSize);
		refillQuarter.setPreferredSize(buttonSize);
		refillQuarter.setMinimumSize(buttonSize);
		refillQuarter.setMaximumSize(buttonSize);
		refillQuarter.addActionListener(this);
		JPanel refillQuarterPanel = new JPanel();
		refillQuarterPanel.setBackground(Color.RED.darker());
		refillQuarterPanel.add(refillQuarter);
		coinRefillPanel.add(refillQuarterPanel);

		// image of loonie downloaded from website below
		// used for both refilling and emptying loonie storage unit
		// https://www.cleanpng.com/png-dollar-coin-canada-loonie-royal-canadian-mint-unci-3389442/download-png.html
		ImageIcon loonie = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/loonie.png");
		Image loonieImg = loonie.getImage() ;  
		Image newLoonieImg = loonieImg.getScaledInstance( 25, 25,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon loonieImgResized = new ImageIcon(newLoonieImg);

		refillLoonie = new JButton();
		refillLoonie.setLayout(new BorderLayout());
		JLabel refillLoonieIcon = new JLabel(loonieImgResized);
		JLabel refillLoonieLabel = new JLabel("Refill $1.00s", SwingConstants.CENTER);
		refillLoonieLabel.setFont(new Font("Arial", Font.BOLD, 22));
		refillLoonie.add(refillLoonieLabel, BorderLayout.CENTER);
		refillLoonie.add(refillLoonieIcon, BorderLayout.WEST);
		refillLoonie.setSize(buttonSize);
		refillLoonie.setPreferredSize(buttonSize);
		refillLoonie.setMinimumSize(buttonSize);
		refillLoonie.setMaximumSize(buttonSize);
		refillLoonie.addActionListener(this);
		JPanel refillLooniePanel = new JPanel();
		refillLooniePanel.setBackground(Color.RED.darker());
		refillLooniePanel.add(refillLoonie);
		coinRefillPanel.add(refillLooniePanel);

		// image of twoonie downloaded from website below
		// used for both refilling and emptying twoonie storage unit
		// https://www.cleanpng.com/png-canada-toonie-loonie-canadian-dollar-royal-canadia-1109303/download-png.html
		ImageIcon twoonie = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/twoonie.png");
		Image twoonieImg = twoonie.getImage() ;  
		Image newTwoonieImg = twoonieImg.getScaledInstance( 25, 25,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon twoonieImgResized = new ImageIcon(newTwoonieImg);

		refillTwoonie = new JButton();
		refillTwoonie.setLayout(new BorderLayout());
		JLabel refillTwoonieIcon = new JLabel(twoonieImgResized);
		JLabel refillTwoonieLabel = new JLabel("Refill $2.00s", SwingConstants.CENTER);
		refillTwoonieLabel.setFont(new Font("Arial", Font.BOLD, 22));
		refillTwoonie.add(refillTwoonieLabel, BorderLayout.CENTER);
		refillTwoonie.add(refillTwoonieIcon, BorderLayout.WEST);
		refillTwoonie.setSize(buttonSize);
		refillTwoonie.setPreferredSize(buttonSize);
		refillTwoonie.setMinimumSize(buttonSize);
		refillTwoonie.setMaximumSize(buttonSize);
		refillTwoonie.addActionListener(this);
		JPanel refillTwooniePanel = new JPanel();
		refillTwooniePanel.setBackground(Color.RED.darker());
		refillTwooniePanel.add(refillTwoonie);
		coinRefillPanel.add(refillTwooniePanel);

		JPanel coinEmptyPanel = new JPanel();
		coinEmptyPanel.setBackground(Color.RED.darker());
		coinEmptyPanel.setLayout(new BoxLayout(coinEmptyPanel, BoxLayout.Y_AXIS));
		coinEmptyPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
		JLabel coinEmptyLabel = new JLabel("Empty Coin Storage Unit");
		coinEmptyLabel.setFont(new Font("Arial", Font.BOLD, 26));
		coinEmptyLabel.setForeground(Color.WHITE);
		JPanel coiNEmptyPanel = new JPanel();
		coiNEmptyPanel.setBackground(Color.RED.darker());
		coiNEmptyPanel.add(coinEmptyLabel);
		coinEmptyPanel.add(coiNEmptyPanel);

		emptyNickel = new JButton();
		emptyNickel.setLayout(new BorderLayout());
		JLabel emptyNickelIcon = new JLabel(nickelImgResized);
		JLabel emptyNickelLabel = new JLabel("Empty $0.05s", SwingConstants.CENTER);
		emptyNickelLabel.setFont(new Font("Arial", Font.BOLD, 22));
		emptyNickel.add(emptyNickelLabel, BorderLayout.CENTER);
		emptyNickel.add(emptyNickelIcon, BorderLayout.WEST);
		emptyNickel.setSize(buttonSize);
		emptyNickel.setPreferredSize(buttonSize);
		emptyNickel.setMinimumSize(buttonSize);
		emptyNickel.setMaximumSize(buttonSize);
		emptyNickel.addActionListener(this);
		JPanel emptyNickelPanel = new JPanel();
		emptyNickelPanel.setBackground(Color.RED.darker());
		emptyNickelPanel.add(emptyNickel);
		coinEmptyPanel.add(emptyNickelPanel);

		emptyDime = new JButton();
		emptyDime.setLayout(new BorderLayout());
		JLabel emptyDimeIcon = new JLabel(dimeImgResized);
		JLabel emptyDimeLabel = new JLabel("Empty $0.10s", SwingConstants.CENTER);
		emptyDimeLabel.setFont(new Font("Arial", Font.BOLD, 22));
		emptyDime.add(emptyDimeLabel, BorderLayout.CENTER);
		emptyDime.add(emptyDimeIcon, BorderLayout.WEST);
		emptyDime.setSize(buttonSize);
		emptyDime.setPreferredSize(buttonSize);
		emptyDime.setMinimumSize(buttonSize);
		emptyDime.setMaximumSize(buttonSize);
		emptyDime.addActionListener(this);
		JPanel emptyDimePanel = new JPanel();
		emptyDimePanel.setBackground(Color.RED.darker());
		emptyDimePanel.add(emptyDime);
		coinEmptyPanel.add(emptyDimePanel);

		emptyQuarter = new JButton();
		emptyQuarter.setLayout(new BorderLayout());
		JLabel emptyQuarterIcon = new JLabel(quarterImgResized);
		JLabel emptyQuarterLabel = new JLabel("Empty $0.25s", SwingConstants.CENTER);
		emptyQuarterLabel.setFont(new Font("Arial", Font.BOLD, 22));
		emptyQuarter.add(emptyQuarterLabel, BorderLayout.CENTER);
		emptyQuarter.add(emptyQuarterIcon, BorderLayout.WEST);
		emptyQuarter.setSize(buttonSize);
		emptyQuarter.setPreferredSize(buttonSize);
		emptyQuarter.setMinimumSize(buttonSize);
		emptyQuarter.setMaximumSize(buttonSize);
		emptyQuarter.addActionListener(this);
		JPanel emptyQuarterPanel = new JPanel();
		emptyQuarterPanel.setBackground(Color.RED.darker());
		emptyQuarterPanel.add(emptyQuarter);
		coinEmptyPanel.add(emptyQuarterPanel);

		emptyLoonie = new JButton();
		emptyLoonie.setLayout(new BorderLayout());
		JLabel emptyLoonieIcon = new JLabel(loonieImgResized);
		JLabel emptyLoonieLabel = new JLabel("Empty $1.00s", SwingConstants.CENTER);
		emptyLoonieLabel.setFont(new Font("Arial", Font.BOLD, 22));
		emptyLoonie.add(emptyLoonieLabel, BorderLayout.CENTER);
		emptyLoonie.add(emptyLoonieIcon, BorderLayout.WEST);
		emptyLoonie.setSize(buttonSize);
		emptyLoonie.setPreferredSize(buttonSize);
		emptyLoonie.setMinimumSize(buttonSize);
		emptyLoonie.setMaximumSize(buttonSize);
		emptyLoonie.addActionListener(this);
		JPanel emptyLooniePanel = new JPanel();
		emptyLooniePanel.setBackground(Color.RED.darker());
		emptyLooniePanel.add(emptyLoonie);
		coinEmptyPanel.add(emptyLooniePanel);

		emptyTwoonie = new JButton();
		emptyTwoonie.setLayout(new BorderLayout());
		JLabel emptyTwoonieIcon = new JLabel(twoonieImgResized);
		JLabel emptyTwoonieLabel = new JLabel("Empty $2.00s", SwingConstants.CENTER);
		emptyTwoonieLabel.setFont(new Font("Arial", Font.BOLD, 22));
		emptyTwoonie.add(emptyTwoonieLabel, BorderLayout.CENTER);
		emptyTwoonie.add(emptyTwoonieIcon, BorderLayout.WEST);
		emptyTwoonie.setSize(buttonSize);
		emptyTwoonie.setPreferredSize(buttonSize);
		emptyTwoonie.setMinimumSize(buttonSize);
		emptyTwoonie.setMaximumSize(buttonSize);
		emptyTwoonie.addActionListener(this);
		JPanel emptyTwooniePanel = new JPanel();
		emptyTwooniePanel.setBackground(Color.RED.darker());
		emptyTwooniePanel.add(emptyTwoonie);
		coinEmptyPanel.add(emptyTwooniePanel);


		JPanel refillPrinterPanel = new JPanel();
		refillPrinterPanel.setBackground(Color.RED.darker());
		refillPrinterPanel.setLayout(new BoxLayout(refillPrinterPanel, BoxLayout.Y_AXIS));
		refillPrinterPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 175, 0));
		JLabel refillPrinterLabel = new JLabel("Refill Printer");
		refillPrinterLabel.setForeground(Color.WHITE);
		refillPrinterLabel.setFont(new Font("Arial", Font.BOLD, 26));
		JPanel refillPrinteRPanel = new JPanel();
		refillPrinteRPanel.setBackground(Color.RED.darker());
		refillPrinteRPanel.add(refillPrinterLabel);
		refillPrinterPanel.add(refillPrinteRPanel);

		// image for reciept paper downloaded from website below
		// https://www.cleanpng.com/png-receipt-invoice-computer-icons-cash-register-1154206/download-png.html
		ImageIcon paper = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/receipt.png");
		Image paperImg = paper.getImage() ;  
		Image newPaperImg = paperImg.getScaledInstance( 25, 25,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon paperImgResized = new ImageIcon(newPaperImg);

		refillPaper = new JButton();
		refillPaper.setLayout(new BorderLayout());
		JLabel refillPaperIcon = new JLabel(paperImgResized);
		JLabel refillPaperLabel = new JLabel("Refill Printer Paper", SwingConstants.CENTER);
		refillPaperLabel.setFont(new Font("Arial", Font.BOLD, 20));
		refillPaper.add(refillPaperLabel, BorderLayout.CENTER);
		refillPaper.add(refillPaperIcon, BorderLayout.WEST);
		refillPaper.setSize(buttonSize);
		refillPaper.setPreferredSize(buttonSize);
		refillPaper.setMinimumSize(buttonSize);
		refillPaper.setMaximumSize(buttonSize);
		refillPaper.addActionListener(this);
		JPanel refillPaperPanel = new JPanel();
		refillPaperPanel.setBackground(Color.RED.darker());
		refillPaperPanel.add(refillPaper);
		refillPrinterPanel.add(refillPaperPanel);

		// image for ink bottle downloaded from website below
		// https://www.seekpng.com/idown/u2e6e6u2q8r5i1o0_ink-bottle-ink-bottle-ink-clip-art/
		ImageIcon ink = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/ink bottle.png");
		Image inkImg = ink.getImage() ;  
		Image newInkImg = inkImg.getScaledInstance( 18, 25,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon inkImgResized = new ImageIcon(newInkImg);

		refillInk = new JButton();
		refillInk.setLayout(new BorderLayout());
		JLabel refillInkIcon = new JLabel(inkImgResized);
		JLabel refillInkLabel = new JLabel("Refill Printer Ink", SwingConstants.CENTER);
		refillInkLabel.setFont(new Font("Arial", Font.BOLD, 22));
		refillInk.add(refillInkLabel, BorderLayout.CENTER);
		refillInk.add(refillInkIcon, BorderLayout.WEST);
		refillInk.setSize(buttonSize);
		refillInk.setPreferredSize(buttonSize);
		refillInk.setMinimumSize(buttonSize);
		refillInk.setMaximumSize(buttonSize);
		refillInk.addActionListener(this);
		JPanel refillInkPanel = new JPanel();
		refillInkPanel.setBackground(Color.RED.darker());
		refillInkPanel.add(refillInk);
		refillPrinterPanel.add(refillInkPanel);

		middlePanel.add(imagePanel);
		middlePanel.add(banknoteEmptyPanel);
		middlePanel.add(coinEmptyPanel);
		middlePanel.add(refillPrinterPanel);
		middlePanel.add(banknoteRefillPanel);
		middlePanel.add(coinRefillPanel);


		mainPanel.add(topPanel);
		mainPanel.add(middlePanel);


		mainPanel.add(topPanel);
		mainPanel.add(middlePanel);

		return mainPanel;
	}

	@Override
	public ReducedState reduce() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		JButton button = (JButton) arg0.getSource();

		// for refilling banknote storage
		if(button == refill5) {

		} else if(button == refill10) {

		} else if(button == refill20) {

		} else if(button == refill50) {

		} else if(button == refill100) {

		}

		// for emptying banknote storage unit
		else if(button == empty5) {

		} else if(button == empty10) {

		} else if(button == empty20) {

		} else if(button == empty50) {

		} else if(button == empty100) {

		}

		// for refilling coin storage unit
		else if(button == refillNickel) {

		} else if(button == refillDime) {

		} else if(button == refillQuarter) {

		} else if(button == refillLoonie) {

		} else if(button == refillTwoonie) {

		}

		// for emptying coin storage unit
		else if(button == emptyNickel) {

		} else if(button == emptyDime) {

		} else if(button == emptyQuarter) {

		} else if(button == emptyLoonie) {

		} else if(button == emptyTwoonie) {

		}

		// for refilling receipt printer
		else if(button == refillInk) {

		} else if(button == refillPaper) {

		} 
	}

}
