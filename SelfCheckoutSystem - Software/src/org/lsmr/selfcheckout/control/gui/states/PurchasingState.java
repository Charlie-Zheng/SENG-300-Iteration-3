package org.lsmr.selfcheckout.control.gui.states;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.lsmr.selfcheckout.control.gui.StateHandler;
import org.lsmr.selfcheckout.control.gui.statedata.StateData;

public class PurchasingState implements GUIState {
	
	private StateHandler<GUIState> stateController;
	
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
		JPanel payPanel = new JPanel();
		payPanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
		
		final Dimension buttonSize1 = new Dimension(150, 150); // for custom checkout button size

		JButton cash = new JButton("Cash");
		cash.setSize(buttonSize1);
		payPanel.add(cash);
		JButton debit = new JButton("Debit");
		debit.setSize(buttonSize1);
		payPanel.add(debit);
		JButton credit = new JButton("Credit");
		credit.setSize(buttonSize1);
		payPanel.add(credit);
		
		return payPanel;
	}

	@Override
	public ReducedState reduce() {
		// TODO Auto-generated method stub
		return null;
	}

}
