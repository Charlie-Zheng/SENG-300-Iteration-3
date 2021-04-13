package org.lsmr.selfcheckout.control.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.control.Attendant;
import org.lsmr.selfcheckout.control.AttendantSystem;
import org.lsmr.selfcheckout.control.Checkout.PayingState;
import org.lsmr.selfcheckout.control.CheckoutException;
import org.lsmr.selfcheckout.control.MembershipCardDatabase;
import org.lsmr.selfcheckout.control.ReceiptItem;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.external.ProductDatabases;
import org.lsmr.selfcheckout.products.PLUCodedProduct;
import org.lsmr.selfcheckout.products.Product;

public class TestCustomerCases extends BaseTest {
	
	public void scanUntilThere(BarcodedItem i) throws CheckoutException {
		c.scanItem(i);
		Product cp = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(i.getBarcode());
		while (true) {
			for (ReceiptItem ri : c.getProductsAdded()) {
				if (ri.product.equals(cp)) {
					return;
				}
			}
			c.scanItem(i);
			System.out.println("Failed scan. Retrying...");
		}
	}
	
	public void triggerPauseState() throws CheckoutException {
		Barcode b = new Barcode("12345");
		BarcodedItem i = new BarcodedItem(b, 103);
		scanUntilThere(i);
	}
	
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
		this.scanUntilThere(i);
		c.addItemToBaggingArea(i);
		c.startPayment(PayingState.Cash);
		c.cancelPayment();
		assertTrue(c.getState().equals("Scanning"));
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
		this.scanUntilThere(i);
		c.addItemToBaggingArea(i);
		c.startPayment(PayingState.Credit);
		c.cancelPayment();
		assertTrue(c.getState().equals("Scanning"));
	}
	/**
	 * 
	 * @throws CheckoutException
	 * @throws OverloadException
	 */
	@Test (expected = CheckoutException.class)
	public void testCancelPaymentNonpay() throws CheckoutException, OverloadException {
		c.cancelPayment();
	}
	/**
	 * 
	 * @throws CheckoutException 
	 */
	@Test
	public void testApproveDoNotBagLastItem() throws CheckoutException {
		Barcode b = new Barcode("12345");
		BarcodedItem i = new BarcodedItem(b, 123);
		this.scanUntilThere(i);
		
		Attendant a = new Attendant(0, "test", 1234);
		HashMap<Integer, Attendant> attendants = new HashMap<>();
		attendants.put(0, a);
		AttendantSystem as = new AttendantSystem(attendants);
		int stationNum = as.register(c);
		as.login(0, 1234);
		
		as.approveDoNotBagLastItem(stationNum);
		assertTrue(c.getState().equals("Scanning"));
	}
	/**
	 * 
	 * @throws CheckoutException 
	 */
	@Test (expected = CheckoutException.class)
	public void testApproveDoNotBagLastItemUnscanned() throws CheckoutException {
		PriceLookupCode plc = new PriceLookupCode("12345");
		c.enterPLUCode(plc);
		
		Attendant a = new Attendant(0, "test", 1234);
		HashMap<Integer, Attendant> attendants = new HashMap<>();
		attendants.put(0, a);
		AttendantSystem as = new AttendantSystem(attendants);
		int stationNum = as.register(c);
		as.login(0, 1234);
		
		as.approveDoNotBagLastItem(stationNum);
	}
	/**
	 * 
	 * @throws CheckoutException 
	 */
	@Test
	public void testSearchProductDatabaseAll() throws CheckoutException {
		ArrayList<Product> p = c.searchProductDatabase("product");
		ProductDatabases.BARCODED_PRODUCT_DATABASE.forEach((barcode, product) -> {
			assertTrue(p.contains(product));
		});
		ProductDatabases.PLU_PRODUCT_DATABASE.forEach((barcode, product) -> {
			assertTrue(p.contains(product));
		});
	}
	/**
	 * 
	 * @throws CheckoutException 
	 */
	@Test
	public void testSearchProductDatabaseSpecific() throws CheckoutException {
		ArrayList<Product> p = c.searchProductDatabase("123");
		Product p12345 = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(new Barcode("12345"));
		Product p12345p = ProductDatabases.PLU_PRODUCT_DATABASE.get(new PriceLookupCode("12345"));
		assertTrue(p.contains(p12345));
		assertTrue(p.contains(p12345p));
	}
	/**
	 * 
	 * @throws CheckoutException 
	 */
	@Test
	public void testEnterPLUCode() throws CheckoutException {
		PriceLookupCode plc = new PriceLookupCode("12345");
		Product product = ProductDatabases.PLU_PRODUCT_DATABASE.get(plc);
		c.enterPLUCode(plc);
		ReceiptItem ri = c.getProductsAdded().get(0);
		assertTrue(ri.product.equals(product));
	}
	/**
	 * 
	 * @throws CheckoutException 
	 */
	@Test (expected = CheckoutException.class)
	public void testEnterPLUCodePaused() throws CheckoutException {
		this.triggerPauseState();
		PriceLookupCode plc = new PriceLookupCode("12345");
		c.enterPLUCode(plc);
	}
	/**
	 * 
	 * @throws CheckoutException 
	 */
	@Test
	public void testEnterMembershipCardInfoNormal() throws CheckoutException {
		String n = "123456789";
		String memberName = MembershipCardDatabase.MEMBERSHIP_CARD_DATABASE.get(n);
		c.enterMembershipCardInfo(n);
		assertTrue(c.getLoggedInMemberName().equals(memberName));
		assertTrue(c.getLoggedInMemberNumber().equals(n));
	}
	/**
	 * 
	 * @throws CheckoutException 
	 */
	@Test (expected = CheckoutException.class)
	public void testEnterMembershipCardInfoTwice() throws CheckoutException {
		String n = "123456789";
		String n2 = "6541236";
		c.enterMembershipCardInfo(n);
		c.enterMembershipCardInfo(n2);
	}
}
