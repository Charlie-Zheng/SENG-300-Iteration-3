package org.lsmr.selfcheckout.control.gui.states;

import javax.swing.JPanel;

import org.lsmr.selfcheckout.control.gui.StateHandler;
import org.lsmr.selfcheckout.control.gui.statedata.StateData;

public class BaseReducedState extends ReducedState {
	
	String data;
	
	public BaseReducedState(String data) {
		this.data = data;
	}
	
	@Override
	public Object getData() {
		return data;
	}

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
