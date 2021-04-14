package org.lsmr.selfcheckout.control;

import java.awt.BorderLayout;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.RepaintManager;
import javax.swing.SwingConstants;

import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.PLUCodedItem;
import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.control.gui.GUIController;
import org.lsmr.selfcheckout.control.gui.StateHandler;
import org.lsmr.selfcheckout.control.gui.statedata.BaggingAreaWeightData;
import org.lsmr.selfcheckout.control.gui.statedata.KeypadStateData;
import org.lsmr.selfcheckout.control.gui.statedata.ListProductStateData;
import org.lsmr.selfcheckout.control.gui.statedata.ScaleStateData;
import org.lsmr.selfcheckout.control.gui.statedata.StateData;
import org.lsmr.selfcheckout.control.gui.states.GUIState;
import org.lsmr.selfcheckout.devices.OverloadException;

public class PhysicalSimulatorWindow implements ActionListener {

	private Checkout checkout;
	private GUIController stateHandler;
	// customer inserts banknote
	private JButton insert5;
	private JButton insert10;
	private JButton insert20;
	private JButton insert50;
	private JButton insert100;

	// customer inserts coin
	private JButton insertNickel;
	private JButton insertDime;
	private JButton insertQuarter;
	private JButton insertLoonie;
	private JButton insertTwoonie;

	private JButton scanToy;
	private JButton scanPiano;
	private JButton scanPlayStation;
	private JButton scanCard;
	private JButton bagToy;
	private JButton bagPiano;
	private JButton bagPlayStation;

	private JLabel bagToyLabel;
	private JLabel bagPianoLabel;
	private JLabel bagPlayStationLabel;

	private float weight;

	// customer places item on scale
	private JButton add100g;
	private JButton add50g;
	private JButton add20g;
	private JButton add10g;
	private JButton addPears;
	private JButton addApples;

	// here incase they add too much weight by accident
	private JButton minus100g;
	private JButton minus50g;
	private JButton minus20g;
	private JButton minus10g;
	private JButton minus5g;
	private JButton minus1g;

	// customer payment methods -- add one for giftcard?
	private JButton enterPin; //not sure if we need this one
	private JButton tapCard;
	private JButton insertCard;
	private JButton swipeCard;

	private JButton goBack;

	private BarcodedItem toy = new BarcodedItem(new Barcode("1124341"), 70.1);
	private BarcodedItem ps6 = new BarcodedItem(new Barcode("0101010"), 150.1);
	private BarcodedItem piano = new BarcodedItem(new Barcode("12345"), 321);

	private PLUCodedItem apples = new PLUCodedItem(new PriceLookupCode("5425"), 645);
	private PLUCodedItem pears = new PLUCodedItem(new PriceLookupCode("4523"), 918);
	
	private JFrame frame;

	public PhysicalSimulatorWindow(Checkout c, GUIController s) {
		this.checkout = c;
		this.stateHandler = s;
	}

	public void createWindow() {
		frame = new JFrame("Simulator");
		// the main panel
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 10, 30));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		// set up statement at top of screen
		// image for coop logo downloaded from website below
		// https://www.google.com/search?q=coop+png&tbm=isch&ved=2ahUKEwjC9bTd-PHvAhU8AzQIHSiLAkkQ2-cCegQIABAA&oq=coop+png&gs_lcp=CgNpbWcQAzIECAAQQzICCAAyBggAEAcQHjIGCAAQBxAeMgYIABAHEB4yBggAEAcQHjIGCAAQBxAeMgYIABAHEB4yBggAEAcQHjIGCAAQBxAeUM-sA1jPrANg67IDaABwAHgAgAFDiAFDkgEBMZgBAKABAaoBC2d3cy13aXotaW1nwAEB&sclient=img&ei=MLFwYMKdCryG0PEPqJaKyAQ&bih=619&biw=1280#imgrc=EmUG01nblMHYrM
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout(275, 0));
		JLabel topStatement = new JLabel("Choose an Option");
		ImageIcon coopImg = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/cooplogo.png");
		Image coOpImg = coopImg.getImage();
		Image newCoOpImg = coOpImg.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
		ImageIcon coOpImgResized = new ImageIcon(newCoOpImg);
		JLabel coOpLogo = new JLabel(coOpImgResized);
		topStatement.setFont(new Font("Arial", Font.BOLD, 60));
		topPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
		topPanel.add(topStatement, BorderLayout.CENTER);
		topPanel.add(coOpLogo, BorderLayout.EAST);

		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new GridLayout(2, 3, 20, 0));
		middlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		// panel for put item on scale image
		JPanel imagePanel = new JPanel();
		ImageIcon checkoutImg = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/guy at checkout.png");
		Image checkOutImg = checkoutImg.getImage();
		Image newCheckoutImg = checkOutImg.getScaledInstance(450, 350, java.awt.Image.SCALE_SMOOTH);
		ImageIcon checkoutImgResized = new ImageIcon(newCheckoutImg);
		JLabel checkoutLogo = new JLabel(checkoutImgResized);
		imagePanel.add(checkoutLogo);

		JPanel buttonPanel = new JPanel();

		Dimension buttonSize = new Dimension(250, 32);

		JPanel banknotePanel = new JPanel();
		banknotePanel.setLayout(new BoxLayout(banknotePanel, BoxLayout.Y_AXIS));
		banknotePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 80, 0));
		JLabel banknoteLabel = new JLabel("Insert Banknote");
		banknoteLabel.setFont(new Font("Arial", Font.BOLD, 26));
		JPanel bankNotePanel = new JPanel();
		bankNotePanel.add(banknoteLabel);
		banknotePanel.add(bankNotePanel);

		// image for all banknotes downloaded from website below - cropped the image
		// https://en.wikipedia.org/wiki/Canadian_dollar#/media/File:Canadian_Frontier_Banknotes_faces.png		
		ImageIcon five = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/5 bill.png");
		Image fiveImg = five.getImage();
		Image newFiveImg = fiveImg.getScaledInstance(50, 25, java.awt.Image.SCALE_SMOOTH);
		ImageIcon fiveImgResized = new ImageIcon(newFiveImg);

		insert5 = new JButton();
		insert5.setLayout(new BorderLayout());
		JLabel fiveIcon = new JLabel(fiveImgResized);
		JLabel fiveLabel = new JLabel("Insert $5.00", SwingConstants.CENTER);
		fiveLabel.setFont(new Font("Arial", Font.BOLD, 22));
		insert5.add(fiveLabel, BorderLayout.CENTER);
		insert5.add(fiveIcon, BorderLayout.WEST);
		insert5.setSize(buttonSize);
		insert5.setPreferredSize(buttonSize);
		insert5.setMinimumSize(buttonSize);
		insert5.setMaximumSize(buttonSize);
		insert5.addActionListener(this);
		JPanel insert5Panel = new JPanel();
		insert5Panel.add(insert5);
		banknotePanel.add(insert5Panel);

		ImageIcon ten = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/10 bill.png");
		Image tenImg = ten.getImage();
		Image newTenImg = tenImg.getScaledInstance(50, 25, java.awt.Image.SCALE_SMOOTH);
		ImageIcon tenImgResized = new ImageIcon(newTenImg);

		insert10 = new JButton();
		insert10.setLayout(new BorderLayout());
		JLabel tenIcon = new JLabel(tenImgResized);
		JLabel tenLabel = new JLabel("Insert $10.00", SwingConstants.CENTER);
		tenLabel.setFont(new Font("Arial", Font.BOLD, 22));
		insert10.add(tenLabel, BorderLayout.CENTER);
		insert10.add(tenIcon, BorderLayout.WEST);
		insert10.setSize(buttonSize);
		insert10.setPreferredSize(buttonSize);
		insert10.setMinimumSize(buttonSize);
		insert10.setMaximumSize(buttonSize);
		insert10.addActionListener(this);
		JPanel insert10Panel = new JPanel();
		insert10Panel.add(insert10);
		banknotePanel.add(insert10Panel);

		ImageIcon twenty = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/20 bill.png");
		Image twentyImg = twenty.getImage();
		Image newTwentyImg = twentyImg.getScaledInstance(50, 25, java.awt.Image.SCALE_SMOOTH);
		ImageIcon twentyImgResized = new ImageIcon(newTwentyImg);

		insert20 = new JButton();
		insert20.setLayout(new BorderLayout());
		JLabel twentyIcon = new JLabel(twentyImgResized);
		JLabel twentyLabel = new JLabel("Insert $20.00", SwingConstants.CENTER);
		twentyLabel.setFont(new Font("Arial", Font.BOLD, 22));
		insert20.add(twentyLabel, BorderLayout.CENTER);
		insert20.add(twentyIcon, BorderLayout.WEST);
		insert20.setSize(buttonSize);
		insert20.setPreferredSize(buttonSize);
		insert20.setMinimumSize(buttonSize);
		insert20.setMaximumSize(buttonSize);
		insert20.addActionListener(this);
		JPanel insert20Panel = new JPanel();
		insert20Panel.add(insert20);
		banknotePanel.add(insert20Panel);

		ImageIcon fifty = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/50 bill.png");
		Image fiftyImg = fifty.getImage();
		Image newFiftyImg = fiftyImg.getScaledInstance(50, 25, java.awt.Image.SCALE_SMOOTH);
		ImageIcon fiftyImgResized = new ImageIcon(newFiftyImg);

		insert50 = new JButton();
		insert50.setLayout(new BorderLayout());
		JLabel fiftyIcon = new JLabel(fiftyImgResized);
		JLabel fiftyLabel = new JLabel("Insert $50.00", SwingConstants.CENTER);
		fiftyLabel.setFont(new Font("Arial", Font.BOLD, 22));
		insert50.add(fiftyLabel, BorderLayout.CENTER);
		insert50.add(fiftyIcon, BorderLayout.WEST);
		insert50.setSize(buttonSize);
		insert50.setPreferredSize(buttonSize);
		insert50.setMinimumSize(buttonSize);
		insert50.setMaximumSize(buttonSize);
		insert50.addActionListener(this);
		JPanel insert50Panel = new JPanel();
		insert50Panel.add(insert50);
		banknotePanel.add(insert50Panel);

		ImageIcon hundred = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/100 bill.png");
		Image hundredImg = hundred.getImage();
		Image newHundredImg = hundredImg.getScaledInstance(50, 25, java.awt.Image.SCALE_SMOOTH);
		ImageIcon hundredImgResized = new ImageIcon(newHundredImg);

		insert100 = new JButton();
		insert100.setLayout(new BorderLayout());
		JLabel hundredIcon = new JLabel(hundredImgResized);
		JLabel hundredLabel = new JLabel("Insert $100.00", SwingConstants.CENTER);
		hundredLabel.setFont(new Font("Arial", Font.BOLD, 22));
		insert100.add(hundredLabel, BorderLayout.CENTER);
		insert100.add(hundredIcon, BorderLayout.WEST);
		insert100.setSize(buttonSize);
		insert100.setPreferredSize(buttonSize);
		insert100.setMinimumSize(buttonSize);
		insert100.setMaximumSize(buttonSize);
		insert100.addActionListener(this);
		JPanel insert100Panel = new JPanel();
		insert100Panel.add(insert100);
		banknotePanel.add(insert100Panel);

		JPanel coinPanel = new JPanel();
		coinPanel.setLayout(new BoxLayout(coinPanel, BoxLayout.Y_AXIS));
		coinPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 80, 0));
		JLabel coinLabel = new JLabel("Insert Coin");
		coinLabel.setFont(new Font("Arial", Font.BOLD, 26));
		JPanel coiNPanel = new JPanel();
		coiNPanel.add(coinLabel);
		coinPanel.add(coiNPanel);

		// image for nickel downloaded from website below
		// https://www.cleanpng.com/png-canada-nickel-coin-dime-loonie-6720907/download-png.html
		ImageIcon nickel = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/nickel.png");
		Image nickelImg = nickel.getImage();
		Image newNickelImg = nickelImg.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
		ImageIcon nickelImgResized = new ImageIcon(newNickelImg);

		insertNickel = new JButton();
		insertNickel.setLayout(new BorderLayout());
		JLabel nickelIcon = new JLabel(nickelImgResized);
		JLabel nickelLabel = new JLabel("Insert $0.05", SwingConstants.CENTER);
		nickelLabel.setFont(new Font("Arial", Font.BOLD, 22));
		insertNickel.add(nickelLabel, BorderLayout.CENTER);
		insertNickel.add(nickelIcon, BorderLayout.WEST);
		insertNickel.setSize(buttonSize);
		insertNickel.setPreferredSize(buttonSize);
		insertNickel.setMinimumSize(buttonSize);
		insertNickel.setMaximumSize(buttonSize);
		insertNickel.addActionListener(this);
		JPanel insertNickelPanel = new JPanel();
		insertNickelPanel.add(insertNickel);
		coinPanel.add(insertNickelPanel);

		// image for dime downloaded from website below
		// https://www.cleanpng.com/png-150th-anniversary-of-canada-coin-set-royal-canadia-1655179/download-png.html
		ImageIcon dime = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/dime.png");
		Image dimeImg = dime.getImage();
		Image newDimeImg = dimeImg.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
		ImageIcon dimeImgResized = new ImageIcon(newDimeImg);

		insertDime = new JButton();
		insertDime.setLayout(new BorderLayout());
		JLabel dimeIcon = new JLabel(dimeImgResized);
		JLabel dimeLabel = new JLabel("Insert $0.10", SwingConstants.CENTER);
		dimeLabel.setFont(new Font("Arial", Font.BOLD, 22));
		insertDime.add(dimeLabel, BorderLayout.CENTER);
		insertDime.add(dimeIcon, BorderLayout.WEST);
		insertDime.setSize(buttonSize);
		insertDime.setPreferredSize(buttonSize);
		insertDime.setMinimumSize(buttonSize);
		insertDime.setMaximumSize(buttonSize);
		insertDime.addActionListener(this);
		JPanel insertDimePanel = new JPanel();
		insertDimePanel.add(insertDime);
		coinPanel.add(insertDimePanel);

		// image of quarter downloaded from website below
		// https://www.cleanpng.com/png-150th-anniversary-of-canada-canadian-coins-quarter-2268512/download-png.html
		ImageIcon quarter = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/quarter.png");
		Image quarterImg = quarter.getImage();
		Image newQuarterImg = quarterImg.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
		ImageIcon quarterImgResized = new ImageIcon(newQuarterImg);

		insertQuarter = new JButton();
		insertQuarter.setLayout(new BorderLayout());
		JLabel quarterIcon = new JLabel(quarterImgResized);
		JLabel quarterLabel = new JLabel("Insert $0.25", SwingConstants.CENTER);
		quarterLabel.setFont(new Font("Arial", Font.BOLD, 22));
		insertQuarter.add(quarterLabel, BorderLayout.CENTER);
		insertQuarter.add(quarterIcon, BorderLayout.WEST);
		insertQuarter.setSize(buttonSize);
		insertQuarter.setPreferredSize(buttonSize);
		insertQuarter.setMinimumSize(buttonSize);
		insertQuarter.setMaximumSize(buttonSize);
		insertQuarter.addActionListener(this);
		JPanel insertQuarterPanel = new JPanel();
		insertQuarterPanel.add(insertQuarter);
		coinPanel.add(insertQuarterPanel);

		// image of loonie downloaded from website below
		// https://www.cleanpng.com/png-dollar-coin-canada-loonie-royal-canadian-mint-unci-3389442/download-png.html
		ImageIcon loonie = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/loonie.png");
		Image loonieImg = loonie.getImage();
		Image newLoonieImg = loonieImg.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
		ImageIcon loonieImgResized = new ImageIcon(newLoonieImg);

		insertLoonie = new JButton();
		insertLoonie.setLayout(new BorderLayout());
		JLabel loonieIcon = new JLabel(loonieImgResized);
		JLabel loonieLabel = new JLabel("Insert $1.00", SwingConstants.CENTER);
		loonieLabel.setFont(new Font("Arial", Font.BOLD, 22));
		insertLoonie.add(loonieLabel, BorderLayout.CENTER);
		insertLoonie.add(loonieIcon, BorderLayout.WEST);
		insertLoonie.setSize(buttonSize);
		insertLoonie.setPreferredSize(buttonSize);
		insertLoonie.setMinimumSize(buttonSize);
		insertLoonie.setMaximumSize(buttonSize);
		insertLoonie.addActionListener(this);
		JPanel insertLooniePanel = new JPanel();
		insertLooniePanel.add(insertLoonie);
		coinPanel.add(insertLooniePanel);

		// image of twoonie downloaded from website below
		// https://www.cleanpng.com/png-canada-toonie-loonie-canadian-dollar-royal-canadia-1109303/download-png.html
		ImageIcon twoonie = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/twoonie.png");
		Image twoonieImg = twoonie.getImage();
		Image newTwoonieImg = twoonieImg.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
		ImageIcon twoonieImgResized = new ImageIcon(newTwoonieImg);

		insertTwoonie = new JButton();
		insertTwoonie.setLayout(new BorderLayout());
		JLabel twoonieIcon = new JLabel(twoonieImgResized);
		JLabel twoonieLabel = new JLabel("Insert $2.00", SwingConstants.CENTER);
		twoonieLabel.setFont(new Font("Arial", Font.BOLD, 22));
		insertTwoonie.add(twoonieLabel, BorderLayout.CENTER);
		insertTwoonie.add(twoonieIcon, BorderLayout.WEST);
		insertTwoonie.setSize(buttonSize);
		insertTwoonie.setPreferredSize(buttonSize);
		insertTwoonie.setMinimumSize(buttonSize);
		insertTwoonie.setMaximumSize(buttonSize);
		insertTwoonie.addActionListener(this);
		JPanel insertTwooniePanel = new JPanel();
		insertTwooniePanel.add(insertTwoonie);
		coinPanel.add(insertTwooniePanel);

		JPanel addWeightPanel = new JPanel();
		addWeightPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
		addWeightPanel.setLayout(new BoxLayout(addWeightPanel, BoxLayout.Y_AXIS));
		JLabel addWeightLabel = new JLabel("Add Weight to Scale");
		addWeightLabel.setFont(new Font("Arial", Font.BOLD, 26));
		JPanel addWeighTPanel = new JPanel();
		addWeighTPanel.add(addWeightLabel);
		addWeightPanel.add(addWeighTPanel);
		weight = (float) 0.000;

		// image for both plus sign and minus sign downloaded from website below - cropped the image
		// https://www.pngitem.com/download/ibbmhT_plus-sign-minus-symbol-math-and-signs-computer/
		ImageIcon plus = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/plus.png");
		Image plusImg = plus.getImage();
		Image newPlusImg = plusImg.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
		ImageIcon plusImgResized = new ImageIcon(newPlusImg);

		addApples = new JButton();
		addApples.setLayout(new BorderLayout());
		JLabel addApplesIcon = new JLabel(plusImgResized);
		JLabel addApplesLabel = new JLabel("Add " + apples.getWeight() + "g of Apples to Scale", SwingConstants.CENTER);
		addApplesLabel.setFont(new Font("Arial", Font.BOLD, 22));
		addApples.add(addApplesLabel, BorderLayout.CENTER);
		addApples.add(addApplesIcon, BorderLayout.WEST);
		addApples.setSize(buttonSize);
		addApples.setPreferredSize(buttonSize);
		addApples.setMinimumSize(buttonSize);
		addApples.setMaximumSize(buttonSize);
		addApples.addActionListener(this);
		JPanel addApplesPanel = new JPanel();
		addApplesPanel.add(addApples);
		addWeightPanel.add(addApplesPanel);

		addPears = new JButton();
		addPears.setLayout(new BorderLayout());
		JLabel addPearsIcon = new JLabel(plusImgResized);
		JLabel addPearsLabel = new JLabel("Add " + pears.getWeight()+ "g of Pears to Scale", SwingConstants.CENTER);
		addPearsLabel.setFont(new Font("Arial", Font.BOLD, 22));
		addPears.add(addPearsLabel, BorderLayout.CENTER);
		addPears.add(addPearsIcon, BorderLayout.WEST);
		addPears.setSize(buttonSize);
		addPears.setPreferredSize(buttonSize);
		addPears.setMinimumSize(buttonSize);
		addPears.setMaximumSize(buttonSize);
		addPears.addActionListener(this);
		JPanel addPearsPanel = new JPanel();
		addPearsPanel.add(addPears);
		addWeightPanel.add(addPearsPanel);

		add10g = new JButton();
		add10g.setLayout(new BorderLayout());
		JLabel add10Icon = new JLabel(plusImgResized);
		JLabel add10Label = new JLabel("Add 10g to Scale", SwingConstants.CENTER);
		add10Label.setFont(new Font("Arial", Font.BOLD, 22));
		add10g.add(add10Label, BorderLayout.CENTER);
		add10g.add(add10Icon, BorderLayout.WEST);
		add10g.setSize(buttonSize);
		add10g.setPreferredSize(buttonSize);
		add10g.setMinimumSize(buttonSize);
		add10g.setMaximumSize(buttonSize);
		add10g.addActionListener(this);
		JPanel add10Panel = new JPanel();
		add10Panel.add(add10g);
		addWeightPanel.add(add10Panel);

		add20g = new JButton();
		add20g.setLayout(new BorderLayout());
		JLabel add20Icon = new JLabel(plusImgResized);
		JLabel add20Label = new JLabel("Add 20g to Scale", SwingConstants.CENTER);
		add20Label.setFont(new Font("Arial", Font.BOLD, 22));
		add20g.add(add20Label, BorderLayout.CENTER);
		add20g.add(add20Icon, BorderLayout.WEST);
		add20g.setSize(buttonSize);
		add20g.setPreferredSize(buttonSize);
		add20g.setMinimumSize(buttonSize);
		add20g.setMaximumSize(buttonSize);
		add20g.addActionListener(this);
		JPanel add20Panel = new JPanel();
		add20Panel.add(add20g);
		addWeightPanel.add(add20Panel);

		add50g = new JButton();
		add50g.setLayout(new BorderLayout());
		JLabel add50Icon = new JLabel(plusImgResized);
		JLabel add50Label = new JLabel("Add 50g to Scale", SwingConstants.CENTER);
		add50Label.setFont(new Font("Arial", Font.BOLD, 22));
		add50g.add(add50Label, BorderLayout.CENTER);
		add50g.add(add50Icon, BorderLayout.WEST);
		add50g.setSize(buttonSize);
		add50g.setPreferredSize(buttonSize);
		add50g.setMinimumSize(buttonSize);
		add50g.setMaximumSize(buttonSize);
		add50g.addActionListener(this);
		JPanel add50Panel = new JPanel();
		add50Panel.add(add50g);
		addWeightPanel.add(add50Panel);

		add100g = new JButton();
		add100g.setLayout(new BorderLayout());
		JLabel add100Icon = new JLabel(plusImgResized);
		JLabel add100Label = new JLabel("Add 100g to Scale", SwingConstants.CENTER);
		add100Label.setFont(new Font("Arial", Font.BOLD, 20));
		add100g.add(add100Label, BorderLayout.CENTER);
		add100g.add(add100Icon, BorderLayout.WEST);
		add100g.setSize(buttonSize);
		add100g.setPreferredSize(buttonSize);
		add100g.setMinimumSize(buttonSize);
		add100g.setMaximumSize(buttonSize);
		add100g.addActionListener(this);
		JPanel add100Panel = new JPanel();
		add100Panel.add(add100g);
		addWeightPanel.add(add100Panel);

		JPanel removeWeightPanel = new JPanel();
		removeWeightPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
		removeWeightPanel.setLayout(new BoxLayout(removeWeightPanel, BoxLayout.Y_AXIS));
		JLabel removeWeightLabel = new JLabel("Remove Weight from Scale");
		removeWeightLabel.setFont(new Font("Arial", Font.BOLD, 26));
		JPanel removeWeighTPanel = new JPanel();
		removeWeighTPanel.add(removeWeightLabel);
		removeWeightPanel.add(removeWeighTPanel);

		ImageIcon minus = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/minus.png");
		Image minusImg = minus.getImage();
		Image newMinusImg = minusImg.getScaledInstance(25, 6, java.awt.Image.SCALE_SMOOTH);
		ImageIcon minusImgResized = new ImageIcon(newMinusImg);

		minus1g = new JButton();
		minus1g.setLayout(new BorderLayout());
		JLabel minus1Icon = new JLabel(minusImgResized);
		JLabel minus1Label = new JLabel("Remove 1g from Scale", SwingConstants.CENTER);
		minus1Label.setFont(new Font("Arial", Font.BOLD, 16));
		minus1g.add(minus1Label, BorderLayout.CENTER);
		minus1g.add(minus1Icon, BorderLayout.WEST);
		minus1g.setSize(buttonSize);
		minus1g.setPreferredSize(buttonSize);
		minus1g.setMinimumSize(buttonSize);
		minus1g.setMaximumSize(buttonSize);
		minus1g.addActionListener(this);
		JPanel minus1Panel = new JPanel();
		minus1Panel.add(minus1g);
		removeWeightPanel.add(minus1Panel);

		minus5g = new JButton();
		minus5g.setLayout(new BorderLayout());
		JLabel minus5Icon = new JLabel(minusImgResized);
		JLabel minus5Label = new JLabel("Remove 5g from Scale", SwingConstants.CENTER);
		minus5Label.setFont(new Font("Arial", Font.BOLD, 16));
		minus5g.add(minus5Label, BorderLayout.CENTER);
		minus5g.add(minus5Icon, BorderLayout.WEST);
		minus5g.setSize(buttonSize);
		minus5g.setPreferredSize(buttonSize);
		minus5g.setMinimumSize(buttonSize);
		minus5g.setMaximumSize(buttonSize);
		minus5g.addActionListener(this);
		JPanel minus5Panel = new JPanel();
		minus5Panel.add(minus5g);
		removeWeightPanel.add(minus5Panel);

		minus10g = new JButton();
		minus10g.setLayout(new BorderLayout());
		JLabel minus10Icon = new JLabel(minusImgResized);
		JLabel minus10Label = new JLabel("Remove 10g from Scale", SwingConstants.CENTER);
		minus10Label.setFont(new Font("Arial", Font.BOLD, 16));
		minus10g.add(minus10Label, BorderLayout.CENTER);
		minus10g.add(minus10Icon, BorderLayout.WEST);
		minus10g.setSize(buttonSize);
		minus10g.setPreferredSize(buttonSize);
		minus10g.setMinimumSize(buttonSize);
		minus10g.setMaximumSize(buttonSize);
		minus10g.addActionListener(this);
		JPanel minus10Panel = new JPanel();
		minus10Panel.add(minus10g);
		removeWeightPanel.add(minus10Panel);

		minus20g = new JButton();
		minus20g.setLayout(new BorderLayout());
		JLabel minus20Icon = new JLabel(minusImgResized);
		JLabel minus20Label = new JLabel("Remove 20g from Scale", SwingConstants.CENTER);
		minus20Label.setFont(new Font("Arial", Font.BOLD, 16));
		minus20g.add(minus20Label, BorderLayout.CENTER);
		minus20g.add(minus20Icon, BorderLayout.WEST);
		minus20g.setSize(buttonSize);
		minus20g.setPreferredSize(buttonSize);
		minus20g.setMinimumSize(buttonSize);
		minus20g.setMaximumSize(buttonSize);
		minus20g.addActionListener(this);
		JPanel minus20Panel = new JPanel();
		minus20Panel.add(minus20g);
		removeWeightPanel.add(minus20Panel);

		minus50g = new JButton();
		minus50g.setLayout(new BorderLayout());
		JLabel minus50Icon = new JLabel(minusImgResized);
		JLabel minus50Label = new JLabel("Remove 50g from Scale", SwingConstants.CENTER);
		minus50Label.setFont(new Font("Arial", Font.BOLD, 16));
		minus50g.add(minus50Label, BorderLayout.CENTER);
		minus50g.add(minus50Icon, BorderLayout.WEST);
		minus50g.setSize(buttonSize);
		minus50g.setPreferredSize(buttonSize);
		minus50g.setMinimumSize(buttonSize);
		minus50g.setMaximumSize(buttonSize);
		minus50g.addActionListener(this);
		JPanel minus50Panel = new JPanel();
		minus50Panel.add(minus50g);
		removeWeightPanel.add(minus50Panel);

		minus100g = new JButton();
		minus100g.setLayout(new BorderLayout());
		JLabel minus100Icon = new JLabel(minusImgResized);
		JLabel minus100Label = new JLabel("Remove 100g from Scale", SwingConstants.CENTER);
		minus100Label.setFont(new Font("Arial", Font.BOLD, 15));
		minus100g.add(minus100Label, BorderLayout.CENTER);
		minus100g.add(minus100Icon, BorderLayout.WEST);
		minus100g.setSize(buttonSize);
		minus100g.setPreferredSize(buttonSize);
		minus100g.setMinimumSize(buttonSize);
		minus100g.setMaximumSize(buttonSize);
		minus100g.addActionListener(this);
		JPanel minus100Panel = new JPanel();
		minus100Panel.add(minus100g);
		removeWeightPanel.add(minus100Panel);

		JPanel actionsPanel = new JPanel();
		actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));
		//actionsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 135, 0));
		JLabel actionsLabel = new JLabel("Scan & Bag");
		actionsLabel.setFont(new Font("Arial", Font.BOLD, 26));
		JPanel actionSPanel = new JPanel();
		actionSPanel.add(actionsLabel);
		actionsPanel.add(actionSPanel);

		//black with yellow scanner
		//https://www.cleanpng.com/png-barcode-scanners-stock-photography-label-barcode-s-3544761/download-png.html
		ImageIcon scanner = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/scanner yellow.png");
		Image scanImg = scanner.getImage();
		Image newScanImg = scanImg.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
		ImageIcon scanImgResized = new ImageIcon(newScanImg);

		scanCard = new JButton();
		scanCard.setLayout(new BorderLayout());
		JLabel scanCardIcon = new JLabel(scanImgResized);
		JLabel scanCardLabel = new JLabel("Scan Member Card", SwingConstants.CENTER);
		scanCardLabel.setFont(new Font("Arial", Font.BOLD, 20));
		scanCard.add(scanCardLabel, BorderLayout.CENTER);
		scanCard.add(scanCardIcon, BorderLayout.WEST);
		scanCard.setSize(buttonSize);
		scanCard.setPreferredSize(buttonSize);
		scanCard.setMinimumSize(buttonSize);
		scanCard.setMaximumSize(buttonSize);
		scanCard.addActionListener(this);
		JPanel scanCardPanel = new JPanel();
		scanCardPanel.add(scanCard);
		actionsPanel.add(scanCardPanel);

		scanToy = new JButton();
		scanToy.setLayout(new BorderLayout());
		JLabel scanToyIcon = new JLabel(scanImgResized);
		JLabel scanToyLabel = new JLabel("Scan Toy", SwingConstants.CENTER);
		scanToyLabel.setFont(new Font("Arial", Font.BOLD, 22));
		scanToy.add(scanToyLabel, BorderLayout.CENTER);
		scanToy.add(scanToyIcon, BorderLayout.WEST);
		scanToy.setSize(buttonSize);
		scanToy.setPreferredSize(buttonSize);
		scanToy.setMinimumSize(buttonSize);
		scanToy.setMaximumSize(buttonSize);
		scanToy.addActionListener(this);
		JPanel scanToyPanel = new JPanel();
		scanToyPanel.add(scanToy);
		actionsPanel.add(scanToyPanel);

		scanPiano = new JButton();
		scanPiano.setLayout(new BorderLayout());
		JLabel scanPianoIcon = new JLabel(scanImgResized);
		JLabel scanPianoLabel = new JLabel("Scan Piano", SwingConstants.CENTER);
		scanPianoLabel.setFont(new Font("Arial", Font.BOLD, 22));
		scanPiano.add(scanPianoLabel, BorderLayout.CENTER);
		scanPiano.add(scanPianoIcon, BorderLayout.WEST);
		scanPiano.setSize(buttonSize);
		scanPiano.setPreferredSize(buttonSize);
		scanPiano.setMinimumSize(buttonSize);
		scanPiano.setMaximumSize(buttonSize);
		scanPiano.addActionListener(this);
		JPanel scanPianoPanel = new JPanel();
		scanPianoPanel.add(scanPiano);
		actionsPanel.add(scanPianoPanel);

		scanPlayStation = new JButton();
		scanPlayStation.setLayout(new BorderLayout());
		JLabel scanPlayStationIcon = new JLabel(scanImgResized);
		JLabel scanPlayStationLabel = new JLabel("Scan PlayStation", SwingConstants.CENTER);
		scanPlayStationLabel.setFont(new Font("Arial", Font.BOLD, 22));
		scanPlayStation.add(scanPlayStationLabel, BorderLayout.CENTER);
		scanPlayStation.add(scanPlayStationIcon, BorderLayout.WEST);
		scanPlayStation.setSize(buttonSize);
		scanPlayStation.setPreferredSize(buttonSize);
		scanPlayStation.setMinimumSize(buttonSize);
		scanPlayStation.setMaximumSize(buttonSize);
		scanPlayStation.addActionListener(this);
		JPanel scanPlayStationPanel = new JPanel();
		scanPlayStationPanel.add(scanPlayStation);
		actionsPanel.add(scanPlayStationPanel);

		// bag of groceries
		//https://www.cleanpng.com/png-shopping-bags-trolleys-grocery-store-clip-art-blac-3014424/download-png.html
		ImageIcon bag = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/bag of groceries.png");
		Image bagImg = bag.getImage();
		Image newBagImg = bagImg.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
		ImageIcon bagImgResized = new ImageIcon(newBagImg);

		bagToy = new JButton();
		bagToy.setLayout(new BorderLayout());
		JLabel bagToyIcon = new JLabel(bagImgResized);
		bagToyLabel = new JLabel("Bag Toy", SwingConstants.CENTER);
		bagToyLabel.setFont(new Font("Arial", Font.BOLD, 22));
		bagToy.add(bagToyLabel, BorderLayout.CENTER);
		bagToy.add(bagToyIcon, BorderLayout.WEST);
		bagToy.setSize(buttonSize);
		bagToy.setPreferredSize(buttonSize);
		bagToy.setMinimumSize(buttonSize);
		bagToy.setMaximumSize(buttonSize);
		bagToy.addActionListener(this);
		JPanel bagToyPanel = new JPanel();
		bagToyPanel.add(bagToy);
		actionsPanel.add(bagToyPanel);

		bagPiano = new JButton();
		bagPiano.setLayout(new BorderLayout());
		JLabel bagPianoIcon = new JLabel(bagImgResized);
		bagPianoLabel = new JLabel("Bag Piano", SwingConstants.CENTER);
		bagPianoLabel.setFont(new Font("Arial", Font.BOLD, 22));
		bagPiano.add(bagPianoLabel, BorderLayout.CENTER);
		bagPiano.add(bagPianoIcon, BorderLayout.WEST);
		bagPiano.setSize(buttonSize);
		bagPiano.setPreferredSize(buttonSize);
		bagPiano.setMinimumSize(buttonSize);
		bagPiano.setMaximumSize(buttonSize);
		bagPiano.addActionListener(this);
		JPanel bagPianoPanel = new JPanel();
		bagPianoPanel.add(bagPiano);
		actionsPanel.add(bagPianoPanel);

		bagPlayStation = new JButton();
		bagPlayStation.setLayout(new BorderLayout());
		JLabel bagPlayStationIcon = new JLabel(bagImgResized);
		bagPlayStationLabel = new JLabel("Bag PlayStation", SwingConstants.CENTER);
		bagPlayStationLabel.setFont(new Font("Arial", Font.BOLD, 22));
		bagPlayStation.add(bagPlayStationLabel, BorderLayout.CENTER);
		bagPlayStation.add(bagPlayStationIcon, BorderLayout.WEST);
		bagPlayStation.setSize(buttonSize);
		bagPlayStation.setPreferredSize(buttonSize);
		bagPlayStation.setMinimumSize(buttonSize);
		bagPlayStation.setMaximumSize(buttonSize);
		bagPlayStation.addActionListener(this);
		JPanel bagPlayStationPanel = new JPanel();
		bagPlayStationPanel.add(bagPlayStation);
		actionsPanel.add(bagPlayStationPanel);

		JPanel payPanel = new JPanel();
		payPanel.setLayout(new BoxLayout(payPanel, BoxLayout.Y_AXIS));
		payPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 125, 0));
		JLabel payLabel = new JLabel("Use PIN Pad");
		payLabel.setFont(new Font("Arial", Font.BOLD, 26));
		JPanel paYPanel = new JPanel();
		paYPanel.add(payLabel);
		payPanel.add(paYPanel);

		// image for swipe card downloaded from website below
		// https://www.cleanpng.com/png-credit-card-payment-card-invoice-electronic-bill-p-2048347/download-png.html
		ImageIcon swipe = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/swipe card.png");
		Image swipeImg = swipe.getImage();
		Image newSwipeImg = swipeImg.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
		ImageIcon swipeImgResized = new ImageIcon(newSwipeImg);

		swipeCard = new JButton();
		swipeCard.setLayout(new BorderLayout());
		JLabel swipeCardIcon = new JLabel(swipeImgResized);
		JLabel swipeCardLabel = new JLabel("Swipe Card", SwingConstants.CENTER);
		swipeCardLabel.setFont(new Font("Arial", Font.BOLD, 22));
		swipeCard.add(swipeCardLabel, BorderLayout.CENTER);
		swipeCard.add(swipeCardIcon, BorderLayout.WEST);
		swipeCard.setSize(buttonSize);
		swipeCard.setPreferredSize(buttonSize);
		swipeCard.setMinimumSize(buttonSize);
		swipeCard.setMaximumSize(buttonSize);
		swipeCard.addActionListener(this);
		JPanel swipeCardPanel = new JPanel();
		swipeCardPanel.add(swipeCard);
		payPanel.add(swipeCardPanel);

		// image for tap card downloaded from website below
		// https://www.cleanpng.com/png-clip-art-scalable-vector-graphics-contactless-paym-6075496/download-png.html
		ImageIcon tap = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/tap card.png");
		Image tapImg = tap.getImage();
		Image newTapImg = tapImg.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
		ImageIcon tapImgResized = new ImageIcon(newTapImg);

		tapCard = new JButton();
		tapCard.setLayout(new BorderLayout());
		JLabel tapCardIcon = new JLabel(tapImgResized);
		JLabel tapCardLabel = new JLabel("Tap Card", SwingConstants.CENTER);
		tapCardLabel.setFont(new Font("Arial", Font.BOLD, 22));
		tapCard.add(tapCardLabel, BorderLayout.CENTER);
		tapCard.add(tapCardIcon, BorderLayout.WEST);
		tapCard.setSize(buttonSize);
		tapCard.setPreferredSize(buttonSize);
		tapCard.setMinimumSize(buttonSize);
		tapCard.setMaximumSize(buttonSize);
		tapCard.addActionListener(this);
		JPanel tapCardPanel = new JPanel();
		tapCardPanel.add(tapCard);
		payPanel.add(tapCardPanel);

		//image for insert card downloaded from website below
		// https://icon-library.com/icon/insert-card-icon-22.html
		ImageIcon insert = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/insert-card-icon-22.jpg");
		Image insertImg = insert.getImage();
		Image newInsertImg = insertImg.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
		ImageIcon insertImgResized = new ImageIcon(newInsertImg);

		insertCard = new JButton();
		insertCard.setLayout(new BorderLayout());
		JLabel insertCardIcon = new JLabel(insertImgResized);
		JLabel insertCardLabel = new JLabel("Insert Card", SwingConstants.CENTER);
		insertCardLabel.setFont(new Font("Arial", Font.BOLD, 22));
		insertCard.add(insertCardLabel, BorderLayout.CENTER);
		insertCard.add(insertCardIcon, BorderLayout.WEST);
		insertCard.setSize(buttonSize);
		insertCard.setPreferredSize(buttonSize);
		insertCard.setMinimumSize(buttonSize);
		insertCard.setMaximumSize(buttonSize);
		insertCard.addActionListener(this);
		JPanel insertCardPanel = new JPanel();
		insertCardPanel.add(insertCard);
		payPanel.add(insertCardPanel);

		// image for entering pin downloaded from website below
		// https://www.pngkey.com/download/u2w7t4q8o0u2i1t4_clip-art-royalty-free-download-pad-icon-free/
		ImageIcon pin = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/enter pin.png");
		Image pinImg = pin.getImage();
		Image newPinImg = pinImg.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
		ImageIcon pinImgResized = new ImageIcon(newPinImg);

		enterPin = new JButton();
		enterPin.setLayout(new BorderLayout());
		JLabel enterPinIcon = new JLabel(pinImgResized);
		JLabel enterPinLabel = new JLabel("Enter Pin", SwingConstants.CENTER);
		enterPinLabel.setFont(new Font("Arial", Font.BOLD, 22));
		enterPin.add(enterPinLabel, BorderLayout.CENTER);
		enterPin.add(enterPinIcon, BorderLayout.WEST);
		enterPin.setSize(buttonSize);
		enterPin.setPreferredSize(buttonSize);
		enterPin.setMinimumSize(buttonSize);
		enterPin.setMaximumSize(buttonSize);
		enterPin.addActionListener(this);
		JPanel enterPinPanel = new JPanel();
		enterPinPanel.add(enterPin);
		payPanel.add(enterPinPanel);

		buttonPanel.add(banknotePanel);
		buttonPanel.add(coinPanel);

		middlePanel.add(actionsPanel);
		middlePanel.add(addWeightPanel);
		middlePanel.add(removeWeightPanel);
		middlePanel.add(payPanel);
		middlePanel.add(banknotePanel);
		middlePanel.add(coinPanel);

		mainPanel.add(topPanel);
		mainPanel.add(middlePanel);

		mainPanel.add(topPanel);
		mainPanel.add(middlePanel);

		frame.setContentPane(mainPanel);
		frame.pack();
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
	}

	/**
	 * Receives button click events and forwards it to the checkout
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		JButton button = (JButton) arg0.getSource();

		// for inserting banknotes
		if (button == insert5) {

		} else if (button == insert10) {

		} else if (button == insert20) {

		} else if (button == insert50) {

		} else if (button == insert100) {

		}
		// for inserting coins
		else if (button == insertNickel) {

		} else if (button == insertDime) {

		} else if (button == insertQuarter) {

		} else if (button == insertLoonie) {

		} else if (button == insertTwoonie) {

		}

		// action items at checkout
		else if (button == scanToy || button == scanPlayStation || button == scanPiano) {

			try {
				if (button == scanToy) {
					checkout.scanItem(toy);
				} else if (button == scanPlayStation) {
					checkout.scanItem(ps6);
				} else {
					checkout.scanItem(piano);
				}
				stateHandler.notifyDataUpdate(new ListProductStateData(checkout.getProductsAdded()));
			} catch (CheckoutException e) {
				if (checkout.isPaused()) {
					if (checkout.expectedWeightOnBaggingArea > checkout.getWeightOnBaggingArea()) {
						stateHandler.notifyDataUpdate(new BaggingAreaWeightData(-1));
					} else {
						stateHandler.notifyDataUpdate(new BaggingAreaWeightData(1));
					}
				} else {
					stateHandler.notifyDataUpdate(new BaggingAreaWeightData(0));
				}
			}
		} else if (button == bagToy || button == bagPlayStation || button == bagPiano) {
			try {
				if (button == bagToy) {
					if (bagToyLabel.getText().startsWith("Bag")) {
						bagToyLabel.setText("Unbag Toy");
						checkout.addItemToBaggingArea(toy);
					} else {
						checkout.removeItemFromBaggingArea(toy);
						bagToyLabel.setText("Bag Toy");
					}

				} else if (button == bagPlayStation) {
					if (bagPlayStationLabel.getText().startsWith("Bag")) {
						bagPlayStationLabel.setText("Unbag PlayStation");
						checkout.addItemToBaggingArea(ps6);
					} else {
						checkout.removeItemFromBaggingArea(ps6);
						bagPlayStationLabel.setText("Bag PlayStation");
					}
				} else {
					if (bagPianoLabel.getText().startsWith("Bag")) {
						bagPianoLabel.setText("Unbag Piano");
						checkout.addItemToBaggingArea(piano);
					} else {
						checkout.removeItemFromBaggingArea(piano);
						bagPianoLabel.setText("Bag Piano");
					}
				}

			} catch (OverloadException | CheckoutException e) {
				
			}
			if (checkout.isPaused()) {
				if (checkout.expectedWeightOnBaggingArea > checkout.getWeightOnBaggingArea()) {
					stateHandler.notifyDataUpdate(new BaggingAreaWeightData(-1));
				} else {
					stateHandler.notifyDataUpdate(new BaggingAreaWeightData(1));
				}
			} else {
				stateHandler.notifyDataUpdate(new BaggingAreaWeightData(0));
			}
			//} else if(button == bagPlay) {

			// add weight to scale
		} else if (button == addApples) {
			checkout.addItemToScale(apples);
			System.out.println(checkout.getWeightOnScale());
			stateHandler.notifyDataUpdate(new ScaleStateData(checkout.getWeightOnScale()));
		} else if (button == addPears) {
			weight += 0.005;

		} else if (button == add10g) {
			weight += 0.010;

		} else if (button == add20g) {
			weight += 0.20;

		} else if (button == add50g) {
			weight += 0.050;

		} else if (button == add100g) {
			weight += 0.100;

		}

		// remove weight from scale
		else if (button == minus1g) {
			weight -= 0.001;

		} else if (button == minus5g) {
			weight -= 0.005;

		} else if (button == minus10g) {
			weight -= 0.010;

		} else if (button == minus20g) {
			weight -= 0.020;

		} else if (button == minus50g) {
			weight -= 0.050;

		} else if (button == minus100g) {
			weight -= 0.100;

		}

		else if (button == goBack) {
		}

		// for using pin pad
		else if (button == swipeCard) {

		} else if (button == tapCard) {

		} else if (button == insertCard) {

		} else if (button == enterPin) {

		}

	}

}
