package org.lsmr.selfcheckout.control;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.external.ProductDatabases;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;

public class MainClass {
	
	/**
	 * Adds the list of keys and list of values into the specified map.
	 * 
	 * Number of keys must be equal to the number of values. This function adds a mapping of
	 * K -> V into the database Map.
	 * 
	 * @param database the database to insert to
	 */
	private static <K, V> void addAll (Map<K, V> database, List<Pair<K, V>> pairs) {
		for (Pair<K, V> p : pairs) {
			database.put(p.first, p.second);
		}
	}

	/**
	 * Converts a String array of data to a list of Key-Value mappings
	 * 
	 * For each data element, data[0] must be the code, data[1] must be the description, and data[2] must
	 * be the price.
	 * 
	 * @param data
	 * @return
	 */
	private static List <Pair<PriceLookupCode, PLUCodedProduct>> pluProductsOf(String[][] data) {
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
	 * Converts a String array of data to a list of Key-Value mappings
	 * 
	 * For each data element, data[0] must be the code, data[1] must be the description, and data[2] must
	 * be the price.
	 * 
	 * @param data
	 * @return
	 */
	private static List <Pair<Barcode, BarcodedProduct>> barcodedProductsOf(String[][] data) {
		ArrayList<Pair<Barcode, BarcodedProduct>> products = new ArrayList<Pair<Barcode, BarcodedProduct>>();

		for (String[] row : data) {
			Barcode code = new Barcode(row[0]);
			BarcodedProduct product = new BarcodedProduct(code, row[1], new BigDecimal(row[2]));
			products.add(new Pair<>(code, product));
		}

		return products;
	}


	public static void initializeDatabases() {
		addAll(
				ProductDatabases.PLU_PRODUCT_DATABASE,
				pluProductsOf(new String[][] {
					{"5425", "Apple", "1.12"},
					{"4523", "Pear", "0.95"}
				}));

		addAll(
				ProductDatabases.BARCODED_PRODUCT_DATABASE,
				barcodedProductsOf(new String[][] {
					{"1124341", "Checkout Machine Toy", "20.99"}
				}));
	}

	public static void main(String[] args) {
		initializeDatabases();
		
		Currency cad = Currency.getInstance("CAD");
		int[] banknoteDenominations = { 5, 10, 20, 50, 100 };
		BigDecimal[] coinDenominations = { new BigDecimal("0.05"), new BigDecimal("0.10"), new BigDecimal("0.25"),
				new BigDecimal("1.00"), new BigDecimal("2.00") };
		SelfCheckoutStation station = new SelfCheckoutStation(cad, banknoteDenominations, coinDenominations, 122, 1);
		Checkout c = new Checkout(station);
		c.run();
	}
}