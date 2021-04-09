package org.lsmr.selfcheckout.control.gui.states;

import javax.swing.JPanel;

import org.lsmr.selfcheckout.control.gui.StateHandler;
import org.lsmr.selfcheckout.control.gui.statedata.StateData;

public abstract class ReducedState implements GUIState {
	
	public abstract Object getData();

	@Override
	public void init(StateHandler<GUIState> stateController, ReducedState reducedState) {
	}

	@Override
	public void onDataUpdate(StateData<?> data) {
	}

	@Override
	public JPanel getPanel() {
		return null;
	}

	@Override
	public ReducedState reduce() {
		return null;
	}

}
