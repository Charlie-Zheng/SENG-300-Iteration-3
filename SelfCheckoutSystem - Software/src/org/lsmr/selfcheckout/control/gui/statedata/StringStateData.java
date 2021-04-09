package org.lsmr.selfcheckout.control.gui.statedata;

public class StringStateData implements StateData<String> {
	
	private String data;
	
	public StringStateData(String data) {
		this.data = data;
	}

	@Override
	public String obtain() {
		return data;
	}

}
