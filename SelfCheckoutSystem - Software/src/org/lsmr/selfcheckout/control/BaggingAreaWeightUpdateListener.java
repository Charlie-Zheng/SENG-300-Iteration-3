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
 * @author Group U08-2
 * @date Mar 12, 2021
 */
public class BaggingAreaWeightUpdateListener implements ElectronicScaleListener {
	private Checkout checkout;

	public BaggingAreaWeightUpdateListener(Checkout checkout) {
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
		checkout.setWeightOnScale(weightInGrams);
	}

	@Override
	public void overload(ElectronicScale scale) {
		checkout.setWeightOnScale(Float.NaN);
	}

	@Override
	public void outOfOverload(ElectronicScale scale) {
		try {
			checkout.setWeightOnScale(scale.getCurrentWeight());
		} catch (OverloadException e) {
			System.err.println("Out of overload scale should not throw Overload Exception");
			e.printStackTrace();
		}

	}

}
