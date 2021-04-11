package org.lsmr.selfcheckout.control.gui.statedata;

public class BooleanStateData implements StateData<Boolean> {

	private boolean value;

	public BooleanStateData(boolean value) {
		this.value = value;
	}

	@Override
	public Boolean obtain() {
		return value;
	}
	
}
