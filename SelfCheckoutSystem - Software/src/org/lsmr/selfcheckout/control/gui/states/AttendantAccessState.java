package org.lsmr.selfcheckout.control.gui.states;

import java.awt.Color;

import javax.swing.JPanel;

import org.lsmr.selfcheckout.control.gui.StateHandler;
import org.lsmr.selfcheckout.control.gui.statedata.StateData;

public class AttendantAccessState implements GUIState {
	
	private StateHandler<GUIState> stateController;
	
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
		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(Color.CYAN);
		
		return mainPanel;
	}

	@Override
	public ReducedState reduce() {
		// TODO Auto-generated method stub
		return null;
	}

}
