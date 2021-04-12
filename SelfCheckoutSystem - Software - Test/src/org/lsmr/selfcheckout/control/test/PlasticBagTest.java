package org.lsmr.selfcheckout.control.test;

import java.math.BigDecimal;

import org.junit.Test;
import org.lsmr.selfcheckout.control.Checkout;

/**
 * @author Group 08
 * @date April 14, 2021
 */

public class PlasticBagTest extends BaseTest {
	
	/**
	 * Tests the basic situation where the price on plastic bags 
	 * are set. 
	 */
	@Test
	public void PlasticBagPriceTest() {
		for (int i = 0; i < REPEAT; i++) {
		c.reset();
		Checkout.setPricePerPlasticBag(new BigDecimal ("0.10"));
		multiTestAssertEquals(new BigDecimal ("0.10"), Checkout.getPricePerPlasticBag());
		Checkout.setPricePerPlasticBag(new BigDecimal ("0.05"));
		}	
	}
	
	/**
	 * Tests the basic situation where the price on plastic bags are 
	 * added correctly to customer's bill when they add n number of 
	 * bags
	 */
	@Test
	public void UsePlasticBagTest() {
		for (int i = 0; i < REPEAT; i++) {
			c.reset();
			Checkout.setPricePerPlasticBag(new BigDecimal ("0.05"));
			c.usePlasticBags(4);
			multiTestAssertEquals(new BigDecimal("0.20"), c.getBalance());
			}
	}
}
