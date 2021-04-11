package org.lsmr.selfcheckout.control.gui.statedata;

import org.lsmr.selfcheckout.products.Product;

public class ProductStateData implements StateData<Product> {

	private Product p;

	public ProductStateData(Product p) {
		this.p = p;
	}

	@Override
	public Product obtain() {
		return p;
	}

}
