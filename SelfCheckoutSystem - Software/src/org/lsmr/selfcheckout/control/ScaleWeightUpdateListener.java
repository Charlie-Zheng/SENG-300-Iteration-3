/**
 * 
 */
package org.lsmr.selfcheckout.control;

import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.ElectronicScale;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.listeners.AbstractDeviceListener;
import org.lsmr.selfcheckout.devices.listeners.ElectronicScaleListener;

/**
 * @author Group V08-1
 * @date Apr 6, 2021
 */
public class ScaleWeightUpdateListener implements ElectronicScaleListener {
	private Checkout checkout;

	public ScaleWeightUpdateListener(Checkout checkout) {
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
	public void weightChanged(ElectronicScale scale, double weightInGrams) {
		checkout.setWeightOnScanScale(weightInGrams);
	}

	@Override
	public void overload(ElectronicScale scale) {
		checkout.setWeightOnScanScale(Float.NaN);
	}

	@Override
	public void outOfOverload(ElectronicScale scale) {
		try {
			checkout.setWeightOnScanScale(scale.getCurrentWeight());
		} catch (OverloadException e) {
			System.err.println("Out of overload scale should not throw Overload Exception");
			e.printStackTrace();
		}

	}

}
