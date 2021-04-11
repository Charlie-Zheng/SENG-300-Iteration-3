package org.lsmr.selfcheckout.control.gui.statedata;

public class KeypadStateData implements StateData<Integer> {

	private int number;

	public KeypadStateData(int number) {
		this.number = number;
	}

	@Override
	public Integer obtain() {
		// TODO Auto-generated method stub
		return number;
	}

}
