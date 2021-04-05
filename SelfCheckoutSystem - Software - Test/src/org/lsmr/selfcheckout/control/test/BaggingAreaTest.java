/**
 * 
 */
package org.lsmr.selfcheckout.control.test;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Test;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.control.Checkout;
import org.lsmr.selfcheckout.control.CheckoutException;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;

/**
 * @author Group U08-2
 * @date Mar 31, 2021
 */
public class BaggingAreaTest extends BaseTest {
	/**
	 * Creates default checkout and scans items, testing that getBalance returns
	 * expected balances.
	 * 
	 * @throws OverloadException
	 * @throws CheckoutException
	 */
	@Test
	public void testGetBalance() throws OverloadException {
		for (int i = 0; i < REPEAT; i++) {
			try {
				Checkout c = makeNewDefaultCheckout();
				multiTestAssertEquals(new BigDecimal(0), c.getBalance());

				// barcode 12345 -> price $123.45
				BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);
				c.scanItemUntilSuccessful(item);
				c.addItemToBaggingArea(item);
				BigDecimal expected = new BigDecimal(12345).divide(new BigDecimal(100));
				multiTestAssertEquals(expected, c.getBalance());

				// barcode 30040321 -> price $3.97
				item = new BarcodedItem(new Barcode("30040321"), 397);
				c.scanItemUntilSuccessful(item);
				c.addItemToBaggingArea(item);
				expected = new BigDecimal(12345 + 397).divide(new BigDecimal(100));
				multiTestAssertEquals(expected, c.getBalance());
			} catch (CheckoutException e) {
				fail();
			}
		}
	}

	/**
	 * Adds two items with valid weights to bagging area.
	 * 
	 * @throws OverloadException
	 */
	@Test
	public void testAddItemToBaggingArea() throws OverloadException {
		for (int i = 0; i < REPEAT; i++) {
			try {
				Checkout c = makeNewDefaultCheckout();
				multiTestAssertEquals(new BigDecimal(0), c.getBalance());

				// barcode 12345 -> price $123.45
				BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);
				c.scanItemUntilSuccessful(item);
				c.addItemToBaggingArea(item);

				// barcode 30040321 -> price $3.97
				item = new BarcodedItem(new Barcode("30040321"), 397);
				c.scanItemUntilSuccessful(item);
				c.addItemToBaggingArea(item);
			} catch (CheckoutException e) {
				fail();
			}
		}

	}

	/**
	 * Adds overweight item to bagging area.
	 * 
	 * @throws OverloadException
	 * @throws CheckoutException
	 */
	@Test(expected = OverloadException.class)
	public void testAddItemToBaggingAreaOverWeight() throws OverloadException, CheckoutException {
		Currency cad = Currency.getInstance("CAD");
		int[] banknoteDenominations = { 5, 10, 20, 50, 100 };
		BigDecimal[] coinDenominations = { new BigDecimal("0.05"), new BigDecimal("0.10"), new BigDecimal("0.25"),
				new BigDecimal("1.00"), new BigDecimal("2.00") };
		SelfCheckoutStation station = new SelfCheckoutStation(cad, banknoteDenominations, coinDenominations, 122, 1);
		Checkout c = new Checkout(station);

		// initial balance is 0
		multiTestAssertEquals(new BigDecimal(0), c.getBalance());
		BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);
		c.scanItemUntilSuccessful(item);
		c.addItemToBaggingArea(item);

	}

	/**
	 * Creates default empty checkout, then adds two items to bagging area,
	 * asserting that getWeightOnScale returns correct output within a margin of
	 * error.
	 * 
	 * @throws OverloadException
	 */
	@Test
	public void testGetWeightOnScale() throws OverloadException {
		for (int i = 0; i < REPEAT; i++) {
			try {
				Checkout c = makeNewDefaultCheckout();

				// initial balance is 0
				multiTestAssertEquals(new BigDecimal(0), c.getBalance());

				// barcode 12345 -> price $123.45
				BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);

				c.scanItemUntilSuccessful(item);
				c.addItemToBaggingArea(item);

				multiTestAssertEquals(123, c.getWeightOnScale(), 0.00001);

				// barcode 30040321 -> price $3.97
				item = new BarcodedItem(new Barcode("30040321"), 397);

				c.scanItemUntilSuccessful(item);
				c.addItemToBaggingArea(item);

				// 123+397=520
				multiTestAssertEquals(520, c.getWeightOnScale(), 0.00001);
			} catch (CheckoutException e) {
				fail();
			}
		}
	}

	/**
	 * Adds overweight item to bagging area, then calls getWeightOnScale, expecting
	 * it to return a NaN
	 * 
	 * @throws OverloadException
	 * @throws CheckoutException
	 */
	@Test
	public void testGetWeightOnScaleOverweight() throws CheckoutException {
		for (int i = 0; i < REPEAT; i++) {

			Currency cad = Currency.getInstance("CAD");
			int[] banknoteDenominations = { 5, 10, 20, 50, 100 };
			BigDecimal[] coinDenominations = { new BigDecimal("0.05"), new BigDecimal("0.10"), new BigDecimal("0.25"),
					new BigDecimal("1.00"), new BigDecimal("2.00") };
			SelfCheckoutStation station = new SelfCheckoutStation(cad, banknoteDenominations, coinDenominations, 122,
					1);
			Checkout c = new Checkout(station);


			BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);
			c.scanItemUntilSuccessful(item);

			try {
				c.addItemToBaggingArea(item);
			} catch (OverloadException e) {

			}
			multiTestAssertEquals(true, Double.isNaN(c.getWeightOnScale()));

		}

	}

	/**
	 * Checks when customer fails to place item in bagging area
	 * 
	 * @throws OverloadException
	 */
	@Test
	public void testFailedToPlaceItemInBaggingArea() throws OverloadException {
		for (int i = 0; i < REPEAT; i++) {
			try {
				Checkout c = makeNewDefaultCheckout();
				BarcodedItem item1 = new BarcodedItem(new Barcode("30040321"), 397);
				BarcodedItem item2 = new BarcodedItem(new Barcode("12345"), 123);

				c.scanItemUntilSuccessful(item1);
				//scan item without adding another
				c.scanItemUntilSuccessful(item2);
				fail();
			} catch (CheckoutException e) {
				success();
			}
		}

		for (int i = 0; i < REPEAT; i++) {
			try {
				Checkout c = makeNewDefaultCheckout();
				BarcodedItem item1 = new BarcodedItem(new Barcode("30040321"), 397);
				BarcodedItem item2 = new BarcodedItem(new Barcode("12345"), 123);

				c.scanItemUntilSuccessful(item1);

				// adds an item with the wrong weight to the bagging area
				c.addItemToBaggingArea(item2);
				c.scanItemUntilSuccessful(item2);
				fail();
			} catch (CheckoutException e) {
				success();
			}
		}
	}

	/**
	 * Checks to see if state is paused when previous item not placed in bagging
	 * area
	 * 
	 * @throws OverloadException
	 * @throws CheckoutException
	 */
	@Test
	public void ifFailedToBagItemSystemPaused() throws OverloadException, CheckoutException {
		for (int i = 0; i < REPEAT; i++) {
			Checkout c = makeNewDefaultCheckout();
			BarcodedItem item1 = new BarcodedItem(new Barcode("30040321"), 397);

			c.scanItemUntilSuccessful(item1);
			multiTestAssertEquals(true, c.isPaused());

		}
	}

	/**
	 * Checks to see if state is changed back to scanning when previous item that
	 * was not placed in bagging area is now placed in the bagging area
	 * 
	 * @throws OverloadException
	 * @throws CheckoutException
	 */
	@Test
	public void ifStateChangedFromPausedToScanning() {
		for (int i = 0; i < REPEAT; i++) {
			try {
				Checkout c = makeNewDefaultCheckout();
				BarcodedItem item1 = new BarcodedItem(new Barcode("12345"), 123);

				c.scanItemUntilSuccessful(item1);
				multiTestAssertEquals(true, c.isPaused());

				c.addItemToBaggingArea(item1);
				multiTestAssertEquals(true, c.isScanning());
			} catch (CheckoutException | OverloadException e) {
				fail();
			}

		}
	}

	/**
	 * Checks to see if scale weight is zero when no items are scanned
	 * 
	 * @throws OverloadException
	 */
	@Test
	public void testWeightOnScaleNoItems() throws OverloadException {
		for (int i = 0; i < REPEAT; i++) {
			Checkout c = makeNewDefaultCheckout();

			multiTestAssertEquals(0, c.getWeightOnScale(), 0.0001);
		}
	}
}
