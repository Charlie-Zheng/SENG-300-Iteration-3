package org.lsmr.selfcheckout.control.test;

import java.math.BigDecimal;

import org.junit.Test;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Card;
import org.lsmr.selfcheckout.PLUCodedItem;
import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.control.CardIssuerDatabase;
import org.lsmr.selfcheckout.control.Checkout.PayingState;
import org.lsmr.selfcheckout.control.CheckoutException;
import org.lsmr.selfcheckout.control.ReceiptItem;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.ReceiptPrinter;
import org.lsmr.selfcheckout.external.CardIssuer;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.Product;

/**
 * @author Group 08
 * @date April 14, 2021
 */

public class PaperInkTest extends BaseTest {
	/**
	 * Test whether or not the ink low is configured correctly
	 * Loads 50 of ink (1048576 max, 10% as low) should be true
	 */
	@Test
	public void InkLowTest() {
		for (int i = 0; i < REPEAT; i++) {
			c.reset();
			c.addInk(50);
			multiTestAssertEquals(true, c.isInkLow());
		}
	}
	/**
	 * Test whether or not the ink low is configured correctly
	 * Loads 300 of ink (1048576 max, 10% as low) should be false
	 * @throws CheckoutException 
	 * @throws OverloadException 
	 */
	@Test
	public void InkLowFailTest() throws CheckoutException, OverloadException {
			c.reset();
			c.addInk(110000);
			multiTestAssertEquals(false, c.isInkLow());
	}
	/**
	 * Test whether or not the paper low is configured correctly
	 * Loads 50 of paper (1024 max, 10% as low)
	 * @throws CheckoutException
	 */
	@Test
	public void PaperLowTest() throws CheckoutException {
		for (int i = 0; i < REPEAT; i++) {
			c.reset();
			c.addPaper(1);
			multiTestAssertEquals(true, c.isPaperLow());
		}
	}
	/**
	 * Test whether or not the paper low is configured correctly
	 * Loads 150 of paper (1024 max, 10% as low) 
	 * @throws OverloadException 
	 */
	@Test
	public void PaperLowFailTest() throws OverloadException {
			c.reset();
			c.addPaper(150);
			multiTestAssertEquals(false, c.isPaperLow());
		}
	
	/**
	 * Test whether or not the receipt printer works as intended
	 * Correctly loaded paper, ink and receipt information allows 
	 * the software to print a receipt (containing 1 barcoded item
	 * and 1 PLU item)
	 * 
	 */
	@Test
	public void ReceiptPrintNormalTest() {
		for (int i = 0; i < REPEAT; i++) {
			c.reset();
			Card card = new Card("gift", "1111222233334444", "John Doe", "123", "0909", true, true);
			CardIssuer issuer = new CardIssuer("abcdefg");
			issuer.addCardData("1111222233334444", "John Doe", getCalendar(12, 2023), "123", new BigDecimal(999999));
			CardIssuerDatabase.GIFT_ISSUER_DATABASE.add(issuer);
			c.addPaper(10);
			c.addInk(100);
			try {
				BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);
				c.scanItem(item);
				c.addItemToBaggingArea(item);
				c.enterPLUCode(new PriceLookupCode("12345"));
				c.startPayment(PayingState.Gift);
				c.payByTappingCard(card);
				c.printReceipt();
				c.removeReceipt();
				success();
			} catch (OverloadException | CheckoutException e) {
				fail();
			}

			CardIssuerDatabase.CREDIT_ISSUER_DATABASE.clear();
			CardIssuerDatabase.DEBIT_ISSUER_DATABASE.clear();
			CardIssuerDatabase.GIFT_ISSUER_DATABASE.clear();			
		}
	}
	
	/**
	 * Test whether or not the receipt printer works as intended
	 * Correctly loaded paper, ink, but is still scanning
	 * @throws CheckoutException 
	 */
	@Test (expected = CheckoutException.class)
	public void StillScanningReceiptPrintTest() throws CheckoutException {
		for (int i = 0; i < REPEAT; i++) {
			c.reset();
			c.addPaper(10);
			c.addInk(100);
			c.printReceipt();
		}
	}

	
}
