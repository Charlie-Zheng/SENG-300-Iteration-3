package org.lsmr.selfcheckout.control;

public class Attendant {
	protected Integer employeeNumber;
	protected String employeeName;
	private int pin; 
	public Attendant(Integer employeeNumber,String employeeName, int pin) throws CheckoutException {
		this.employeeName = employeeName;
		this.employeeNumber = employeeNumber;
		if(String.valueOf(pin).length() == 4) this.pin = pin;
		else {
			throw new CheckoutException("The pin entered to register the attendant is not valid.");
		}
	}
	public boolean validatePin(int pin) {
		if (this.pin == pin) return true; 
		return false; 
	}
}
