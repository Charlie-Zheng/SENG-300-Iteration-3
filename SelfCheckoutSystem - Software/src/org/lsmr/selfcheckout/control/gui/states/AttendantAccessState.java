package org.lsmr.selfcheckout.control.gui.states;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GrayFilter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.lsmr.selfcheckout.control.ProductTableModel;
import org.lsmr.selfcheckout.control.ReceiptItem;
import org.lsmr.selfcheckout.control.gui.StateHandler;
import org.lsmr.selfcheckout.control.gui.statedata.ListProductStateData;
import org.lsmr.selfcheckout.control.gui.statedata.AttendantStateData;
import org.lsmr.selfcheckout.control.gui.statedata.ScannedItemsRequestData;
import org.lsmr.selfcheckout.control.gui.statedata.StateData;
import org.omg.CORBA.Request;

public class AttendantAccessState implements GUIState, ActionListener {

	private StateHandler<GUIState> stateController;

	private ProductTableModel tableModel = new ProductTableModel();

	private JTable scannedTable;
	private JButton start;
	private JButton shutDown;
	private JButton block;
	private JButton unblock;
	private JButton look;
	private JButton approveWeight;
	private JButton removeItem;
	private JButton logOut;
	private JButton goBack;

	@Override
	public void init(StateHandler<GUIState> stateController, ReducedState reducedState) {
		this.stateController = stateController;

	}


	@Override
	public JPanel getPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 50)); // for margins
		//mainPanel.setLayout(new BorderLayout());
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		mainPanel.setBackground(Color.RED.darker());


		// set up statement at top of screen
		JPanel topPanel = new JPanel();
		//topPanel.setLayout(new BorderLayout());
		topPanel.setBackground(Color.RED.darker());
		JLabel topStatement = new JLabel("Choose an Action");
		topStatement.setForeground(Color.WHITE);
		topStatement.setFont(new Font("Arial", Font.BOLD, 60));
		topPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 0, 30));
		topPanel.add(topStatement);


		// ----- center panel of layout ----
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridConstraints = new GridBagConstraints(); // to align views
		centerPanel.setBackground(Color.RED.darker());

		JComponent scannedTable = getProductPanel(new Dimension(650, 500), 50); 
		gridConstraints.gridx = 0;
		gridConstraints.gridy = 1;
		gridConstraints.weighty = 0.9;
		centerPanel.add(new JScrollPane(scannedTable), gridConstraints);
		// ---- checkout button panel of layout ------

		// dollar sign image downloaded from below website
		// https://www.cleanpng.com/png-dollar-sign-united-states-dollar-symbol-us-dollars-1327518/download-png.html		
		JPanel checkoutButtonPanel = new JPanel();
		checkoutButtonPanel.setLayout(new BorderLayout());
		checkoutButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 20));
		checkoutButtonPanel.setBackground(Color.RED.darker());

		JPanel buttonLayout = new JPanel();
		buttonLayout.setLayout(new BoxLayout(buttonLayout, BoxLayout.Y_AXIS));
		buttonLayout.setBackground(Color.RED.darker());

		Dimension buttonSize = new Dimension(300, 45);

		JPanel startPanel = new JPanel();
		startPanel.setBackground(Color.RED.darker());
		start = new JButton("Start Station");

		start.setFont(new Font("Arial", Font.BOLD, 30));
		start.addActionListener(this);	
		start.setSize(buttonSize);
		start.setPreferredSize(buttonSize);
		start.setMinimumSize(buttonSize);
		start.setMaximumSize(buttonSize);
		startPanel.add(start);
		buttonLayout.add(startPanel);
		buttonLayout.add(newSpacing(0, 10));


		// jpanel with gridlayout row: 1, col: 2, then add jpanel to buttonLayout
		JPanel shutDownPanel = new JPanel();
		shutDownPanel.setBackground(Color.RED.darker());
		shutDown = new JButton("Shut Down Station");

		shutDown.setFont(new Font("Arial", Font.BOLD, 30));
		shutDown.addActionListener(this);
		shutDown.setSize(buttonSize);
		shutDown.setPreferredSize(buttonSize);
		shutDown.setMinimumSize(buttonSize);
		shutDown.setMaximumSize(buttonSize);
		shutDownPanel.add(shutDown);
		buttonLayout.add(shutDownPanel);
		buttonLayout.add(newSpacing(0, 10));

		// magnifying glass image downloaded from below website
		// https://www.cleanpng.com/png-magnifying-glass-light-clip-art-loupe-650531/download-png.html		
		JPanel lookPanel = new JPanel();
		lookPanel.setBackground(Color.RED.darker());
		ImageIcon lookUp = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/magnifying glass.png");
		Image lookImg = lookUp.getImage() ;  
		Image newLookImg = lookImg.getScaledInstance( 40, 40,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon lookImgResized = new ImageIcon(newLookImg);

		look = new JButton();
		look.setLayout(new BorderLayout());
		JLabel lookIcon = new JLabel(lookImgResized);
		JLabel lookLabel = new JLabel("Look Up Item", SwingConstants.CENTER);
		lookLabel.setFont(new Font("Arial", Font.BOLD, 30));
		look.add(lookLabel, BorderLayout.CENTER);
		look.add(lookIcon, BorderLayout.WEST);
		look.addActionListener(this);

		look.setSize(buttonSize);
		look.setPreferredSize(buttonSize);
		look.setMinimumSize(buttonSize);
		look.setMaximumSize(buttonSize);
		lookPanel.add(look);
		buttonLayout.add(lookPanel);
		buttonLayout.add(newSpacing(0, 10));

		JPanel weightPanel = new JPanel();
		weightPanel.setBackground(Color.RED.darker());
		approveWeight = new JButton("Approve Weight Discrepancy");
		approveWeight.setFont(new Font("Arial", Font.BOLD, 18));
		approveWeight.addActionListener(this);
		approveWeight.setSize(buttonSize);
		approveWeight.setPreferredSize(buttonSize);
		approveWeight.setMinimumSize(buttonSize);
		approveWeight.setMaximumSize(buttonSize);
		weightPanel.add(approveWeight);
		buttonLayout.add(weightPanel);
		buttonLayout.add(newSpacing(0, 10));

		JPanel removeItemPanel = new JPanel();
		removeItemPanel.setBackground(Color.RED.darker());
		removeItem = new JButton("Remove Item");

		removeItem.setFont(new Font("Arial", Font.BOLD, 30));
		removeItem.addActionListener(this);
		removeItem.setSize(buttonSize);
		removeItem.setPreferredSize(buttonSize);
		removeItem.setMinimumSize(buttonSize);
		removeItem.setMaximumSize(buttonSize);
		removeItem.setEnabled(false);
		removeItemPanel.add(removeItem);
		buttonLayout.add(removeItemPanel);
		buttonLayout.add(newSpacing(0, 10));

		JPanel blockPanel = new JPanel();
		blockPanel.setBackground(Color.RED.darker());
		block = new JButton();
		ImageIcon blocK = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/no-sign.png");
		Image blockImg = blocK.getImage() ;  
		Image newBlockImg = blockImg.getScaledInstance( 40, 40,  java.awt.Image.SCALE_SMOOTH) ;  
		Image greyBlockImg = GrayFilter.createDisabledImage(newBlockImg);
		ImageIcon blockImgResized = new ImageIcon(newBlockImg);
		
		block = new JButton();
		block.setLayout(new BorderLayout());
		JLabel blockLabel = new JLabel("Block Station", SwingConstants.CENTER);
		blockLabel.setFont(new Font("Arial", Font.BOLD, 30));
		JLabel blockIcon = new JLabel(blockImgResized);
		block.add(blockLabel, BorderLayout.CENTER);
		block.add(blockIcon, BorderLayout.WEST);
		block.addActionListener(this);
		block.setSize(buttonSize);
		block.setPreferredSize(buttonSize);
		block.setMinimumSize(buttonSize);
		block.setMaximumSize(buttonSize);
		blockPanel.add(block);
		buttonLayout.add(blockPanel);
		buttonLayout.add(newSpacing(0, 10));
		
		JPanel unblockPanel = new JPanel();
		unblockPanel.setBackground(Color.RED.darker());
		unblock = new JButton("Unblock Station");
		unblock.setFont(new Font("Arial", Font.BOLD, 30));
		unblock.addActionListener(this);
		unblock.setSize(buttonSize);
		unblock.setPreferredSize(buttonSize);
		unblock.setMinimumSize(buttonSize);
		unblock.setMaximumSize(buttonSize);
		unblockPanel.add(unblock);
		buttonLayout.add(unblockPanel);
		buttonLayout.add(newSpacing(0, 10));

		JPanel logOutPanel = new JPanel();
		logOutPanel.setBackground(Color.RED.darker());
		logOut = new JButton("Log Out");

		logOut.setFont(new Font("Arial", Font.BOLD, 30));
		logOut.addActionListener(this);
		logOut.setSize(buttonSize);
		logOut.setPreferredSize(buttonSize);
		logOut.setMinimumSize(buttonSize);
		logOut.setMaximumSize(buttonSize);
		logOutPanel.add(logOut);
		buttonLayout.add(logOutPanel);

		buttonLayout.add(newSpacing(0, 10));



		// image of black arrow downloaded from below website
		// https://www.pikpng.com/downpngs/oxJooi_simpleicons-interface-undo-black-arrow-pointing-to-tanda/
		JPanel goBackPanel = new JPanel();
		goBackPanel.setBackground(Color.RED.darker());
		ImageIcon arrow = new ImageIcon("src/org/lsmr/selfcheckout/gui/icons/black arrow.png");
		Image img = arrow.getImage() ;  
		Image newimg = img.getScaledInstance( 40, 40,  java.awt.Image.SCALE_SMOOTH) ;  
		ImageIcon arrowResized = new ImageIcon(newimg);

		goBack = new JButton();
		goBack.setLayout(new BorderLayout());
		goBack.setLayout(new BorderLayout()); //so we can add an icon
		JLabel backIcon = new JLabel(arrowResized);
		JLabel backLabel = new JLabel("Go Back", SwingConstants.CENTER);
		backLabel.setFont(new Font("Arial", Font.BOLD, 30));
		goBack.add(backLabel, BorderLayout.CENTER);
		goBack.add(backIcon, BorderLayout.WEST);
		goBack.addActionListener(this);
		goBack.setSize(buttonSize);
		goBack.setPreferredSize(buttonSize);
		goBack.setMinimumSize(buttonSize);
		goBack.setMaximumSize(buttonSize);
		goBackPanel.add(goBack);
		buttonLayout.add(goBackPanel);



		checkoutButtonPanel.add(buttonLayout, BorderLayout.SOUTH);


		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weighty = 0.2;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		mainPanel.add(topPanel, constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.weighty = 0.8;
		constraints.weightx = 0.6;
		constraints.anchor = GridBagConstraints.CENTER;
		mainPanel.add(centerPanel, constraints);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.weighty = 0.8;
		constraints.weightx = 0.4;
		constraints.anchor = GridBagConstraints.LINE_END;
		mainPanel.add(checkoutButtonPanel, constraints);
		/*	mainPanel.add(topPanel, BorderLayout.PAGE_START);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(checkoutButtonPanel, BorderLayout.LINE_END);
		mainPanel.add(helpPanel, BorderLayout.PAGE_END);
		 */

		stateController.notifyListeners(new ScannedItemsRequestData());

		return mainPanel;
	}

	@Override
	public ReducedState reduce() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDataUpdate(StateData<?> data) {
		// if only java can support functional programming and Maybe types. would be a one-line code
		if (data instanceof ListProductStateData) {
			List<ReceiptItem> items = ((ListProductStateData) data).obtain();
			tableModel.setProductScannedList(items);
		}
	}

	/**
	 * Creates a spacing placeholder to place certain elements at specific locations
	 * @param width the spacing width
	 * @param height the spacing height
	 * @return a new JComponent that acts as a spacer
	 */
	private JComponent newSpacing(int width, int height) {
		JPanel spacing = new JPanel();
		Dimension size = new Dimension(width, height);

		// setting all these makes it work
		spacing.setSize(size);
		spacing.setPreferredSize(size);
		spacing.setMinimumSize(size);
		spacing.setMaximumSize(size);

		return spacing;
	}

	private JComponent getProductPanel(Dimension tableSize, int rowHeight) {
		final int scrollBarWidth = 10;

		// table
		scannedTable = new JTable(tableModel);

		scannedTable.setRowHeight(rowHeight);
		scannedTable.setPreferredScrollableViewportSize(scannedTable.getPreferredSize());
		scannedTable.setFillsViewportHeight(true);
		scannedTable.setPreferredSize(tableSize);
		scannedTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scannedTable.getTableHeader().setReorderingAllowed(false);
		scannedTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 20));
		// should dynamically change font in all columns we add but seeing if
		// code in for loop will work first
		/*Object dce = scannedTable.getDefaultEditor(Object.class);
		if(dce instanceof DefaultCellEditor) {
		    ((DefaultCellEditor) dce).getComponent().setFont(new Font("Arial", Font.PLAIN, 18));
		}*/

		// dynamic, so we only adjust the model and everything should work
		for (int i = 0; i < tableModel.getColumnCount(); i++) {
			// set column width
			float sizeWeight = tableModel.getColumnWidthWeight(i);
			scannedTable.getColumnModel().getColumn(i).setPreferredWidth((int) (tableSize.width * sizeWeight));
			scannedTable.setFont(new Font("Arial", Font.PLAIN, 18));
		}

		scannedTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				removeItem.setEnabled(true);
			}
		});

		// wrap it in a scroll pane to see table headers
		JScrollPane container = new JScrollPane(scannedTable);
		container.setHorizontalScrollBar(null);
		container.getVerticalScrollBar().setPreferredSize(new Dimension(scrollBarWidth, 0)); // want width to be predictable so we know the exact size for the table
		container.setPreferredSize(new Dimension(tableSize.width + scrollBarWidth, tableSize.height));
		return container;
	}


	@Override
	public void actionPerformed(ActionEvent event) {
		JButton button = (JButton) event.getSource();
		
		if (button == start) {
			//turn on checkout
			stateController.setState(new StartState());
		} else if(button == shutDown) {
			Runtime. getRuntime(). exit(0);
			
		} else if(button == block) {
			stateController.setState(new BlockState());
			
		} else if(button == unblock) {
			stateController.setState(new StartState());
			
		} else if (button == look) {
			stateController.setState(new AttendantLookUpState());
			
		} else if(button == approveWeight) {
			stateController.notifyListeners(new AttendantStateData(AttendantStateData.APPROVE_WEIGHT));
			stateController.setState(new BuyingState());
			//logs out attendant
			
		} else if(button == removeItem) {
			//remove selected item from table
			stateController.notifyListeners(new AttendantStateData(AttendantStateData.REMOVE, scannedTable.getSelectedRow()));
			if (scannedTable.getRowCount() - 1 <= 0) {
				removeItem.setEnabled(false);
			}

		} else if(button == logOut) {
			//back to employee log in screen or buying state? or start state?
			stateController.setState(new AttendantLogInState());
			
		} else if(button == goBack) {
			stateController.setState(new AttendantState());
			
		}
	}
}
