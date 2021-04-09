package org.lsmr.selfcheckout.control;

import java.math.BigDecimal;
import java.util.Currency;

import org.lsmr.selfcheckout.devices.SelfCheckoutStation;

public class MainClass {
	public static void main(String[] args) {
		Currency cad = Currency.getInstance("CAD");
		int[] banknoteDenominations = { 5, 10, 20, 50, 100 };
		BigDecimal[] coinDenominations = { new BigDecimal("0.05"), new BigDecimal("0.10"), new BigDecimal("0.25"),
				new BigDecimal("1.00"), new BigDecimal("2.00") };
		SelfCheckoutStation station = new SelfCheckoutStation(cad, banknoteDenominations, coinDenominations, 122, 1);
		Checkout c = new Checkout(station);
		c.run();
	}
}
