package org.lsmr.selfcheckout.control;

public class Attendant {
	protected Integer employeeNumber;
	protected String employeeName;
	private int pin; 
	/**
	 * Constructs a new Attendant object.
	 * 
	 * @param employeeNumber
	 *            the number associated to the Attendant
	 * @param employeeName
	 *            the name of the Attendant 
	 * @param pin
	 *            the pin to log into the Attendant's account       
	 * @throws CheckoutException
	 *             if the pin is not valid
	 */
	public Attendant(Integer employeeNumber,String employeeName, int pin) throws CheckoutException {
		this.employeeName = employeeName;
		this.employeeNumber = employeeNumber;
		if(String.valueOf(pin).length() == 4) this.pin = pin;
		else {
			throw new CheckoutException("The pin entered to register the attendant is not valid.");
		}
	}
	
	/**
	 * Method to validate a given pin to the pin of the current Attendant.
	 * 
	 * @param pin
	 *			the pin entered. 
	 *@return
	 *			true if the pin matches the pin of this Attendant, false otherwise
	 * 
	 */
	public boolean validatePin(int pin) {
		if (this.pin == pin) return true; 
		return false; 
	}
}
