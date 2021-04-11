package org.lsmr.selfcheckout.control.gui.statedata;

import org.lsmr.selfcheckout.products.BarcodedProduct;

public class InsertBarcodedProductData implements StateData<BarcodedProduct>{

	private BarcodedProduct p;

	public InsertBarcodedProductData(BarcodedProduct p) {
		this.p = p;
	}

	@Override
	public BarcodedProduct obtain() {
		return p;
	}

}
