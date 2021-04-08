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
import org.lsmr.selfcheckout.control.Checkout.PayingState;
import org.lsmr.selfcheckout.control.CheckoutException;
import org.lsmr.selfcheckout.devices.OverloadException;

/**
 * @author Group U08-2
 * @date Mar 31, 2021
 */
public class StartPaymentTest extends BaseTest {
	/**
	 * Tests empty cart starts in scanning state and not paying state and tests for
	 * no state change after an expected failed payment initiation, as there are no
	 * items to purchase.
	 */
	@Test
	public void paymentProcessCheckNoItemsScanned() {
		for (int i = 0; i < REPEAT; i++) {
			Checkout c = makeNewDefaultCheckout();
			// should not transition to payment, since there is nothing to pay for
			try {
				c.startPayment(Checkout.PayingState.Cash);
				fail();
			} catch (CheckoutException e) {
				success();
				// no change to scanning/paying state
				multiTestAssertEquals(true, c.isScanning());
				multiTestAssertEquals(false, c.isPaying());
			}
		}
	}

	/**
	 * Scans items and tests for successful payment process initiation.
	 * <p>
	 * Scans valid items and expects payment initiation to be successful. Also tests
	 * that checkout in in paying state and is not in scanning state, as payment
	 * initiation should prevent further scanning.
	 */
	@Test
	public void paymentProcessCheckItemsScannedAndAdded() {
		for (int i = 0; i < REPEAT; i++) {
			try {

				Checkout c = makeNewDefaultCheckout();

				// scan some items then check if payment process can be successfully started
				BarcodedItem item = new BarcodedItem(new Barcode("30040321"), 397);
				BarcodedItem item2 = new BarcodedItem(new Barcode("987654321"), 98);
				c.scanItem(item);

				//Has to add item to bagging area first
				c.addItemToBaggingArea(item);

				c.scanItem(item2);
				c.addItemToBaggingArea(item2);

				c.startPayment(Checkout.PayingState.Cash);

				// should be paying and no longer scanning
				multiTestAssertEquals(true, c.isPaying());
				multiTestAssertEquals(false, c.isScanning());
			} catch (CheckoutException | OverloadException e) {
				fail();
			}
		}
		for (int i = 0; i < REPEAT; i++) {
			try {

				Checkout c = makeNewDefaultCheckout();

				// scan some items then check if payment process can be successfully started
				BarcodedItem item = new BarcodedItem(new Barcode("30040321"), 397);
				BarcodedItem item2 = new BarcodedItem(new Barcode("987654321"), 98);
				c.scanItem(item);

				//Has to add item to bagging area first
				c.addItemToBaggingArea(item);

				c.scanItem(item2);
				c.addItemToBaggingArea(item2);

				c.startPayment(Checkout.PayingState.Debit);

				// should be paying and no longer scanning
				multiTestAssertEquals(true, c.isPaying());
				multiTestAssertEquals(false, c.isScanning());
			} catch (CheckoutException | OverloadException e) {
				fail();
			}
		}
		for (int i = 0; i < REPEAT; i++) {
			try {

				Checkout c = makeNewDefaultCheckout();

				// scan some items then check if payment process can be successfully started
				BarcodedItem item = new BarcodedItem(new Barcode("30040321"), 397);
				BarcodedItem item2 = new BarcodedItem(new Barcode("987654321"), 98);
				c.scanItem(item);

				//Has to add item to bagging area first
				c.addItemToBaggingArea(item);

				c.scanItem(item2);
				c.addItemToBaggingArea(item2);

				c.startPayment(Checkout.PayingState.Credit);

				// should be paying and no longer scanning
				multiTestAssertEquals(true, c.isPaying());
				multiTestAssertEquals(false, c.isScanning());
			} catch (CheckoutException | OverloadException e) {
				fail();
			}
		}

	}

	/**
	 * Tests that checkout is not in paying or scanning states once coin payment is
	 * complete.
	 * <p>
	 * Creates default checkout and valid barcoded item. Scans item, begins payment,
	 * and pays with valid coins. Checkout instance expected to not be in paying or
	 * scanning state once transaction complete.
	 * 
	 * @throws Exception
	 * @throws OverloadException
	 * @throws CheckoutException
	 */
	@Test
	public void paymentProcessCheckDonePayingCoin() throws Exception {
		for (int i = 0; i < 100; i++) {
			try {
				Checkout c = makeNewDefaultCheckout();

				// scan some items then check if payment process can be successfully started
				BarcodedItem item = new BarcodedItem(new Barcode("30040321"), 397);
				c.scanItem(item);

				//Has to add item to bagging area
				try {
					c.addItemToBaggingArea(item);
				} catch (OverloadException e) {
					e.printStackTrace();
					throw new Exception("Failed to add item to bagging area");
				}

				try {
					c.startPayment(Checkout.PayingState.Cash);
				} catch (CheckoutException e) {
					fail();
					continue;
				}

				Coin coin = new Coin(new BigDecimal("2.00"), Currency.getInstance("CAD"));
				c.payWithCoin(coin);
				c.payWithCoin(coin);

				multiTestAssertEquals(false, c.isPaying());
				multiTestAssertEquals(false, c.isScanning());
			} catch (CheckoutException e) {
				fail();
			}
		}
	}

	/**
	 * Tests that checkout is not in paying or scanning states once banknote payment
	 * is complete.
	 * <p>
	 * Creates default checkout and valid barcoded item. Scans item, begins payment,
	 * and pays with valid banknote. Checkout instance expected to not be in paying
	 * or scanning state once transaction complete.
	 * 
	 * @throws Exception
	 * @throws OverloadException
	 * @throws CheckoutException
	 */
	@Test
	public void paymentProcessCheckDonePayingBanknote() throws Exception {
		for (int i = 0; i < REPEAT; i++) {
			try {
				Checkout c = makeNewDefaultCheckout();

				// scan some items then check if payment process can be successfully started
				BarcodedItem item = new BarcodedItem(new Barcode("30040321"), 397);
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

				c.payWithBanknote(new Banknote(100, Currency.getInstance("CAD")));

				multiTestAssertEquals(false, c.isPaying());
				multiTestAssertEquals(false, c.isScanning());
			} catch (OverloadException | CheckoutException e) {
				fail();
			}
		}

	}

	/**
	 * Scans item after payment process initiated, expecting no change to balance.
	 * <p>
	 * Creates default checkout and valid barcoded item. Scans item then starts
	 * payment. Asserts that checkout balance is what it is expected to be, then
	 * scans in item again. Asserts the the balance has not changed, as payment
	 * initiation should lock balance.
	 * 
	 * @throws Exception
	 */
	@Test
	public void paymentProcessCheckScanDuringPayment() {
		for (int i = 0; i < REPEAT; i++) {
			try {
				Checkout c = makeNewDefaultCheckout();
				BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);
				c.scanItem(item);

				c.addItemToBaggingArea(item);

				c.startPayment(Checkout.PayingState.Cash);

				BigDecimal expected = new BigDecimal(12345).divide(new BigDecimal(100));
				multiTestAssertEquals(expected, c.getBalance());
				c.scanItem(item);

				//balance shouldn't change
				multiTestAssertEquals(expected, c.getBalance());
			} catch (CheckoutException | OverloadException e) {
				fail();
			}

		}
	}

	/**
	 * Creates a self checkout station and attempts to begin paying without adding
	 * the correct weight to the bagging area
	 * <p>
	 * Should return false, which notifies that the device is not ready to start
	 * payment
	 * <p>
	 */
	@Test
	public void paymentProcessCheckWeightNotMatching() {
		for (int i = 0; i < REPEAT; i++) {
			Checkout c = makeNewDefaultCheckout();
			BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);
			try {
				c.scanItem(item);
			} catch (CheckoutException e1) {
				fail();
				continue;
			}
			//
			try {
				c.startPayment(PayingState.Credit);
				fail();
			} catch (CheckoutException e) {
				success();
			}
		}

		for (int i = 0; i < REPEAT; i++) {
			Checkout c = makeNewDefaultCheckout();

			//expected weight is 123
			BarcodedItem item = new BarcodedItem(new Barcode("12345"), 323);

			try {
				c.scanItem(item);
			} catch (CheckoutException e1) {
				fail();
				continue;
			}

			try {
				c.addItemToBaggingArea(item);
			} catch (CheckoutException e1) {

			} catch (OverloadException e) {
				fail();
				continue;
			}

			try {
				c.startPayment(PayingState.Credit);
				fail();
			} catch (CheckoutException e) {
				success();
			}
		}

	}

	/**
	 * Creates a self checkout station and attempts to begin paying without adding
	 * the correct weight to the bagging area by forgetting to add an item
	 * <p>
	 * Should return false, which notifies that the device is not ready to start
	 * payment
	 * <p>
	 */
	@Test
	public void paymentProcessCheckOmitOne() {
		for (int i = 0; i < REPEAT; i++) {
			try {
				Checkout c = makeNewDefaultCheckout();
				//forget to add one of the items
					BarcodedItem item = new BarcodedItem(new Barcode("987654321"), 98);
					c.scanItem(item);
					c.addItemToBaggingArea(item);
					item = new BarcodedItem(new Barcode("12345"), 123);
					c.scanItem(item);
				try {
					c.startPayment(PayingState.Debit);
				} catch (CheckoutException e) {
					success();
				}
			} catch (CheckoutException | OverloadException e) {
				fail();
			}
		}
	}

	/**
	 * Creates a self checkout station and attempts to begin paying while the item
	 * weight is slightly off
	 * <p>
	 * Should successfully start payment
	 * <p>
	 */
	@Test
	public void paymentProcessCheckSlightlyDifferentWeight() {
		for (int i = 0; i < REPEAT; i++) {
			try {
				Checkout c = makeNewDefaultCheckout();
				//forget to add one of the items
				BarcodedItem item = new BarcodedItem(new Barcode("987654321"), 105);
				c.scanItem(item);
				c.addItemToBaggingArea(item);
				c.startPayment(PayingState.Debit);
				multiTestAssertEquals(true, c.isPaying());
				
			} catch (CheckoutException | OverloadException e) {
				fail();
			}
		}
	}
}
