package org.lsmr.selfcheckout.control.gui.statedata;

import org.lsmr.selfcheckout.control.Checkout;
import org.lsmr.selfcheckout.control.Checkout.PayingState;

public class PurchasingStateData implements StateData<Checkout.PayingState> {

	private PayingState state;

	public PurchasingStateData(PayingState state) {
		this.state = state;
	}

	@Override
	public PayingState obtain() {
		return state;
	}
	

}
