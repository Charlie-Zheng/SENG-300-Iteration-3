package org.lsmr.selfcheckout.control.gui.statedata;

public class FloatStateData implements StateData<Float> {

	private float value;

	public FloatStateData(float value) {
		this.value = value;
	}

	@Override
	public Float obtain() {
		return value;
	}

}
