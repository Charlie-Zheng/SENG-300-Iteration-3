package org.lsmr.selfcheckout.control.gui.statedata;

public class ScaleStateData implements StateData<Double> {
	
	private double weight;
	
	public ScaleStateData(double weight) {
		this.weight = weight;
	}

	@Override
	public Double obtain() {
		return weight;
	}

}
