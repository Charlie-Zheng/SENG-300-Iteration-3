/**
 * 
 */
package org.lsmr.selfcheckout.control.test;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Test;
import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.control.Checkout;
import org.lsmr.selfcheckout.control.CheckoutException;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;

/**
 * @author Group U08-2
 * @date Mar 31, 2021
 */
public class CashPaymentTest extends BaseTest {
	/**
	 * @throws Exception
	 * @Description This test inserts a valid coin in good conditions
	 *              <p>
	 * @Expected_Outcome Coin value reduces balance. There is a chance this test
	 *                   fails due to hardware.
	 *                   <p>
	 * @Purpose Test that the station accepts valid coins
	 */
	@Test
	public void testValidCoin() throws Exception {
		for (int i = 0; i < REPEAT; i++)
			try {
				c.reset();
				BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);
				//balance is 123.45
				c.scanItem(item);

				//Has to add item to bagging area first
				try {
					c.addItemToBaggingArea(item);
				} catch (OverloadException e1) {
					e1.printStackTrace();
					throw new Exception("Failed to add item to bagging area");
				}

				Currency cad = Currency.getInstance("CAD");

				c.startPayment(Checkout.PayingState.Cash);

				Coin coin = new Coin(new BigDecimal("1.00"), cad);
				c.payWithCoin(coin);
				// balance should be 123.45 - 1.00 = 122.45
				BigDecimal expected = new BigDecimal(12345 - 100).divide(new BigDecimal(100));
				multiTestAssertEquals(expected, c.getBalance());

				coin = new Coin(new BigDecimal("0.25"), cad);
				c.payWithCoin(coin);

				// balance should be 123.45 - 1.00 - 0.25 = 122.20
				expected = new BigDecimal("122.20");
				multiTestAssertEquals(expected, c.getBalance());
			} catch (CheckoutException e) {
				fail();
			}
	}

	/**
	 * @Description This test inserts a valid coin, but the station is not in the
	 *              payment state
	 *              <p>
	 * @Expected_Outcome throws checkout exception.
	 *                   <p>
	 * @Purpose Test that the station states work as it is supposed to
	 */
	@Test(expected = CheckoutException.class)
	public void testValidCoinButBeforePayment() throws CheckoutException {
		c.reset();
		Currency cad = Currency.getInstance("CAD");
		Coin coin = new Coin(new BigDecimal(1.0), cad);
		c.payWithCoin(coin);
	}

	/**
	 * @throws Exception
	 * @Description This test inserts a valid coin, after state is in payment
	 *              process
	 *              <p>
	 * @Expected_Outcome Coin value reduces balance. There is a chance this test
	 *                   fails due to hardware.
	 *                   <p>
	 * @Purpose Test that the station states work as it is supposed to
	 */
	@Test
	public void testValidCoinButAfterPayment() throws Exception {
		for (int i = 0; i < REPEAT; i++) {
			try {
				c.reset();
				BarcodedItem item = new BarcodedItem(new Barcode("30040321"), 397);
				//balance is 3.97
				c.scanItem(item);

				//Has to add item to bagging area first
				try {
					c.addItemToBaggingArea(item);
				} catch (OverloadException e1) {
					e1.printStackTrace();
					throw new Exception("Failed to add item to bagging area");
				}

				Currency cad = Currency.getInstance("CAD");

				try {
					c.startPayment(Checkout.PayingState.Cash);
				} catch (CheckoutException e) {
					fail();
					continue;
				}

				Coin coin = new Coin(new BigDecimal("2.00"), cad);
				c.payWithCoin(coin);
				c.payWithCoin(coin);

				// balance is 3.97-2.00-2.00 = -0.03
				c.payWithCoin(coin);
				fail();
			} catch (CheckoutException e) {
				success();
			}
		}

	}

	/**
	 * @throws Exception
	 * @Description This test inserts a valid coin to a disabled coin slot
	 *              <p>
	 * @Expected_Outcome throws checkout exception. There is a chance this test
	 *                   fails due to hardware.
	 *                   <p>
	 * @Purpose Test that the coin slot works as it is supposed to and won't accept
	 *          coins when disabled.
	 */
	@Test
	public void testValidCoinButDisabledCoinSlot() throws Exception {
		Currency cad = Currency.getInstance("CAD");
		int[] banknoteDenominations = { 5, 10, 20, 50, 100 };
		BigDecimal[] coinDenominations = { new BigDecimal(0.05), new BigDecimal(0.1), new BigDecimal(0.25),
				new BigDecimal(1.0), new BigDecimal(2.0) };
		SelfCheckoutStation station = new SelfCheckoutStation(cad, banknoteDenominations, coinDenominations, 1000, 1);
		Checkout c = new Checkout(station);
		for (int i = 0; i < REPEAT; i++) {
			try {

				c.reset();
				BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);
				c.scanItem(item);

				try {
					c.addItemToBaggingArea(item);
				} catch (OverloadException e) {
					//Succeeds when caught else where, but not here
					throw new Exception("Failed to add item to bagging area");
				}

				try {
					c.startPayment(Checkout.PayingState.Cash);
				} catch (CheckoutException e) {
					fail();
					continue;
				}

				Coin coin = new Coin(new BigDecimal(1.0), cad);
				station.coinSlot.disable();

				// should throw checkout exception here
				c.payWithCoin(coin);

				fail();
			} catch (CheckoutException e) {
				success();
			}
		}

	}

	/**
	 * @throws Exception
	 * @Description This test inserts an invalid coin denomination
	 *              <p>
	 * @Expected_Outcome Ignore coin. There is a chance this test fails due to
	 *                   hardware.
	 *                   <p>
	 * @Purpose Test that the station ignores invalid coin denominations as it is
	 *          supposed to
	 */
	@Test
	public void testInvalidCoinDenom() throws Exception {
		for (int i = 0; i < REPEAT; i++) {
			try {
				Currency cad = Currency.getInstance("CAD");
				c.reset();
				Coin coin = new Coin(new BigDecimal(3), cad);

				BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);
				// balance is 123.45
				c.scanItem(item);

				try {
					c.addItemToBaggingArea(item);
				} catch (OverloadException e) {
					//Succeeds when caught else where, but not here
					throw new Exception("Failed to add item to bagging area");
				}

				try {
					c.startPayment(Checkout.PayingState.Cash);
				} catch (CheckoutException e) {
					fail();
					continue;
				}

				c.payWithCoin(coin);
				BigDecimal expected = new BigDecimal(12345).divide(new BigDecimal(100));
				multiTestAssertEquals(expected, c.getBalance());
			} catch (CheckoutException e) {
				fail();
			}
		}

	}

	/**
	 * @throws Exception
	 * @Description This test inserts an invalid coin currency
	 *              <p>
	 * @Expected_Outcome Ignore coin. There is a chance this test fails due to
	 *                   hardware.
	 *                   <p>
	 * @Purpose Test that the station ignores coins with invalid currency as it is
	 *          supposed to
	 */
	@Test
	public void testInvalidCoinCur() throws Exception {
		for (int i = 0; i < REPEAT; i++) {
			try {
				Currency usd = Currency.getInstance("USD");

				c.reset();
				BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);
				// balance is 123.45
				c.scanItem(item);

				//Has to add item to bagging area first
				try {
					c.addItemToBaggingArea(item);
				} catch (OverloadException e1) {
					e1.printStackTrace();
					throw new Exception("Failed to add item to bagging area");
				}

				c.startPayment(Checkout.PayingState.Cash);

				Coin coin = new Coin(new BigDecimal(1), usd);
				c.payWithCoin(coin);
				BigDecimal expected = new BigDecimal(12345).divide(new BigDecimal(100));
				multiTestAssertEquals(expected, c.getBalance());
			} catch (CheckoutException e) {
				fail();
			}
		}

	}

	/**
	 * @throws Exception
	 * @Description This test inserts a valid banknote in good conditions.
	 *              <p>
	 * @Expected_Outcome Balance reduces by value of banknote. There is a chance
	 *                   this test fails due to hardware.
	 *                   <p>
	 * @Purpose Test that the station accepts valid banknotes.
	 */
	@Test
	public void testValidBanknote() throws Exception {
		for (int i = 0; i < REPEAT; i++) {
			try {
				Currency cad = Currency.getInstance("CAD");
				c.reset();
				BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);
				// balance is 123.45
				c.scanItem(item);

				try {
					c.addItemToBaggingArea(item);
				} catch (OverloadException e) {
					//Succeeds when caught else where, but not here
					throw new Exception("Failed to add item to bagging area");
				}

				c.startPayment(Checkout.PayingState.Cash);

				Banknote note = new Banknote(5, cad);
				c.payWithBanknote(note);
				BigDecimal expected = new BigDecimal(12345 - 500).divide(new BigDecimal(100));
				multiTestAssertEquals(expected, c.getBalance());

				note = new Banknote(10, cad);
				c.payWithBanknote(note);
				expected = new BigDecimal(12345 - 500 - 1000).divide(new BigDecimal(100));
				multiTestAssertEquals(expected, c.getBalance());

				note = new Banknote(100, cad);
				c.payWithBanknote(note);
				expected = new BigDecimal(12345 - 500 - 1000 - 10000).divide(new BigDecimal(100));
				multiTestAssertEquals(expected, c.getBalance());

			} catch (CheckoutException | OverloadException e) {
				fail();
			}
		}
	}

	/**
	 * @Description This test inserts a valid banknote, but the station is not in
	 *              the payment state.
	 *              <p>
	 * @Expected_Outcome Expected outcome : throws checkout exception.
	 *                   <p>
	 * @Purpose Test that the station states work as it is supposed to.
	 */
	@Test
	public void testValidBanknoteButBeforePayment() throws OverloadException {
		for (int i = 0; i < REPEAT; i++) {
			try {
				Currency cad = Currency.getInstance("CAD");
				c.reset();
				Banknote note = new Banknote(5, cad);
				c.payWithBanknote(note);
				fail();
			} catch (CheckoutException e) {
				success();
			}
		}

	}

	/**
	 * @throws Exception
	 * @Description This test inserts a valid banknote, after payment is already
	 *              finished
	 *              <p>
	 * @Expected_Outcome A CheckoutException is thrown for attempting to pay when
	 *                   payment is already finished
	 *                   <p>
	 * @Purpose Test that the station states work as it is supposed to.
	 */
	@Test
	public void testValidBanknoteButAfterPayment() throws Exception {
		for (int i = 0; i < REPEAT; i++) {
			try {
				Currency cad = Currency.getInstance("CAD");
				c.reset();
				BarcodedItem item = new BarcodedItem(new Barcode("30040321"), 397);
				// balance is 3.97
				c.scanItem(item);

				//Has to add item to bagging area first
				try {
					c.addItemToBaggingArea(item);
				} catch (OverloadException e1) {
					e1.printStackTrace();
					throw new Exception("Failed to add item to bagging area");
				}

				try {
					c.startPayment(Checkout.PayingState.Cash);
				} catch (CheckoutException e) {
					fail();
					continue;
				}

				Banknote note = new Banknote(100, cad);
				c.payWithBanknote(note);

				//should throw error, since the balance is already negative
				c.payWithBanknote(note);
				fail();
			} catch (CheckoutException e) {
				success();
			} catch (OverloadException e) {
				fail();
			}
		}

	}

	/**
	 * @throws Exception
	 * @Description This test inserts a valid banknote to a disabled banknote input
	 *              <p>
	 * @Expected_Outcome throws checkout exception. There is a chance this test
	 *                   fails due to hardware.
	 *                   <p>
	 * @Purpose Test that the banknote input works as it is supposed to and won't
	 *          accept notes when disabled.
	 */
	@Test
	public void testValidBanknoteButDisabledBanknoteInput() throws Exception {
		Currency cad = Currency.getInstance("CAD");
		int[] banknoteDenominations = { 5, 10, 20, 50, 100 };
		BigDecimal[] coinDenominations = { new BigDecimal(0.05), new BigDecimal(0.1), new BigDecimal(0.25),
				new BigDecimal(1.0), new BigDecimal(2.0) };
		SelfCheckoutStation station = new SelfCheckoutStation(cad, banknoteDenominations, coinDenominations, 1000, 1);

		Checkout c = new Checkout(station);
		for (int i = 0; i < REPEAT; i++) {
			try {
				c.reset();
				BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);
				c.scanItem(item);

				try {
					c.addItemToBaggingArea(item);
				} catch (OverloadException e) {
					//Succeeds when caught else where, but not here
					throw new Exception("Failed to add item to bagging area");
				}

				try {
					c.startPayment(Checkout.PayingState.Cash);
				} catch (CheckoutException e) {
					fail();
					continue;
				}

				Banknote note = new Banknote(5, cad);
				station.banknoteInput.disable();
				c.payWithBanknote(note);
				fail();
			} catch (CheckoutException e) {
				success();
			} catch (OverloadException e) {
				fail();
			}
		}

	}

	/**
	 * @throws Exception
	 * @Description This test inserts an invalid banknote denomination.
	 *              <p>
	 * @Expected_Outcome Balance doesn't change
	 *                   <p>
	 * @Purpose Test that the station ignores invalid banknote denominations as it
	 *          is supposed to.
	 */
	@Test
	public void testInvalidBanknoteDenom() throws Exception {
		for (int i = 0; i < REPEAT; i++) {
			try {
				Currency cad = Currency.getInstance("CAD");
				c.reset();
				BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);
				c.scanItem(item);

				try {
					c.addItemToBaggingArea(item);
				} catch (OverloadException e) {
					//Succeeds when caught else where, but not here
					throw new Exception("Failed to add item to bagging area");
				}

				c.startPayment(Checkout.PayingState.Cash);

				Banknote note = new Banknote(30, cad);
				c.payWithBanknote(note);
				BigDecimal expected = new BigDecimal("123.45");
				multiTestAssertEquals(expected, c.getBalance());

			} catch (CheckoutException | OverloadException e) {
				fail();
			}
		}

	}

	/**
	 * @throws Exception
	 * @Description This test inserts an invalid banknote currency.
	 *              <p>
	 * @ExpectedOutcome Balance doesn't change
	 *                  <p>
	 * @Purpose Test that the station ignores banknotes with invalid currency as it
	 *          is supposed to.
	 */
	@Test
	public void testInvalidBanknoteCurr() throws Exception {
		for (int i = 0; i < REPEAT; i++) {
			try {
				Currency usd = Currency.getInstance("USD");
				c.reset();
				BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);
				c.scanItem(item);

				try {
					c.addItemToBaggingArea(item);
				} catch (OverloadException e) {
					//Succeeds when caught else where, but not here
					throw new Exception("Failed to add item to bagging area");
				}

				c.startPayment(Checkout.PayingState.Cash);

				Banknote note = new Banknote(10, usd);
				c.payWithBanknote(note);
				BigDecimal expected = new BigDecimal("123.45");
				multiTestAssertEquals(expected, c.getBalance());
			} catch (CheckoutException | OverloadException e) {
				fail();
			}
		}

	}

	/**
	 * @throws Exception
	 * @Description This test inserts a banknote to an already overloaded banknote
	 *              input.
	 *              <p>
	 * @ExpectedOutcome Throw Overload Exception. There is a chance this test fails
	 *                  due to hardware.
	 *                  <p>
	 * @Purpose Test that the station does not accept banknotes to the banknote
	 *          input once the banknote input is full.
	 */
	@Test
	public void testOverloadedBanknoteInput() {
		for (int i = 0; i < REPEAT; i++) {
			try {
				Currency cad = Currency.getInstance("CAD");
				c.reset();
				BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);
				c.scanItem(item);
				try {
					c.addItemToBaggingArea(item);
				} catch (OverloadException e) {
					//Succeeds when caught else where, but not here
					fail();
				}

				c.startPayment(Checkout.PayingState.Cash);

				Banknote note = new Banknote(30, cad);
				try {
					c.payWithBanknote(note);
				} catch (CheckoutException e) {
					//invalid banknote should be checkoutexception
				}
				note = new Banknote(10, cad);

				// should throw overload exception because there is a dangling banknote from the rejection
				c.payWithBanknote(note);
				fail();
			} catch (CheckoutException e) {
				fail();
			} catch (OverloadException e) {
				success();
			}
		}
	}
}
