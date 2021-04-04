/**
 * 
 */
package org.lsmr.selfcheckout.control;

import java.util.Map;
import java.util.Stack;

import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.BanknoteDispenser;
import org.lsmr.selfcheckout.devices.BanknoteSlot;
import org.lsmr.selfcheckout.devices.DisabledException;
import org.lsmr.selfcheckout.devices.EmptyException;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.listeners.AbstractDeviceListener;
import org.lsmr.selfcheckout.devices.listeners.BanknoteSlotListener;

/**
 * @author Group U08-2
 * @date Mar 26, 2021
 */
public class BanknoteDispenserSlotListener implements BanknoteSlotListener {

	private Stack<Integer> numToEmit = new Stack<Integer>();
	private Checkout checkout;
	private Map<Integer, BanknoteDispenser> noteDispensers;
	public BanknoteDispenserSlotListener(Checkout c, Map<Integer, BanknoteDispenser> noteDispensers) {
		checkout = c;
		this.noteDispensers = noteDispensers;
	}

	protected void addBanknoteToEmit(int n) {
		numToEmit.add(n);
		if(numToEmit.size() == 1) {
			try {
				noteDispensers.get(numToEmit.peek()).emit();
			} catch (EmptyException | DisabledException | OverloadException e) {
			}
		}
	
	}

	@Override
	public void enabled(AbstractDevice<? extends AbstractDeviceListener> device) {

	}

	@Override
	public void disabled(AbstractDevice<? extends AbstractDeviceListener> device) {

	}

	@Override
	public void banknoteInserted(BanknoteSlot slot) {

	}

	@Override
	public void banknoteEjected(BanknoteSlot slot) {
		checkout.addBalanceCurr(numToEmit.pop());
	}

	@Override
	public void banknoteRemoved(BanknoteSlot slot) {
		if (!numToEmit.empty()) {
			try {
				noteDispensers.get(numToEmit.peek()).emit();
			} catch (EmptyException | DisabledException | OverloadException e) {
			}
		}
	}

}
