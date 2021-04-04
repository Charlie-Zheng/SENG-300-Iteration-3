/**
 * 
 */
package org.lsmr.selfcheckout.control;

import java.math.BigDecimal;
import java.util.Currency;

import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.BanknoteValidator;
import org.lsmr.selfcheckout.devices.listeners.AbstractDeviceListener;
import org.lsmr.selfcheckout.devices.listeners.BanknoteValidatorListener;

/**
 * @author Group U08-2
 * @date Mar 12, 2021
 */
public class BanknoteValidatorBalanceUpdateListener implements BanknoteValidatorListener{
	private Checkout checkout;
	public BanknoteValidatorBalanceUpdateListener(Checkout checkout) {
		this.checkout = checkout;
	}
	
	@Override
	public void enabled(AbstractDevice<? extends AbstractDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disabled(AbstractDevice<? extends AbstractDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validBanknoteDetected(BanknoteValidator validator, Currency currency, int value) {
		checkout.subtractBalance(value);		
	}

	@Override
	public void invalidBanknoteDetected(BanknoteValidator validator) {
		
	}



}
