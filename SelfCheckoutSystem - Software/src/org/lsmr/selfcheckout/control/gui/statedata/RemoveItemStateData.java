package org.lsmr.selfcheckout.control.gui.statedata;

public class RemoveItemStateData implements StateData<Integer> {

	private int index;

	public RemoveItemStateData(int index) {
		this.index = index;
	}

	@Override
	public Integer obtain() {
		return index;
	}
}
