package org.lsmr.selfcheckout.control.test;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Currency;

import org.junit.After;
import org.junit.Before;
import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.control.Checkout;
import org.lsmr.selfcheckout.control.MembershipCardDatabase;
import org.lsmr.selfcheckout.control.ProductWeightDatabase;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.SimulationException;
import org.lsmr.selfcheckout.external.ProductDatabases;
import org.lsmr.selfcheckout.products.BarcodedProduct;

/**
 * Test suite for Checkout. All tests are run 100 times, and the test passes if
 * the success rate is greater than 75%. This is to account for the possible
 * random failures in the hardware
 * 
 * @author Group U08-2
 * @date Mar 31, 2021
 */
public abstract class BaseTest {
	//We didn't use this, so changing it will have no effect
	protected static String BASEDIR = "C:";

	protected int totalTests;
	protected int successfulTests;
	protected final int REPEAT = 5;

	/**
	 * Setup method that is invoked before each test method, initializing product
	 * database. Resets the number of successful and total tests
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		initProductDatabase();
		initMembershipCardDatabase();
		totalTests = 0;
		successfulTests = 0;
	}

	/**
	 * Teardown method that is invoked after each test method. It checks if the
	 * success rate of tests was greater than 75%.
	 */
	@After
	public void tearDown() {
		if (totalTests > 0) {
			assertTrue("The success rate was not above 75%, it was " + (double) (successfulTests) / totalTests,
					(double) (successfulTests) / totalTests > 0.75);

		}
	}

	/**
	 * Helper method to generate a Calendar instance with the given date. Used in
	 * creating card instances.
	 * 
	 * @param month
	 *            the month starting with January = 1
	 * @param year
	 *            the four-digit year
	 * @return a new calendar instance
	 */
	protected Calendar getCalendar(int month, int year) {
		Calendar date = Calendar.getInstance();
		date.set(year, month - 1, 1);
		return date;
	}

	/**
	 * Creates a new default checkout instance on a new selfCheckoutStation which
	 * accepts CAD, with banknote denominations of $5, $10, $20, $50, $100, and coin
	 * denominations of $0.05, $0.10, $0.25, $1, $2. The maximum weight of the scale
	 * is 1000 and has a sensitivity of 1.
	 * 
	 * @return The new default checkout instance
	 */
	protected Checkout makeNewDefaultCheckout() {
		Currency cad = Currency.getInstance("CAD");
		int[] banknoteDenominations = { 5, 10, 20, 50, 100 };
		BigDecimal[] coinDenominations = { new BigDecimal("0.05"), new BigDecimal("0.10"), new BigDecimal("0.25"),
				new BigDecimal("1.00"), new BigDecimal("2.00") };
		SelfCheckoutStation station = new SelfCheckoutStation(cad, banknoteDenominations, coinDenominations, 100000, 1);
		Checkout c = new Checkout(station);
		for (Integer value : station.banknoteDispensers.keySet()) {
			Banknote[] banknotes = new Banknote[10];
			for (int i = 0; i < 10; i++) {
				banknotes[i] = new Banknote(value, cad);
			}
			try {
				station.banknoteDispensers.get(value).load(banknotes);
			} catch (SimulationException | OverloadException e) {
				//should not happen
				e.printStackTrace();
			}
		}
		for (BigDecimal value : station.coinDispensers.keySet()) {
			Coin[] coins = new Coin[10];
			for (int i = 0; i < 10; i++) {
				coins[i] = new Coin(value, cad);
			}
			try {
				station.coinDispensers.get(value).load(coins);
			} catch (SimulationException | OverloadException e) {
				//should not happen
				e.printStackTrace();
			}
		}
		return c;
	}

	/**
	 * Check if the actual is equal to the expected. If it is equal, a successful
	 * test is declared, otherwise a failed test is declared
	 * 
	 * @param expected
	 *            The expected value
	 * @param actual
	 *            The actual value
	 */
	protected void multiTestAssertEquals(long expected, long actual) {
		if (expected == actual) {
			successfulTests++;
		}
		totalTests++;
	}

	/**
	 * Check if the actual is equal to the expected. If it is equal, a successful
	 * test is declared, otherwise a failed test is declared
	 * 
	 * @param expected
	 *            The expected value
	 * @param actual
	 *            The actual value
	 */
	protected void multiTestAssertEquals(boolean expected, boolean actual) {
		if (expected == actual) {
			successfulTests++;
		}
		totalTests++;
	}

	/**
	 * Check if the actual is equal to the expected. Equality is determined by if
	 * the absolute value of the difference in expected and actual values is less
	 * than the delta. If it is equal, a successful test is declared, otherwise a
	 * failed test is declared.
	 * 
	 * @param expected
	 *            The expected value
	 * @param actual
	 *            The actual value
	 * @param The
	 *            maximum difference between expected and actual for a successful
	 *            test
	 */
	protected void multiTestAssertEquals(double expected, double actual, double delta) {
		if (Math.abs(expected - actual) < delta) {
			successfulTests++;
		}
		totalTests++;
	}

	/**
	 * Check if the actual is equal to the excepted using the
	 * BigDecimal.comparesTo() method. If it is equal, a successful test is
	 * declared, otherwise a failed test is declared
	 * 
	 * @param expected
	 *            The expected value
	 * @param actual
	 *            The actual value
	 */
	protected void multiTestAssertEquals(BigDecimal expected, BigDecimal actual) {
		if (expected.compareTo(actual) == 0) {
			successfulTests++;
		}
		totalTests++;
	}

	/**
	 * Declares a successful test
	 */
	protected void success() {
		successfulTests++;
		totalTests++;
	}

	/**
	 * Declares a failed test
	 */
	protected void fail() {
		totalTests++;
	}

	/**
	 * Clears the product database and initializes it with the following 3 products
	 * and their barcode:
	 * <p>
	 * 1. A product with a barcode of 12345 and price $123.45 and weight 123
	 * <p>
	 * 2. A product with a barcode of 987654321 and price $98.76 and weight 98
	 * <p>
	 * 3. A product with a barcode of 30040321 and price $3.97 and weight 397
	 * <p>
	 * The prices are exact.
	 */
	protected void initProductDatabase() {
		Barcode temp;
		ProductDatabases.BARCODED_PRODUCT_DATABASE.clear();

		// An product with a barcode of 12345 and price $123.45
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(temp = new Barcode("12345"),
				new BarcodedProduct(temp, "Product with barcode of 12345 and price $123.45", new BigDecimal("123.45")));
		ProductWeightDatabase.PRODUCT_WEIGHT_DATABASE.put(temp, 123.0);

		// A product with a barcode of 987654321 and price $98.76
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(temp = new Barcode("987654321"), new BarcodedProduct(temp,
				"Product with barcode of 987654321 and price $98.76", new BigDecimal("98.76")));
		ProductWeightDatabase.PRODUCT_WEIGHT_DATABASE.put(temp, 98.0);

		// A product with a barcode of 30040321 and price $3.97
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(temp = new Barcode("30040321"),
				new BarcodedProduct(temp, "Product with barcode of 30040321 and price $3.97", new BigDecimal("3.97")));
		ProductWeightDatabase.PRODUCT_WEIGHT_DATABASE.put(temp, 397.0);
	}

	/**
	 * Clears the membership card database and initializes it with the following 3
	 * membership numbers
	 * <p>
	 * 1. 123456789
	 * <p>
	 * 2. 6541236
	 * <p>
	 * 3. 11111111
	 * <p>
	 * The prices are exact.
	 */
	protected void initMembershipCardDatabase() {
		MembershipCardDatabase.MEMBERSHIP_CARD_DATABASE.clear();
		MembershipCardDatabase.MEMBERSHIP_CARD_DATABASE.put("123456789", "Alice");
		MembershipCardDatabase.MEMBERSHIP_CARD_DATABASE.put("6541236", "Bob");
		MembershipCardDatabase.MEMBERSHIP_CARD_DATABASE.put("11111111", "Charlie");

	}
}
