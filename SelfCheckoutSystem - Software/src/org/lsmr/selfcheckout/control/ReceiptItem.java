/**
 * 
 */
package org.lsmr.selfcheckout.control;

import java.math.BigDecimal;

import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;
import org.lsmr.selfcheckout.products.Product;

/**
 * @author charl
 * @date Apr. 6; 2021
 */
public class ReceiptItem {
	/**
	 * The product this receipt item represents
	 */
	public final Product product;

	/**
	 * The total price of the product
	 */
	public final BigDecimal totalPrice;
	/**
	 * The weight in grams
	 */
	public final double weightInGrams;
	/**
	 * If the product is sold by weight, the price per kilogram, if the product is
	 * sold by unit, the price per unit
	 */
	public final BigDecimal pricePerKilogram;

	/**
	 * @param product
	 *            The product this receipt item represents
	 * @param totalPrice
	 *            The total price of the product
	 * @param weightInGrams
	 *            the weight in grams
	 * @param pricePerUnit
	 *            If the product is sold by weight, the price per kilogram, if the
	 *            product is sold by unit, the price per unit
	 */
	public ReceiptItem(Product product, BigDecimal totalPrice, double weightInGrams, BigDecimal pricePerUnit) {
		this.product = product;
		this.totalPrice = totalPrice;
		this.weightInGrams = weightInGrams;
		this.pricePerKilogram = pricePerUnit;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		int space = 40;
		String desc;
		if (product instanceof BarcodedProduct) {
			desc = ((BarcodedProduct) product).getDescription();

		} else {
			desc = ((PLUCodedProduct) product).getDescription();
		}
		if (desc.length() > space - 4) {
			desc = desc.substring(0, space - 4);
		}
		sb.append(desc);
		int priceInCents = (int) (totalPrice.doubleValue() * 100 + 0.5);
		String dollars = String.valueOf(priceInCents / 100);
		for (int i = 0; i < space - desc.length() - dollars.length(); i++) {
			sb.append(' ');
		}
		sb.append('$');
		sb.append(priceInCents / 100);
		sb.append('.');
		sb.append(priceInCents % 100/10);
		sb.append(priceInCents % 10);
		String weight;
		if (!product.isPerUnit()) {
			sb.append('\n');
			sb.append("    ");
			weight = String.valueOf(weightInGrams / 1000);
			sb.append(weight);
			sb.append('g');
			for (int i = 0; i < space - 5 - weight.length() - 15; i++) {
				sb.append(' ');
			}
			sb.append('$');
			sb.append(pricePerKilogram);
			sb.append("/kg");
		}

		return sb.toString();
	}
}
