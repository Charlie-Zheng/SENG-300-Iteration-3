package org.lsmr.selfcheckout.control.test;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.Map;

import org.junit.Test;
import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.control.Checkout;
import org.lsmr.selfcheckout.control.Checkout.PayingState;
import org.lsmr.selfcheckout.control.CheckoutException;
import org.lsmr.selfcheckout.control.GiveChange;
import org.lsmr.selfcheckout.devices.DisabledException;
import org.lsmr.selfcheckout.devices.EmptyException;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.SimulationException;

/**
 * Test suite for Checkout. All tests are run 100 times, and the test passes if
 * the success rate is greater than 75%. This is to account for the possible
 * random failures in the hardware
 * 
 * @author Group U08-2
 * @date Mar 31, 2021
 */
public class CheckoutTest extends BaseTest {

	/**
	 * Creates default checkout, and expects it to be created successfully.
	 */
	@Test
	public void testCheckoutDefault() {
		makeNewDefaultCheckout();
	}
	/**
	 * Creates default checkout, and expects it to be created successfully.
	 */
	@Test
	public void testCheckoutReset() {
		c.reset();
	}
	/**
	 * Tests passing a null self checkout station in and should throw
	 * SimulationException.
	 */
	@Test
	public void testCheckoutNull() {
		for (int i = 0; i < REPEAT; i++) {
			try {
				new Checkout(null);
			} catch (SimulationException e) {
				success();
			}

		}

	}

}
