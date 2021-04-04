/**
 * 
 */
package org.lsmr.selfcheckout.control;

/**
 * @author Group U08-2
 * @date Mar 12, 2021
 */
public class CheckoutException extends Exception {
	/**
	 * Create an exception without an error message.
	 */
	public CheckoutException() {}

	/**
	 * Create an exception with an error message.
	 * 
	 * @param message
	 *            The error message to use.
	 */
	public CheckoutException(String message) {
		super(message);
	}
}
