/**
 * 
 */
package org.lsmr.selfcheckout.control;

import java.math.BigDecimal;

import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.BarcodeScanner;
import org.lsmr.selfcheckout.devices.listeners.AbstractDeviceListener;
import org.lsmr.selfcheckout.devices.listeners.BarcodeScannerListener;
import org.lsmr.selfcheckout.external.ProductDatabases;
import org.lsmr.selfcheckout.products.BarcodedProduct;

/**
 * @author Group U08-2
 * @date Mar 11, 2021
 */
public class BarcodeScannerUpdateListener implements BarcodeScannerListener {
	private Checkout checkout;

	public BarcodeScannerUpdateListener(Checkout checkout) {
		this.checkout = checkout;
	}

	@Override
	public void enabled(AbstractDevice<? extends AbstractDeviceListener> device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disabled(AbstractDevice<? extends AbstractDeviceListener> device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void barcodeScanned(BarcodeScanner barcodeScanner, Barcode barcode) {
		if(ProductDatabases.BARCODED_PRODUCT_DATABASE.containsKey(barcode)) {
			BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
			//barcoded products are always per unit
			checkout.addBalanceUnit(product.getPrice());
			checkout.addExpectedWeightOnScale(ProductWeightDatabase.PRODUCT_WEIGHT_DATABASE.get(barcode));
			checkout.addBarcodedProductToList(product);
		}
	}

}
