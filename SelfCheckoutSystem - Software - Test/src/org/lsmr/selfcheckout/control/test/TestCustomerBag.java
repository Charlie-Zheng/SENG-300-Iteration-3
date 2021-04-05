package org.lsmr.selfcheckout.control.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.control.Checkout;
import org.lsmr.selfcheckout.control.CheckoutException;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;

public class TestCustomerBag extends BaseTest {

	/**
	 * Adds the bag once to make sure this normal case works
	 * 
	 * @throws OverloadException
	 */
	@Test
	public void testAddCustomerBagOnce() throws CheckoutException, OverloadException {
		Checkout checkout = makeNewDefaultCheckout();
		checkout.addCustomerBag(150);
		assertTrue(checkout.usingCustomerBag());
	}

	/**
	 * Check an exception is properly thrown when bags are added twice
	 * 
	 * @throws OverloadException
	 */
	@Test(expected = CheckoutException.class)
	public void testAddCustomerBagTwice() throws CheckoutException, OverloadException {
		Checkout checkout = makeNewDefaultCheckout();
		checkout.addCustomerBag(150);
		checkout.addCustomerBag(150);
	}

	/**
	 * Test the bag is present on the bagging area scale
	 * 
	 * @throws OverloadException
	 */
	@Test
	public void testAddCustomerBagWeight() throws CheckoutException, OverloadException {
		double weightb = 150;
		Checkout checkout = makeNewDefaultCheckout();
		checkout.addCustomerBag(weightb);
		assertTrue(checkout.usingCustomerBag());
		double weight = checkout.getWeightOnScale();
		assertTrue(weight == weightb);
	}

	/**
	 * Test the bag is present on the bagging area scale with other items
	 * 
	 * @throws OverloadException
	 */
	@Test
	public void testAddCustomerBagWeight2() {
		for (int i = 0; i < REPEAT; i++) {

			try {
				double weightb = 150;

				Checkout checkout = makeNewDefaultCheckout();

				BarcodedItem i1 = new BarcodedItem(new Barcode("12345"), 123);
				BarcodedItem i2 = new BarcodedItem(new Barcode("30040321"), 397);

				checkout.scanItemUntilSuccessful(i1);
				checkout.addItemToBaggingArea(i1);
				checkout.scanItemUntilSuccessful(i2);
				checkout.addItemToBaggingArea(i2);
				checkout.addCustomerBag(weightb);
				multiTestAssertEquals(true, checkout.usingCustomerBag());
				double weight = checkout.getWeightOnScale();
				multiTestAssertEquals((weightb + 123 + 397), weight, 0.001);
			} catch (OverloadException | CheckoutException e) {
				fail();
			}

		}

	}
}
