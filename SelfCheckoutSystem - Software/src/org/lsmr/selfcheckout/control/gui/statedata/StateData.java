package org.lsmr.selfcheckout.control.gui.statedata;

/**
 * State data. Used to send data from states to observers or to send data from an outside source
 * to states.
 * @author Vianney Nguyen
 *
 * @param <T>
 */
public interface StateData<T> {
	public T obtain();
}
