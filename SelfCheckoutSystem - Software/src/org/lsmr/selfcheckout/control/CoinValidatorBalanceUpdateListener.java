/**
 * 
 */
package org.lsmr.selfcheckout.control;

import java.math.BigDecimal;

import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.CoinValidator;
import org.lsmr.selfcheckout.devices.listeners.AbstractDeviceListener;
import org.lsmr.selfcheckout.devices.listeners.CoinValidatorListener;

/**
 * @author Group U08-2
 * @date Mar 12, 2021
 */
public class CoinValidatorBalanceUpdateListener implements CoinValidatorListener{
	private Checkout checkout;
	public CoinValidatorBalanceUpdateListener(Checkout checkout) {
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
	public void validCoinDetected(CoinValidator validator, BigDecimal value) {
		checkout.subtractBalance(value);		
	}

	@Override
	public void invalidCoinDetected(CoinValidator validator) {
		
	}

}
