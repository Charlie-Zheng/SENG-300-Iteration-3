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
	
	/**
	 * Called when the Checkout class wants to send data to the state
	 * @param data
	 */
	public void onDataUpdate(StateData<?> data);
	
	/**
	 * Returns the view to put on the JFrame
	 * @return
	 */
	public JPanel getPanel();
	
	/**
	 * Reduces the state to it's bare minimum to give to the next state
	 * @return the reduced state
	 */
	public ReducedState reduce();
}
