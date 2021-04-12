package org.lsmr.selfcheckout.control.test;

import static org.junit.Assert.*;

import java.util.Currency;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.control.Checkout;
import org.lsmr.selfcheckout.control.Checkout.PayingState;
import org.lsmr.selfcheckout.control.CheckoutException;
import org.lsmr.selfcheckout.devices.OverloadException;

public class TestCustomerCases extends BaseTest {
	
	//protected static String BASEDIR = "C:";

	//protected int totalTests;
	//protected int successfulTests;
	//protected final int REPEAT = 100;
	//protected Checkout c;
	
	/**
	 * - Customer returns to adding items (cancelPayment())
	 * - Customer does not want to bag a scanned item (doNotBagLastItem())
	 * - Customer looks up product (searchProductDatabase(String name))
	 * - Customer enters PLU code for a product (enterPLUCode(PriceLookupCode code))
	 * - Customer enters their membership card information (enterMembershipCardInfo(String number))
	 */
	
	/**
	 * 
	 * @throws CheckoutException
	 * @throws OverloadException
	 */
	@Test
	public void testCancelPaymentCashNormal() throws CheckoutException, OverloadException {
		Barcode b = new Barcode("12345");
		BarcodedItem i = new BarcodedItem(b, 123);
		c.scanItem(i);
		c.addItemToBaggingArea(i);
		c.startPayment(PayingState.Cash);
		c.cancelPayment();
	}
	/**
	 * 
	 * @throws CheckoutException
	 * @throws OverloadException
	 */
	@Test (expected = CheckoutException.class)
	public void testCancelPaymentCashPaid() throws CheckoutException, OverloadException {
		Barcode b = new Barcode("12345");
		BarcodedItem i = new BarcodedItem(b, 123);
		c.scanItem(i);
		c.addItemToBaggingArea(i);
		c.startPayment(PayingState.Cash);
		Banknote banknote = new Banknote(10, Currency.getInstance("CAD"));
		c.payWithBanknote(banknote);
		c.cancelPayment();
	}
	/**
	 * 
	 * @throws CheckoutException
	 * @throws OverloadException
	 */
	@Test
	public void testCancelPaymentCredit() throws CheckoutException, OverloadException {
		Barcode b = new Barcode("12345");
		BarcodedItem i = new BarcodedItem(b, 123);
		c.scanItem(i);
		c.addItemToBaggingArea(i);
		c.startPayment(PayingState.Credit);
		c.cancelPayment();
		assertTrue(c.getState().equals("Scanning"));
	}

}
