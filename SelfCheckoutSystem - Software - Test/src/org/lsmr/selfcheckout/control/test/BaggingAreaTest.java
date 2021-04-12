/**
 * 
 */
package org.lsmr.selfcheckout.control.test;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Test;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Card;
import org.lsmr.selfcheckout.control.CardIssuerDatabase;
import org.lsmr.selfcheckout.control.Checkout;
import org.lsmr.selfcheckout.control.CheckoutException;
import org.lsmr.selfcheckout.control.Checkout.PayingState;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.external.CardIssuer;

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
		for (int i = 0; i < 100; i++) {
			try {
				c.reset();
				multiTestAssertEquals(new BigDecimal(0), c.getBalance());

				// barcode 12345 -> price $123.45
				BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);
				c.scanItem(item);
				c.addItemToBaggingArea(item);
				BigDecimal expected = new BigDecimal(12345).divide(new BigDecimal(100));
				multiTestAssertEquals(expected, c.getBalance());

				// barcode 30040321 -> price $3.97
				item = new BarcodedItem(new Barcode("30040321"), 397);
				c.scanItem(item);
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
				c.reset();
				multiTestAssertEquals(new BigDecimal(0), c.getBalance());

				// barcode 12345 -> price $123.45
				BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);
				c.scanItem(item);
				c.addItemToBaggingArea(item);

				// barcode 30040321 -> price $3.97
				item = new BarcodedItem(new Barcode("30040321"), 397);
				c.scanItem(item);
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
		c.scanItem(item);
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
				c.reset();

				// initial balance is 0
				multiTestAssertEquals(new BigDecimal(0), c.getBalance());

				// barcode 12345 -> price $123.45
				BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);

				c.scanItem(item);
				c.addItemToBaggingArea(item);

				multiTestAssertEquals(123, c.getWeightOnBaggingArea(), 0.00001);

				// barcode 30040321 -> price $3.97
				item = new BarcodedItem(new Barcode("30040321"), 397);

				c.scanItem(item);
				c.addItemToBaggingArea(item);

				// 123+397=520
				multiTestAssertEquals(520, c.getWeightOnBaggingArea(), 0.00001);
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
			c.scanItem(item);

			try {
				c.addItemToBaggingArea(item);
			} catch (OverloadException e) {

			}
			multiTestAssertEquals(true, Double.isNaN(c.getWeightOnBaggingArea()));

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
				c.reset();
				BarcodedItem item1 = new BarcodedItem(new Barcode("30040321"), 397);
				BarcodedItem item2 = new BarcodedItem(new Barcode("12345"), 123);

				c.scanItem(item1);
				//scan item without adding another
				c.scanItem(item2);
				fail();
			} catch (CheckoutException e) {
				success();
			}
		}

		for (int i = 0; i < REPEAT; i++) {
			try {
				c.reset();
				BarcodedItem item1 = new BarcodedItem(new Barcode("30040321"), 397);
				BarcodedItem item2 = new BarcodedItem(new Barcode("12345"), 123);

				c.scanItem(item1);

				// adds an item with the wrong weight to the bagging area
				c.addItemToBaggingArea(item2);
				c.scanItem(item2);
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
			c.reset();
			BarcodedItem item1 = new BarcodedItem(new Barcode("30040321"), 397);

			c.scanItem(item1);
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
				c.reset();
				BarcodedItem item1 = new BarcodedItem(new Barcode("12345"), 123);

				c.scanItem(item1);
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
			c.reset();

			multiTestAssertEquals(0, c.getWeightOnBaggingArea(), 0.0001);
		}
	}
	
	/**
	 * Checks to see if removing purchased items from bagging area works
	 * when the state is Done, correctly changing the state back to 
	 * scanning. 
	 * 
	 * @throws OverloadException
	 */
	@Test
	public void testRemovePurchasedItems() throws OverloadException, CheckoutException {
		for (int i = 0; i < REPEAT; i++) {

			c.reset();
			
			c.doNotPrintReceipt();
			c.removePurchasedItemFromBaggingArea();
			multiTestAssertEquals(true, c.isScanning());
			}		
		}
	
	/**
	 * Checks to see if removing purchased items from bagging area doesn't
	 * work when the state is not Done, and does not change the state
	 * back to scanning
	 * 
	 * @throws OverloadException
	 */
	@Test
	public void testStateRemovePurchasedItems() throws OverloadException {
		for (int i = 0; i < REPEAT; i++) {
				Card card = new Card("debit", "1111222233334444", "John Doe", "123", "0909", true, true);
				CardIssuer issuer = new CardIssuer("abcdefg");
				issuer.addCardData("1111222233334444", "John Doe", getCalendar(12, 2023), "123", new BigDecimal(999999));
				CardIssuerDatabase.DEBIT_ISSUER_DATABASE.add(issuer);
				
				
				BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);

				// Try out all three payment types

				// tap
				c.reset();

				try {
					c.scanItem(item);

					//Has to add item to bagging area first
					c.addItemToBaggingArea(item);
					c.startPayment(PayingState.Debit);
					
				c.removePurchasedItemFromBaggingArea();
				multiTestAssertEquals(0, c.getWeightOnBaggingArea(), 0.0001);
				multiTestAssertEquals(false, c.isScanning());
				c.payByTappingCard(card);
				success();
				} catch (CheckoutException | OverloadException e) {
				fail();
				} 				
				
				CardIssuerDatabase.CREDIT_ISSUER_DATABASE.clear();
				CardIssuerDatabase.DEBIT_ISSUER_DATABASE.clear();
				CardIssuerDatabase.GIFT_ISSUER_DATABASE.clear();
			
				
		}
	}
	/**
	 * To see if the expectedweightinbaggingarea works as intended, 
	 * that the state is changed from scanning to paused. 
	 * @throws CheckoutException
	 */
	@Test 
	public void weightInBaggingAreaNotConformtoExpectationTest() throws OverloadException, CheckoutException {
		for (int i = 0; i < REPEAT; i++) {
			try{
			c.reset();

			BarcodedItem item1 = new BarcodedItem(new Barcode("12345"), 123);
			BarcodedItem item2 = new BarcodedItem(new Barcode("23456"), 10);
			c.scanItem(item1);
			c.addItemToBaggingArea(item2);
			fail();
			}catch (OverloadException | CheckoutException e) {
			multiTestAssertEquals(true, c.isPaused());
			success();
			}
		}
	}
	
}
