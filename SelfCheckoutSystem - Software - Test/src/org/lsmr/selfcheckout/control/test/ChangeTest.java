package org.lsmr.selfcheckout.control.test;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.control.Checkout;
import org.lsmr.selfcheckout.control.Checkout.PayingState;
import org.lsmr.selfcheckout.control.CheckoutException;
import org.lsmr.selfcheckout.control.GiveChange;
import org.lsmr.selfcheckout.devices.DisabledException;
import org.lsmr.selfcheckout.devices.EmptyException;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.SimulationException;
import org.lsmr.selfcheckout.external.ProductDatabases;
import org.lsmr.selfcheckout.products.BarcodedProduct;

/**
 * Test suite for giving change. All tests are run 100 times, and the test passes if
 * the success rate is greater than 75%. This is to account for the possible
 * random failures in the hardware
 * 
 * @author Group T08-2
 * @date Mar 17, 2021
 */
public class ChangeTest extends BaseTest {

	/**
	 * This test checks if calculated change by the GiveChange module has the
	 * correct denominations
	 * <p>
	 */
	@Test
	public void testGiveChangeDenom() {
		Currency cad = Currency.getInstance("CAD");
		int[] banknoteDenominations = { 5, 10, 20, 50, 100 };
		BigDecimal[] coinDenominations = { new BigDecimal("0.05"), new BigDecimal("0.10"), new BigDecimal("0.25"),
				new BigDecimal("1.00"), new BigDecimal("2.00") };
		SelfCheckoutStation station = new SelfCheckoutStation(cad, banknoteDenominations, coinDenominations, 1000, 1);
		GiveChange giveChange = new GiveChange(station);

		// $19.05 balance
		giveChange.calculateChange(new BigDecimal("-19.05"));
		Map<BigDecimal, Integer> coins = giveChange.getCoinsToGive();
		Map<Integer, Integer> notes = giveChange.getNotesToGive();

		for (BigDecimal coinDenom : coins.keySet()) {
			Arrays.sort(coinDenominations);
			assertTrue(Arrays.binarySearch(coinDenominations, coinDenom) >= 0);
		}
		for (Integer noteDenom : notes.keySet()) {
			Arrays.sort(banknoteDenominations);
			assertTrue(Arrays.binarySearch(banknoteDenominations, noteDenom) >= 0);
		}

		// $64.52 balance
		giveChange.calculateChange(new BigDecimal("-64.52"));

		coins = giveChange.getCoinsToGive();
		notes = giveChange.getNotesToGive();

		for (BigDecimal coinDenom : coins.keySet()) {
			Arrays.sort(coinDenominations);
			assertTrue(Arrays.binarySearch(coinDenominations, coinDenom) >= 0);
		}
		for (Integer noteDenom : notes.keySet()) {
			Arrays.sort(banknoteDenominations);
			assertTrue(Arrays.binarySearch(banknoteDenominations, noteDenom) >= 0);
		}

	}

	/**
	 * This test checks if calculated change by the GiveChange module is the correct
	 * amount
	 * <p>
	 */
	@Test
	public void testGiveChangeCalculated() {
		Currency cad = Currency.getInstance("CAD");
		int[] banknoteDenominations = { 5, 10, 20, 50, 100 };
		BigDecimal[] coinDenominations = { new BigDecimal("0.05"), new BigDecimal("0.10"), new BigDecimal("0.25"),
				new BigDecimal("1.00"), new BigDecimal("2.00") };
		SelfCheckoutStation station = new SelfCheckoutStation(cad, banknoteDenominations, coinDenominations, 1000, 1);
		GiveChange giveChange = new GiveChange(station);

		// $19.05 balance, change should be 19.05
		giveChange.calculateChange(new BigDecimal("-19.05"));
		Map<BigDecimal, Integer> coins = giveChange.getCoinsToGive();
		Map<Integer, Integer> notes = giveChange.getNotesToGive();

		BigDecimal totalChange = new BigDecimal(0);

		for (Map.Entry<BigDecimal, Integer> coin : coins.entrySet()) {
			totalChange = totalChange.add(coin.getKey().multiply(new BigDecimal(coin.getValue())));
		}

		for (Map.Entry<Integer, Integer> note : notes.entrySet()) {
			totalChange = totalChange.add(new BigDecimal(note.getKey() * note.getValue()));
		}
		assertTrue(new BigDecimal("19.05").compareTo(totalChange) == 0);

		// $64.52 balance, change should be 64.55
		giveChange.calculateChange(new BigDecimal("-64.52"));

		coins = giveChange.getCoinsToGive();
		notes = giveChange.getNotesToGive();

		totalChange = new BigDecimal(0);

		for (Map.Entry<BigDecimal, Integer> coin : coins.entrySet()) {
			totalChange = totalChange.add(coin.getKey().multiply(new BigDecimal(coin.getValue())));
		}

		for (Map.Entry<Integer, Integer> note : notes.entrySet()) {
			totalChange = totalChange.add(new BigDecimal(note.getKey() * note.getValue()));
		}
		assertTrue(new BigDecimal("64.55").compareTo(totalChange) == 0);

	}

	/**
	 * This test checks if the amount of the final balance is non-negative
	 * <p>
	 */
	@Test
	public void testGivingChangeFinalBalance() {
		for (int i = 0; i < REPEAT; i++) {
			try {
				Currency cad = Currency.getInstance("CAD");
				Checkout c = makeNewDefaultCheckout();
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
				multiTestAssertEquals(true, c.getBalance().compareTo(new BigDecimal(0)) >= 0);

			} catch (OverloadException | CheckoutException | EmptyException | DisabledException e) {
				fail();
			}
		}

		for (int i = 0; i < REPEAT; i++) {
			try {
				Currency cad = Currency.getInstance("CAD");
				Checkout c = makeNewDefaultCheckout();
				BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);
				// balance is 123.45
				c.scanItem(item);
				c.addItemToBaggingArea(item);
				c.startPayment(Checkout.PayingState.Cash);

				Banknote note = new Banknote(100, cad);
				c.payWithBanknote(note);
				c.payWithBanknote(note);

				c.emitChange();
				c.getChangeFromCoinTray();
				c.getChangeFromBanknoteSlots();

				multiTestAssertEquals(true, c.getBalance().compareTo(new BigDecimal(0)) >= 0);

			} catch (OverloadException | CheckoutException | EmptyException | DisabledException e) {
				fail();
			}
		}
	}

	/**
	 * This test checks if the received change is equal to the change in balance
	 * <p>
	 */
	@Test
	public void testGivingChangeRecievedChange() {
		for (int i = 0; i < REPEAT; i++) {
			try {
				Currency cad = Currency.getInstance("CAD");
				Checkout c = makeNewDefaultCheckout();
				BarcodedItem item = new BarcodedItem(new Barcode("30040321"), 397);
				// balance is 3.97
				c.scanItem(item);
				c.addItemToBaggingArea(item);
				c.startPayment(Checkout.PayingState.Cash);

				Banknote note = new Banknote(20, cad);
				c.payWithBanknote(note);

				BigDecimal initialBalance = c.getBalance();

				c.emitChange();
				List<Coin> coins = c.getChangeFromCoinTray();
				List<Banknote> banknotes = c.getChangeFromBanknoteSlots();

				BigDecimal finalBalance = c.getBalance();

				//need to check if the change given actually adds up to what should have been given
				BigDecimal receivedChange = new BigDecimal(0);
				for (Coin coin : coins) {
					receivedChange = receivedChange.add(coin.getValue());
				}
				for (Banknote banknote : banknotes) {
					receivedChange = receivedChange.add(new BigDecimal(banknote.getValue()));
				}

				multiTestAssertEquals(finalBalance.subtract(initialBalance), receivedChange);
			} catch (OverloadException | CheckoutException | EmptyException | DisabledException e) {
				fail();
			}
		}
		for (int i = 0; i < REPEAT; i++) {
			try {
				Currency cad = Currency.getInstance("CAD");
				Checkout c = makeNewDefaultCheckout();
				BarcodedItem item = new BarcodedItem(new Barcode("12345"), 123);
				// balance is 3.97
				c.scanItem(item);
				c.addItemToBaggingArea(item);
				c.startPayment(Checkout.PayingState.Cash);

				Banknote note = new Banknote(100, cad);
				c.payWithBanknote(note);

				note = new Banknote(50, cad);
				c.payWithBanknote(note);

				BigDecimal initialBalance = c.getBalance();

				c.emitChange();
				List<Coin> coins = c.getChangeFromCoinTray();
				List<Banknote> banknotes = c.getChangeFromBanknoteSlots();

				BigDecimal finalBalance = c.getBalance();

				//need to check if the change given actually adds up to what should have been given
				BigDecimal receivedChange = new BigDecimal(0);
				for (Coin coin : coins) {
					receivedChange = receivedChange.add(coin.getValue());
				}
				for (Banknote banknote : banknotes) {
					receivedChange = receivedChange.add(new BigDecimal(banknote.getValue()));
				}
				multiTestAssertEquals(finalBalance.subtract(initialBalance), receivedChange);
			} catch (OverloadException | CheckoutException | EmptyException | DisabledException e) {
				fail();
			}
		}
	}

	/**
	 * The checkout should throw an checkout exception if emitChange() is called
	 * before paying
	 * <p>
	 */
	@Test
	public void testGetChangeBeforePaying() {
		for (int i = 0; i < REPEAT; i++) {
			try {
				Currency cad = Currency.getInstance("CAD");
				Checkout c = makeNewDefaultCheckout();
				BarcodedItem item = new BarcodedItem(new Barcode("30040321"), 397);
				// balance is 3.97

				c.emitChange();

				fail();

			} catch (OverloadException | EmptyException | DisabledException e) {
				fail();
			} catch (CheckoutException e) {
				success();
			}
		}
		for (int i = 0; i < REPEAT; i++) {
			try {
				Currency cad = Currency.getInstance("CAD");
				Checkout c = makeNewDefaultCheckout();
				BarcodedItem item = new BarcodedItem(new Barcode("30040321"), 397);
				// balance is 3.97
				c.scanItem(item);
				c.addItemToBaggingArea(item);

				c.emitChange();

				fail();

			} catch (OverloadException | EmptyException | DisabledException e) {
				fail();
			} catch (CheckoutException e) {
				success();
			}
		}
	}

	/**
	 * The checkout should throw an checkout exception if emitChange() is called
	 * before paying enough
	 * <p>
	 */
	@Test
	public void testGetChangeBeforePayingEnough() {
		for (int i = 0; i < REPEAT; i++) {
			try {
				Currency cad = Currency.getInstance("CAD");
				Checkout c = makeNewDefaultCheckout();
				BarcodedItem item = new BarcodedItem(new Barcode("30040321"), 397);
				// balance is 3.97
				c.scanItem(item);
				c.addItemToBaggingArea(item);
				
				c.startPayment(PayingState.Cash);

				Coin coin = new Coin(new BigDecimal("2.00"), cad);
				c.payWithCoin(coin);
				try {
					c.emitChange();
					fail();
				} catch (CheckoutException e) {
					success();
				}
			

			} catch (OverloadException | EmptyException | DisabledException e) {
				fail();
			} catch (CheckoutException e) {
				fail();
			}
		}
	}
}
