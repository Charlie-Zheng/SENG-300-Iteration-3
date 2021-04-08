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
	public final Product product;
	public final BigDecimal totalPrice;
	public final double weightInGrams;
	public final BigDecimal pricePerKilogram;

	/**
	 * @param product
	 * @param totalPrice
	 * @param weightInGrams
	 * @param pricePerKilogram
	 */
	public ReceiptItem(Product product, BigDecimal totalPrice, double weightInGrams, BigDecimal pricePerKilogram) {
		super();
		this.product = product;
		this.totalPrice = totalPrice;
		this.weightInGrams = weightInGrams;
		this.pricePerKilogram = pricePerKilogram;
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
		sb.append(priceInCents % 100);

		if (!Double.isNaN(weightInGrams)) {
			sb.append('\n');
			sb.append("    ");

			String weight;
			if (!product.isPerUnit()) {
				weight = String.valueOf(weightInGrams / 1000);
				sb.append(weight);
				sb.append('g');
				for (int i = 0; i < space - 5 - weight.length() - 5; i++) {
					sb.append(' ');
				}
				sb.append('$');
				sb.append(pricePerKilogram);
				sb.append("/kg");
			} else {
				weight = String.valueOf((int) weightInGrams);
				sb.append(weight);
				for (int i = 0; i < space - 4 - weight.length() - 15; i++) {
					sb.append(' ');
				}
				sb.append('$');
				sb.append(pricePerKilogram);
				sb.append("/unit");
			}

		}
		return sb.toString();
	}
}
