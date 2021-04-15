package org.lsmr.selfcheckout.control;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.control.gui.GUIController;
import org.lsmr.selfcheckout.control.gui.Pair;
import org.lsmr.selfcheckout.control.gui.StateHandler.StateUpdateListener;
import org.lsmr.selfcheckout.control.gui.states.AttendantLogInState;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.external.CardIssuer;
import org.lsmr.selfcheckout.external.ProductDatabases;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;

public class GUIMainClass {

	/**
	 * Adds the list of keys and list of values into the specified map. Number of
	 * keys must be equal to the number of values. This function adds a mapping of K
	 * -> V into the database Map.
	 * 
	 * @param database
	 *            the database to insert to
	 */
	private static <K, V> void addAll(Map<K, V> database, List<Pair<K, V>> pairs) {
		for (Pair<K, V> p : pairs) {
			database.put(p.first, p.second);
		}
	}

	/**
	 * Converts a String array of data to a list of Key-Value mappings For each data
	 * element, data[0] must be the code, data[1] must be the description, and
	 * data[2] must be the price.
	 * 
	 * @param data
	 * @return
	 */
	private static List<Pair<PriceLookupCode, PLUCodedProduct>> pluProductsOf(String[][] data) {
		// why this works but initializing an array of generic types doesn't, I have no idea.
		// the underlying implementation of an ArrayList should perform the same thing I did.
		ArrayList<Pair<PriceLookupCode, PLUCodedProduct>> products = new ArrayList<Pair<PriceLookupCode, PLUCodedProduct>>();

		for (String[] row : data) {
			PriceLookupCode code = new PriceLookupCode(row[0]);
			PLUCodedProduct product = new PLUCodedProduct(code, row[1], new BigDecimal(row[2]));
			products.add(new Pair<>(code, product));
		}

		return products;
	}

	/**
	 * Converts a String array of data to a list of Key-Value mappings For each data
	 * element, data[0] must be the code, data[1] must be the description, and
	 * data[2] must be the price.
	 * 
	 * @param data
	 * @return
	 */
	private static List<Pair<Barcode, BarcodedProduct>> barcodedProductsOf(String[][] data) {
		ArrayList<Pair<Barcode, BarcodedProduct>> products = new ArrayList<Pair<Barcode, BarcodedProduct>>();

		for (String[] row : data) {
			Barcode code = new Barcode(row[0]);
			BarcodedProduct product = new BarcodedProduct(code, row[1], new BigDecimal(row[2]));
			products.add(new Pair<>(code, product));
		}

		return products;
	}

	private static List<Pair<String, String>> membershipsOf(String[][] data) {
		ArrayList<Pair<String, String>> memberships = new ArrayList<Pair<String, String>>();

		for (String[] row : data) {
			memberships.add(new Pair<>(row[0], row[1]));
		}

		return memberships;
	}

	private static List<Pair<Barcode, Double>> productWeightsOf(String[][] data) {
		ArrayList<Pair<Barcode, Double>> weights = new ArrayList<Pair<Barcode, Double>>();

		for (String[] row : data) {
			weights.add(new Pair<>(new Barcode(row[0]), Double.valueOf(row[1])));
		}

		return weights;
	}

	private static HashMap<Integer, Attendant> employees = new HashMap<Integer, Attendant>();

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
	protected static Calendar getCalendar(int month, int year) {
		Calendar date = Calendar.getInstance();
		date.set(year, month - 1, 1);
		return date;
	}

	public static void initializeDatabases() {
		addAll(ProductDatabases.PLU_PRODUCT_DATABASE,
				pluProductsOf(new String[][] { { "5425", "Apple", "1.12" }, { "4523", "Pear", "0.95" } }));

		addAll(ProductDatabases.BARCODED_PRODUCT_DATABASE,
				barcodedProductsOf(new String[][] { { "1124341", "Checkout Machine Toy", "20.99" },
						{ "0101010", "PlayStation 6 (PS6)", "699.99" }, { "12345", "Grand Piano", "109000.00" } }));
		addAll(ProductWeightDatabase.PRODUCT_WEIGHT_DATABASE, productWeightsOf(
				new String[][] { { "1124341", "70.1" }, { "0101010", "150.9" }, { "12345", "321.0" }, }));

		addAll(MembershipCardDatabase.MEMBERSHIP_CARD_DATABASE,
				membershipsOf(new String[][] { { "123456789", "Robert James Walker" } }));

		CardIssuer debitIssuer = new CardIssuer("DebitCards LTD");
		debitIssuer.addCardData("1111222233334444", "John Doe", getCalendar(12, 2023), "123", new BigDecimal(1000000));

		CardIssuerDatabase.DEBIT_ISSUER_DATABASE.add(debitIssuer);

		CardIssuer creditIssuer = new CardIssuer("Real Canadian CreditCards");
		creditIssuer.addCardData("7777", "Jane Doe", getCalendar(12, 2023), "123", new BigDecimal(1000000));

		CardIssuerDatabase.CREDIT_ISSUER_DATABASE.add(creditIssuer);

		CardIssuer giftIssuer = new CardIssuer("SelfCheckout INC.");
		giftIssuer.addCardData("123456", "Jas Jome", getCalendar(12, 2023), "123", new BigDecimal(1000000));

		CardIssuerDatabase.GIFT_ISSUER_DATABASE.add(giftIssuer);

		try {
			employees.put(1234, new Attendant(1234, "James", 1234));
			employees.put(1111, new Attendant(1111, "Ones", 1111));
		} catch (CheckoutException e) {
		}
		// assertions
		if (ProductWeightDatabase.PRODUCT_WEIGHT_DATABASE.size() != ProductDatabases.BARCODED_PRODUCT_DATABASE.size()) {
			throw new RuntimeException("Product weight database and barcoded database have different sizes");
		}
	}

	public static void main(String[] args) {
		initializeDatabases();

		AttendantSystem attendentSystem = new AttendantSystem(employees);

		Currency cad = Currency.getInstance("CAD");
		int[] banknoteDenominations = { 5, 10, 20, 50, 100 };
		BigDecimal[] coinDenominations = { new BigDecimal("0.05"), new BigDecimal("0.10"), new BigDecimal("0.25"),
				new BigDecimal("1.00"), new BigDecimal("2.00") };
		SelfCheckoutStation station = new SelfCheckoutStation(cad, banknoteDenominations, coinDenominations, 10000, 1);
		Checkout c = new Checkout(station);

		StateUpdateListener guiUpdateListener = new GUIupdateListener(c);

		GUIController guiController = new GUIController(station.screen.getFrame());
		guiController.addStateUpdateListener(guiUpdateListener); // so the checkout station can know of any GUI updates
		guiController.setState(new AttendantLogInState());

		// setup frame to terminate the whole program when closed
		station.screen.getFrame().addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		c.guiController = guiController;

		c.addInk(10000);
		c.addPaper(1000);
		c.reset();

		try {
			attendentSystem.register(c);
		} catch (CheckoutException e) {
		}

		c.run();

		// fire up our simulator
		GUIPhysicalSimulatorWindow window = new GUIPhysicalSimulatorWindow(c, guiController);
		window.createWindow();

		GUIPhysicalAttendantSimulatorWindow attWindow = new GUIPhysicalAttendantSimulatorWindow(c);
		attWindow.setupWindow();
		guiController.addStateUpdateListener(attWindow);
	}
}
