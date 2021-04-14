/**
 * 
 */
package org.lsmr.selfcheckout.control.test;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Test;
import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Card;
import org.lsmr.selfcheckout.control.CardIssuerDatabase;
import org.lsmr.selfcheckout.control.Checkout;
import org.lsmr.selfcheckout.control.CheckoutException;
import org.lsmr.selfcheckout.control.Checkout.PayingState;
import org.lsmr.selfcheckout.devices.DisabledException;
import org.lsmr.selfcheckout.devices.EmptyException;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.external.CardIssuer;

/**
 * @author Group U08-2
 * @date Mar 31, 2021
 */
public class CardPaymentTest extends BaseTest {

	/**
	 * @throws Exception
	 * @Description Assuming a card accepts all three methods of payment (swipe,
	 *              insert, tap), we check that a debit card can do all of these
	 *              transaction methods.
	 *              <p>
	 * @Expected_Outcome The checkout succeeds
	 *                   <p>
	 * @Purpose Verify that a normal debit payment works
	 */
	@Test //TODO This test fails for some reason. I'm not sure why
	public void testNormalDebitCardPayment() {

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
				c.payByTappingCard(card);
				success();
			} catch (CheckoutException | OverloadException e) {
				fail();
			}
			CardIssuerDatabase.CREDIT_ISSUER_DATABASE.clear();
			CardIssuerDatabase.DEBIT_ISSUER_DATABASE.clear();
		}
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
				c.payByInsertingCard(card, "0909");
				success();
			} catch (CheckoutException | OverloadException e) {
				fail();
			}
			CardIssuerDatabase.CREDIT_ISSUER_DATABASE.clear();
			CardIssuerDatabase.DEBIT_ISSUER_DATABASE.clear();
		}
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
				c.payBySwipingCard(card, new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
				success();
			} catch (CheckoutException | OverloadException e) {
				fail();
			}
			CardIssuerDatabase.CREDIT_ISSUER_DATABASE.clear();
			CardIssuerDatabase.DEBIT_ISSUER_DATABASE.clear();
		}
	}

	/**
	 * /
	 * 
	 * @throws Exception
	 *             **
	 * @Description Assuming a card accepts all three methods of payment (swipe,
	 *              insert, tap), we check that a credit card can do all of these
	 *              transaction methods.
	 *              <p>
	 * @Expected_Outcome The checkout succeeds
	 *                   <p>
	 * @Purpose Verify that a normal credit payment works
	 */
	@Test
	public void testNormalCreditCardPayment() throws Exception {

		for (int i = 0; i < REPEAT; i++) {
			Card card = new Card("credit", "1111222233334444", "John Doe", "123", "0909", true, true);
			CardIssuer issuer = new CardIssuer("abcdefg");
			issuer.addCardData("1111222233334444", "John Doe", getCalendar(12, 2023), "123", new BigDecimal(999999));
			CardIssuerDatabase.CREDIT_ISSUER_DATABASE.add(issuer);
			
			
			BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);

			// Try out all three payment types

			// tap
			c.reset();
			
			c.scanItem(item);

			//Has to add item to bagging area first
			try {
				c.addItemToBaggingArea(item);
				c.startPayment(PayingState.Credit);
				c.payByTappingCard(card);
				success();
			} catch (CheckoutException | OverloadException e) {
				fail();
			}
			CardIssuerDatabase.CREDIT_ISSUER_DATABASE.clear();
			CardIssuerDatabase.DEBIT_ISSUER_DATABASE.clear();
		}
		for (int i = 0; i < REPEAT; i++) {
			Card card = new Card("credit", "1111222233334444", "John Doe", "123", "0909", true, true);
			CardIssuer issuer = new CardIssuer("abcdefg");
			issuer.addCardData("1111222233334444", "John Doe", getCalendar(12, 2023), "123", new BigDecimal(999999));
			CardIssuerDatabase.CREDIT_ISSUER_DATABASE.add(issuer);
			
			
			BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);

			// Try out all three payment types

			// tap
			c.reset();
			
			c.scanItem(item);

			//Has to add item to bagging area first
			try {
				c.addItemToBaggingArea(item);
				c.startPayment(PayingState.Credit);
				c.payByInsertingCard(card, "0909");
				success();
			} catch (CheckoutException | OverloadException e) {
				fail();
			}
			CardIssuerDatabase.CREDIT_ISSUER_DATABASE.clear();
			CardIssuerDatabase.DEBIT_ISSUER_DATABASE.clear();
		}
		for (int i = 0; i < REPEAT; i++) {
			Card card = new Card("credit", "1111222233334444", "John Doe", "123", "0909", true, true);
			CardIssuer issuer = new CardIssuer("abcdefg");
			issuer.addCardData("1111222233334444", "John Doe", getCalendar(12, 2023), "123", new BigDecimal(999999));
			CardIssuerDatabase.CREDIT_ISSUER_DATABASE.add(issuer);
			
			
			BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);

			// Try out all three payment types

			// tap
			c.reset();
			
			c.scanItem(item);

			//Has to add item to bagging area first
			try {
				c.addItemToBaggingArea(item);
				c.startPayment(PayingState.Credit);
				c.payBySwipingCard(card, new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
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
	 * @throws Exception
	 * @Description Assuming a card accepts all three methods of payment (swipe,
	 *              insert, tap), we check that a gift card can do all of these
	 *              transaction methods.
	 *              <p>
	 * @Expected_Outcome The checkout succeeds
	 *                   <p>
	 * @Purpose Verify that a normal gift payment works
	 */
	@Test
	public void testNormalGiftCardPayment() {
		
		for (int i = 0; i < REPEAT; i++) {
			Card card = new Card("gift", "1111222233334444", "John Doe", "123", "0909", true, true);
			CardIssuer issuer = new CardIssuer("abcdefg");
			issuer.addCardData("1111222233334444", "John Doe", getCalendar(12, 2023), "123", new BigDecimal(999999));
			CardIssuerDatabase.GIFT_ISSUER_DATABASE.add(issuer);
			
			
			BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);

			// Try out all three payment types
			
			// tap
			c.reset();
			
			
			try {
				c.scanItem(item);
				
				//Has to add item to bagging area first
				
				c.addItemToBaggingArea(item);
				c.startPayment(PayingState.Gift);
				c.payByTappingCard(card);
				success();
			} catch (CheckoutException | OverloadException e) {
				fail();
			}
			CardIssuerDatabase.CREDIT_ISSUER_DATABASE.clear();
			CardIssuerDatabase.DEBIT_ISSUER_DATABASE.clear();
			CardIssuerDatabase.GIFT_ISSUER_DATABASE.clear();
		}
		for (int i = 0; i < REPEAT; i++) {
			Card card = new Card("gift", "1111222233334444", "John Doe", "123", "0909", true, true);
			CardIssuer issuer = new CardIssuer("abcdefg");
			issuer.addCardData("1111222233334444", "John Doe", getCalendar(12, 2023), "123", new BigDecimal(999999));
			CardIssuerDatabase.GIFT_ISSUER_DATABASE.add(issuer);
			// don't add the card to the issuer

			
			BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);

			// Try out all three payment types

			// insert
			c.reset();
			
			
			try {
				c.scanItem(item);
				//Has to add item to bagging area first
				c.addItemToBaggingArea(item);
				c.startPayment(PayingState.Gift);
				c.payByInsertingCard(card, "0909");
				c.removeCard();
				success();
			} catch (OverloadException | CheckoutException e) {
				fail();
			}
			CardIssuerDatabase.CREDIT_ISSUER_DATABASE.clear();
			CardIssuerDatabase.DEBIT_ISSUER_DATABASE.clear();
			CardIssuerDatabase.GIFT_ISSUER_DATABASE.clear();
		}
		for (int i = 0; i < REPEAT; i++) {
			Card card = new Card("gift", "1111222233334444", "John Doe", "123", "0909", true, true);
			CardIssuer issuer = new CardIssuer("abcdefg");
			issuer.addCardData("1111222233334444", "John Doe", getCalendar(12, 2023), "123", new BigDecimal(999999));
			CardIssuerDatabase.GIFT_ISSUER_DATABASE.add(issuer);
			// don't add the card to the issuer

			
			BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);

			// Try out all three payment types

			// swipe
			c.reset();
			
			try {
				c.scanItem(item);
				//Has to add item to bagging area first
				c.addItemToBaggingArea(item);
				c.startPayment(PayingState.Gift);
				c.payBySwipingCard(card, new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
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
	 * @Description Verify that a random card not associated to any CardIssuer will
	 *              be rejected.
	 *              <p>
	 * @Expected_Outcome The machine rejects the card and throws an error
	 *                   <p>
	 * @Purpose To prevent people from paying with fake cards
	 */
	@Test
	public void testInvalidDebitCard() {

		for (int i = 0; i < REPEAT; i++) {
			Card card = new Card("debit", "1111222233334444", "John Doe", "123", "0909", true, true);
			CardIssuer issuer = new CardIssuer("abcdefg");
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
				c.payByTappingCard(card);
				fail();
			} catch (CheckoutException e) {
				success();
			} catch (OverloadException e) {
				fail();
			}
			CardIssuerDatabase.CREDIT_ISSUER_DATABASE.clear();
			CardIssuerDatabase.DEBIT_ISSUER_DATABASE.clear();
		}
		for (int i = 0; i < REPEAT; i++) {
			Card card = new Card("debit", "1111222233334444", "John Doe", "123", "0909", true, true);
			CardIssuer issuer = new CardIssuer("abcdefg");
			CardIssuerDatabase.DEBIT_ISSUER_DATABASE.add(issuer);
			// don't add the card to the issuer

			
			BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);

			// Try out all three payment types

			// tap
			c.reset();
			
			
			try {
				c.scanItem(item);
				//Has to add item to bagging area first
				c.addItemToBaggingArea(item);
				c.startPayment(PayingState.Debit);
				c.payByInsertingCard(card, "0909");
				fail();
			} catch (CheckoutException e) {
				success();
			} catch (OverloadException e) {
				fail();
			}
			CardIssuerDatabase.CREDIT_ISSUER_DATABASE.clear();
			CardIssuerDatabase.DEBIT_ISSUER_DATABASE.clear();
		}
		for (int i = 0; i < REPEAT; i++) {
			Card card = new Card("debit", "1111222233334444", "John Doe", "123", "0909", true, true);
			CardIssuer issuer = new CardIssuer("abcdefg");
			CardIssuerDatabase.DEBIT_ISSUER_DATABASE.add(issuer);
			// don't add the card to the issuer

			
			BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);

			// Try out all three payment types

			// tap
			c.reset();
			
			try {
				c.scanItem(item);
				//Has to add item to bagging area first
				c.addItemToBaggingArea(item);
				c.startPayment(PayingState.Debit);
				c.payBySwipingCard(card, new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
				fail();
			} catch (CheckoutException e) {
				success();
			} catch (OverloadException e) {
				fail();
			}
			CardIssuerDatabase.CREDIT_ISSUER_DATABASE.clear();
			CardIssuerDatabase.DEBIT_ISSUER_DATABASE.clear();
		}
	}

	/**
	 * @Description Verify that a random card not associated to any CardIssuer will
	 *              be rejected.
	 *              <p>
	 * @Expected_Outcome The machine rejects the card and throws an error
	 *                   <p>
	 * @Purpose To prevent people from paying with fake cards
	 */
	@Test
	public void testInvalidCreditCard() {
		for (int i = 0; i < REPEAT; i++) {
			Card card = new Card("credit", "1111222233334444", "John Doe", "123", "0909", true, true);
			CardIssuer issuer = new CardIssuer("abcdefg");
			CardIssuerDatabase.CREDIT_ISSUER_DATABASE.add(issuer);
			
			
			BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);

			// Try out all three payment types

			// tap
			c.reset();
			
			try {
				c.scanItem(item);
				//Has to add item to bagging area first
				c.addItemToBaggingArea(item);
				c.startPayment(PayingState.Credit);
				c.payByTappingCard(card);
				fail();
			} catch (CheckoutException e) {
				success();
			} catch (OverloadException e) {
				fail();
			}
			CardIssuerDatabase.CREDIT_ISSUER_DATABASE.clear();
			CardIssuerDatabase.DEBIT_ISSUER_DATABASE.clear();
		}
		for (int i = 0; i < REPEAT; i++) {
			Card card = new Card("credit", "1111222233334444", "John Doe", "123", "0909", true, true);
			CardIssuer issuer = new CardIssuer("abcdefg");
			CardIssuerDatabase.CREDIT_ISSUER_DATABASE.add(issuer);
			// don't add the card to the issuer

			
			BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);

			// Try out all three payment types

			// tap
			c.reset();
			
			try {
				c.scanItem(item);
				//Has to add item to bagging area first
				c.addItemToBaggingArea(item);
				c.startPayment(PayingState.Debit);
				c.payByInsertingCard(card, "0909");
				fail();
			} catch (CheckoutException e) {
				success();
			} catch (OverloadException e) {
				fail();
			}
			CardIssuerDatabase.CREDIT_ISSUER_DATABASE.clear();
			CardIssuerDatabase.DEBIT_ISSUER_DATABASE.clear();
		}
		for (int i = 0; i < REPEAT; i++) {
			Card card = new Card("credit", "1111222233334444", "John Doe", "123", "0909", true, true);
			CardIssuer issuer = new CardIssuer("abcdefg");
			CardIssuerDatabase.CREDIT_ISSUER_DATABASE.add(issuer);
			// don't add the card to the issuer

			
			BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);

			// Try out all three payment types

			// tap
			c.reset();
			
			try {
				c.scanItem(item);
				//Has to add item to bagging area first
				c.addItemToBaggingArea(item);
				c.startPayment(PayingState.Debit);
				c.payBySwipingCard(card, new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
				fail();
			} catch (CheckoutException e) {
				success();
			} catch (OverloadException e) {
				fail();
			}
			CardIssuerDatabase.CREDIT_ISSUER_DATABASE.clear();
			CardIssuerDatabase.DEBIT_ISSUER_DATABASE.clear();
		}
	}

	/**
	 * @Description Verify that a credit card without enough funds will fail
	 *              <p>
	 * @Expected_Outcome An exception is thrown because of insufficient funds
	 *                   <p>
	 * @Purpose Ensure that users have enough money to pay for an item
	 */
	@Test
	public void testInsufficientFundsOnCreditCard() {

		for (int i = 0; i < REPEAT; i++) {
			Card card = new Card("credit", "1111222233334444", "John Doe", "123", "0909", true, true);
			CardIssuer issuer = new CardIssuer("abcdefg");
			issuer.addCardData("1111222233334444", "John Doe", getCalendar(9, 2023), "123", BigDecimal.ONE);
			CardIssuerDatabase.CREDIT_ISSUER_DATABASE.add(issuer);
			
			BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);

			// Initialize state before payment
			// tap
			c.reset();
			
			try {
				c.scanItem(item);
				//Has to add item to bagging area first
				c.addItemToBaggingArea(item);
				c.startPayment(PayingState.Credit);
				c.payByTappingCard(card);
				fail();
			} catch (CheckoutException e) {
				success();
			} catch (OverloadException e) {
				fail();
			}
			CardIssuerDatabase.CREDIT_ISSUER_DATABASE.clear();
			CardIssuerDatabase.DEBIT_ISSUER_DATABASE.clear();
		}
	}

	/**
	 * @Description Verify that a debit card without enough funds will fail
	 *              <p>
	 * @Expected_Outcome An exception is thrown because of insufficient funds
	 *                   <p>
	 * @Purpose Ensure that users have enough money to pay for an item
	 */
	@Test
	public void testInsufficientFundsOnDebitCard() {

		for (int i = 0; i < REPEAT; i++) {
			Card card = new Card("debit", "1111222233334444", "John Doe", "123", "0909", true, true);
			CardIssuer issuer = new CardIssuer("abcdefg");
			issuer.addCardData("1111222233334444", "John Doe", getCalendar(9, 2023), "123", BigDecimal.ONE);
			CardIssuerDatabase.DEBIT_ISSUER_DATABASE.add(issuer);
			BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);

			// Initialize state before payment
			c.reset();
			
			try {
				c.scanItem(item);
				//Has to add item to bagging area first
				c.addItemToBaggingArea(item);
				c.payByInsertingCard(card, "0909");
				fail();
			} catch (CheckoutException e) {
				success();
			} catch (OverloadException e) {
				fail();
			}
			CardIssuerDatabase.CREDIT_ISSUER_DATABASE.clear();
			CardIssuerDatabase.DEBIT_ISSUER_DATABASE.clear();
		}
	}

	/**
	 * @Description Test to see if the software rejects an incorrect entered pin
	 *              <p>
	 * @Expected_Outcome An exception is thrown because the pin is incorrect
	 *                   <p>
	 * @Purpose Prevent people from paying with a card without knowing the pin
	 */
	@Test
	public void testIncorrectPinOnCardPayment() {

		for (int i = 0; i < REPEAT; i++) {
			Card card = new Card("debit", "1111222233334444", "John Doe", "123", "1111", true, true);
			CardIssuer issuer = new CardIssuer("abcdefg");
			issuer.addCardData("1111222233334444", "John Doe", getCalendar(9, 2023), "123", new BigDecimal(9999));
			CardIssuerDatabase.DEBIT_ISSUER_DATABASE.add(issuer);
			BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);

			// Initialize state before payment
			c.reset();
			
			try {
				c.scanItem(item);
				//Has to add item to bagging area first
				c.addItemToBaggingArea(item);
				c.payByInsertingCard(card, "9999");
				fail();
			} catch (CheckoutException e) {
				success();
			} catch (OverloadException e) {
				fail();
			}
			CardIssuerDatabase.CREDIT_ISSUER_DATABASE.clear();
			CardIssuerDatabase.DEBIT_ISSUER_DATABASE.clear();
		}
	}

	/**
	 * @Description Test to see if the software detects the user paying debit after
	 *              choosing credit, and paying with credit after choosing debit
	 *              <p>
	 * @Expected_Outcome An exception is thrown from the result of using an
	 *                   incorrect card type
	 *                   <p>
	 * @Purpose Enforce people to pay with the correct selected card type
	 */
	@Test
	public void testWrongCard() {

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
				c.startPayment(PayingState.Credit);
				c.payByTappingCard(card);
				fail();
			} catch (CheckoutException e) {
				success();
			} catch (OverloadException e) {
				fail();
			}
			CardIssuerDatabase.CREDIT_ISSUER_DATABASE.clear();
			CardIssuerDatabase.DEBIT_ISSUER_DATABASE.clear();
		}
		for (int i = 0; i < REPEAT; i++) {
			Card card = new Card("credit", "1111222233334444", "John Doe", "123", "0909", true, true);
			CardIssuer issuer = new CardIssuer("abcdefg");
			issuer.addCardData("1111222233334444", "John Doe", getCalendar(12, 2023), "123", new BigDecimal(999999));
			CardIssuerDatabase.CREDIT_ISSUER_DATABASE.add(issuer);
			
			
			BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);

			// Try out all three payment types

			// tap
			c.reset();
			
			try {
				c.scanItem(item);
				//Has to add item to bagging area first
				c.addItemToBaggingArea(item);
				c.startPayment(PayingState.Debit);
				c.payByTappingCard(card);
				fail();
			} catch (CheckoutException e) {
				success();
			} catch (OverloadException e) {
				fail();
			}
			CardIssuerDatabase.CREDIT_ISSUER_DATABASE.clear();
			CardIssuerDatabase.DEBIT_ISSUER_DATABASE.clear();
		}
		for (int i = 0; i < REPEAT; i++) {
			Card card = new Card("credit", "1111222233334444", "John Doe", "123", "0909", true, true);
			CardIssuer issuer = new CardIssuer("abcdefg");
			issuer.addCardData("1111222233334444", "John Doe", getCalendar(12, 2023), "123", new BigDecimal(999999));
			CardIssuerDatabase.CREDIT_ISSUER_DATABASE.add(issuer);
			
			
			BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);

			// Try out all three payment types

			// swipe
			c.reset();
			
			try {
				c.scanItem(item);
				//Has to add item to bagging area first
				c.addItemToBaggingArea(item);
				c.startPayment(PayingState.Debit);
				c.payBySwipingCard(card, new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
				fail();
			} catch (CheckoutException e) {
				success();
			} catch (OverloadException e) {
				fail();
			}
			CardIssuerDatabase.CREDIT_ISSUER_DATABASE.clear();
			CardIssuerDatabase.DEBIT_ISSUER_DATABASE.clear();
		}
	}

	/**
	 * User attempts to pay in a way using methods that are not supported by the
	 * card. For example, the user taps the card that doesn't support tapping.
	 */
	/**
	 * @throws OverloadException
	 * @Description User attempts to pay in a way using methods that are not
	 *              supported by the card. For example, the user taps the card that
	 *              doesn't support tapping.
	 *              <p>
	 * @Expected_Outcome An exception is thrown from the result of an unsupported
	 *                   card use
	 *                   <p>
	 * @Purpose To ensure that the software respects the card's supported operations
	 */
	@Test
	public void testUnsupportedMethodOfCardPayment() {

		for (int i = 0; i < REPEAT; i++) {
			try {
				Card noTap = new Card("credit", "1111222233334444", "John Doe", "123", "1111", false, true);
				Card noChip = new Card("debit", "1111111155555555", "John Doe", "123", "1111", true, false);

				CardIssuer issuer = new CardIssuer("abcdefg");
				issuer.addCardData("1111222233334444", "John Doe", getCalendar(9, 2023), "123", new BigDecimal(9999));
				issuer.addCardData("1111111155555555", "John Doe", getCalendar(9, 2023), "123", new BigDecimal(9999));

				CardIssuerDatabase.CREDIT_ISSUER_DATABASE.add(issuer);
				CardIssuerDatabase.DEBIT_ISSUER_DATABASE.add(issuer);
				
				BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);
				c.reset();
				c.scanItem(item);
				c.addItemToBaggingArea(item);

				
				c.startPayment(PayingState.Credit);
				try {
					c.payByTappingCard(noTap);
					fail();
				} catch (CheckoutException e) {
					success();
				}

				// insert the card (no chip)
				c.reset();
				c.scanItem(item);
				c.addItemToBaggingArea(item);

				
				c.startPayment(PayingState.Debit);
				try {
					c.payByInsertingCard(noChip, "1111");
					fail();
				} catch (CheckoutException e) {
					success();
				}

			} catch (CheckoutException | OverloadException e) {
				// fail because the exception is not related to what's causing the issue
				fail();
			}
			CardIssuerDatabase.CREDIT_ISSUER_DATABASE.clear();
			CardIssuerDatabase.DEBIT_ISSUER_DATABASE.clear();
		}
	}
	
	/**
	 * The customer attempts to pay with a card via the three methods
	 * when the self checkout device is not ready to accept card payment
	 * @throws OverloadException
	 */
	@Test
	public void CardPaymentisNotReadyTest() throws OverloadException {
		for (int i = 0; i < REPEAT; i++) {
			Card card = new Card("debit", "1111222233334444", "John Doe", "123", "0909", true, true);
			CardIssuer issuer = new CardIssuer("abcdefg");
			issuer.addCardData("1111222233334444", "John Doe", getCalendar(12, 2023), "123", new BigDecimal(999999));
			CardIssuerDatabase.DEBIT_ISSUER_DATABASE.add(issuer);
		
		//Insert
			BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);
			try {
				c.reset();
				c.scanItem(item);
				c.addItemToBaggingArea(item);
				c.payByInsertingCard(card, "0909");
				fail();
			} catch (CheckoutException e) {
				success();
			}
			CardIssuerDatabase.CREDIT_ISSUER_DATABASE.clear();
			CardIssuerDatabase.DEBIT_ISSUER_DATABASE.clear();
			CardIssuerDatabase.GIFT_ISSUER_DATABASE.clear();
		}
		for (int i = 0; i < REPEAT; i++) {
			Card card = new Card("debit", "1111222233334444", "John Doe", "123", "0909", true, true);
			CardIssuer issuer = new CardIssuer("abcdefg");
			issuer.addCardData("1111222233334444", "John Doe", getCalendar(12, 2023), "123", new BigDecimal(999999));
			CardIssuerDatabase.DEBIT_ISSUER_DATABASE.add(issuer);
		
		
			BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);

			// Try out all three payment types

			// Swipe
			try {
				c.reset();
				c.scanItem(item);
				c.addItemToBaggingArea(item);
				c.payBySwipingCard(card, new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
				fail();
			} catch (CheckoutException e1) {
				success();
			}
		}
		
		for (int i = 0; i < REPEAT; i++) {
			Card card = new Card("debit", "1111222233334444", "John Doe", "123", "0909", true, true);
			CardIssuer issuer = new CardIssuer("abcdefg");
			issuer.addCardData("1111222233334444", "John Doe", getCalendar(12, 2023), "123", new BigDecimal(999999));
			CardIssuerDatabase.DEBIT_ISSUER_DATABASE.add(issuer);
		
		
			BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);
			
			// Try out all three payment types

			// Tap
			try {
				c.reset();
				c.scanItem(item);
				c.addItemToBaggingArea(item);
				c.payByTappingCard(card);
				fail();
			} catch (CheckoutException e2) {
				success();
			}
		}
	}
	
	/**
	 * The customer attempts to pay with a card when the self checkout device
	 *  is done (not scanning or paused)
	 * @throws OverloadException
	 */
	@Test
	public void NotScanningPayment() throws OverloadException {
		for (int i = 0; i < REPEAT; i++) {
			try {
				Currency cad = Currency.getInstance("CAD");
				c.reset();
				BarcodedItem item = new BarcodedItem(new Barcode("30040321"), 397);
				// balance is 3.97
				c.scanItem(item);
				c.addItemToBaggingArea(item);
				c.startPayment(Checkout.PayingState.Cash);

				Banknote note = new Banknote(20, cad);
				c.payWithBanknote(note);

				c.emitChange();
				c.getChangeFromCoinTray();
				c.getChangeFromBanknoteSlots();
				c.removeRejectedBanknote();
				c.startPayment(PayingState.Debit);
				fail();

			} catch (OverloadException | CheckoutException | EmptyException | DisabledException e) {
				success();
			}
		}
	}
}
