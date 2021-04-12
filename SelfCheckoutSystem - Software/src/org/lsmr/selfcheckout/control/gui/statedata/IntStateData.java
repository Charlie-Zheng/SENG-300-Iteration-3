package org.lsmr.selfcheckout.control.gui.statedata;

public class IntStateData implements StateData<Integer> {
	
	private int i;
	
	public IntStateData(int value) {
		i = value;
	}

	@Override
	public Integer obtain() {
		return i;
	}

}
