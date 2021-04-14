/**
 * 
 */
package org.lsmr.selfcheckout.control.gui.statedata;

import org.lsmr.selfcheckout.control.gui.Pair;

/**
 * @author charl
 * @date Apr. 13, 2021
 */
public class AttendantLogInData implements StateData<Pair<Integer, Integer>> {
	protected int num;
	protected int pin;
	protected boolean success;
	public AttendantLogInData(int empNum, int empPin) {
		num = empNum;
		pin = empPin;
		success = false;
	}
	
	public void success() {
		success = true;
	}
	
	public boolean isSuccessful() {
		return success;
	}
	
	@Override
	public Pair<Integer, Integer> obtain() {
		return new Pair<Integer, Integer>(num, pin);
	}

}
