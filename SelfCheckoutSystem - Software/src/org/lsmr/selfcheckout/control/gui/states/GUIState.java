package org.lsmr.selfcheckout.control.gui.states;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.lsmr.selfcheckout.control.gui.StateHandler;
import org.lsmr.selfcheckout.control.gui.statedata.StateData;

public interface GUIState {
	/**
	 * Creates a GUI with the given state controller and previous state contents
	 * @param stateController
	 * @param reducedState
	 */
	public void init(StateHandler<GUIState> stateController, ReducedState reducedState);
	public void onDataUpdate(StateData<?> data);
	public JPanel getPanel();
	public ReducedState reduce();
}
