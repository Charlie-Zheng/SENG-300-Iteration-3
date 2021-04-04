/**
 * 
 */
package org.lsmr.selfcheckout.control.test;

import java.math.BigDecimal;

import org.junit.Test;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.control.Checkout;
import org.lsmr.selfcheckout.control.CheckoutException;
import org.lsmr.selfcheckout.devices.OverloadException;

/**
 * @author Group U08-2
 * @date Mar 31, 2021
 */
public class ScanItemTest extends BaseTest {

	/**
	 * Creates default checkout instance and valid barcoded items. Expects items to
	 * scan successfully, without an exception.
	 * 
	 * @throws CheckoutException
	 * @throws OverloadException
	 */
	@Test
	public void testScanBarcodedItem() throws CheckoutException, OverloadException {
		for (int i = 0; i < REPEAT; i++) {
			Checkout c = makeNewDefaultCheckout();
			BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);
			c.scanItem(item);

		}
		for (int i = 0; i < REPEAT; i++) {
			Checkout c = makeNewDefaultCheckout();
			BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);
			c.scanItem(item);

		}

	}

	/**
	 * Scans barcoded item not in database, expecting it not to change the checkout
	 * balance.
	 */
	@Test
	public void testBarcodedItemNotInDatabaseGetBalance() {
		for (int i = 0; i < REPEAT; i++) {
			try {
				Checkout c = makeNewDefaultCheckout();
				BarcodedItem item = new BarcodedItem(new Barcode("00000"), 123);
				c.scanItem(item);

				BigDecimal expected = new BigDecimal(0);
				multiTestAssertEquals(expected, c.getBalance());
			} catch (CheckoutException e) {
				// TODO: handle exception
			}
		}

	}

	/**
	 * Creates default checkout instance and barcoded item with barcode not found in
	 * BARCODED_PRODUCT_DATABASE. Expects item to scan, without an exception.
	 * 
	 * @throws CheckoutException
	 */
	@Test
	public void testScanBarcodedItemNotInDatabase() throws CheckoutException {
		for (int i = 0; i < REPEAT; i++) {
			Checkout c = makeNewDefaultCheckout();
			BarcodedItem item = new BarcodedItem(new Barcode("00000"), 156);
			c.scanItem(item);
		}

	}

	/**
	 * Creates default checkout instance and item that is not barcoded. Expects item
	 * to scan, without an exception.
	 * 
	 * @throws CheckoutException
	 */
	@Test
	public void testScanNotBarcodedItem() throws CheckoutException {
		for (int i = 0; i < REPEAT; i++) {
			Checkout c = makeNewDefaultCheckout();
			Item item = new Item(156) {
			};
			c.scanItem(item);
		}

	}
}
