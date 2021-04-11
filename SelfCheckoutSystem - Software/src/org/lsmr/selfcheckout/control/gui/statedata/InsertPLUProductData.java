package org.lsmr.selfcheckout.control.gui.statedata;

import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;
import org.lsmr.selfcheckout.products.Product;

public class InsertPLUProductData implements StateData<PLUCodedProduct> {

	private PLUCodedProduct p;

	public InsertPLUProductData(PLUCodedProduct p) {
		this.p = p;
	}

	@Override
	public PLUCodedProduct obtain() {
		return p;
	}

}
