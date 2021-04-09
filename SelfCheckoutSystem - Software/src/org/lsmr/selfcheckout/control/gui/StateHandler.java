package org.lsmr.selfcheckout.control.gui;

import java.util.ArrayList;

import org.lsmr.selfcheckout.control.gui.statedata.StateData;

public abstract class StateHandler<T> {
	private final ArrayList<StateUpdateListener> listeners = new ArrayList<StateUpdateListener>();
	
	public abstract void setState(T state);
	
	public void addStateUpdateListener(StateUpdateListener stateUpdateListener) {
		listeners.add(stateUpdateListener);
	}
	
	public void notifyListeners(StateData<?> stateData) {
		for (StateUpdateListener listener : listeners) {
			listener.onStateUpdate(stateData);
		}
	}
	
	public static interface StateUpdateListener {
		public void onStateUpdate(StateData<?> stateData);
	}
}
