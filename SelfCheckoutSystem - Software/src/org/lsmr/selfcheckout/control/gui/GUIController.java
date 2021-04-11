package org.lsmr.selfcheckout.control.gui;

import javax.swing.JFrame;

import org.lsmr.selfcheckout.control.gui.statedata.StateData;
import org.lsmr.selfcheckout.control.gui.states.BuyingState;
import org.lsmr.selfcheckout.control.gui.states.GUIState;
import org.lsmr.selfcheckout.control.gui.states.ReducedState;

public class GUIController extends StateHandler<GUIState> {
	
	private JFrame frame;
	private GUIState activeState;
	
	
	public GUIController(JFrame frame) {
		this.frame = frame;
	}

	@Override
	public void setState(GUIState state) {
		if (activeState != null) {
			ReducedState reducedState = activeState.reduce();
			activeState = state;
			state.init(this, reducedState);
		} else {
			activeState = state;
			state.init(this, null);
		}
		
		frame.getContentPane().removeAll();
		frame.getContentPane().add(state.getPanel());
		frame.getContentPane().revalidate();
		frame.repaint();
		
	}
	
	/**
	 * Notify the active state of a new data update
	 * @param data
	 */
	public void notifyDataUpdate(StateData<?> data) {
		activeState.onDataUpdate(data);
	}

	
}
