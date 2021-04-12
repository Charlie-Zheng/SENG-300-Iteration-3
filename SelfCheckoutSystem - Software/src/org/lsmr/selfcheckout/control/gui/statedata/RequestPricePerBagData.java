package org.lsmr.selfcheckout.control.gui.statedata;

public class RequestPricePerBagData implements StateData<Float> {

	private float cost;

	public RequestPricePerBagData(float value) {
		cost = value;
	}

	@Override
	public Float obtain() {
		return cost;
	}

}
