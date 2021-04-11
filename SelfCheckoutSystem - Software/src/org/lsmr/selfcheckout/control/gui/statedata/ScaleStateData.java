package org.lsmr.selfcheckout.control.gui.statedata;

public class ScaleStateData implements StateData<Float> {
	
	private float weight;
	
	public ScaleStateData(float weight) {
		this.weight = weight;
	}

	@Override
	public Float obtain() {
		return weight;
	}

}
