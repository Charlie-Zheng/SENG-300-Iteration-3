/**
 * 
 */
package org.lsmr.selfcheckout.control;

import java.math.BigDecimal;

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

}
