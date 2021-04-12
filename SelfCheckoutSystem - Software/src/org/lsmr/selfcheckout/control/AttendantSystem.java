package org.lsmr.selfcheckout.control;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.PLUCodedItem;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.external.ProductDatabases;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;
import org.lsmr.selfcheckout.products.Product;

public class AttendantSystem {
	// stations numbered from 0 ... stations.length-1
	public ArrayList<Checkout> stations; 
	// Map to associate attendants with their employee numbers
	public HashMap<Integer,Attendant> attendants; 
	public Attendant currentEmployee = null;
	private enum ConsoleState{
		LoggedIn,LoggedOut
	};
	private ConsoleState state = ConsoleState.LoggedOut;
	
	/**
	 * Constructor for AttendantSystem.
	 * 
	 * @param attendants 
	 *			A map of attendants that have access to the system.
	 * 
	 */
	public AttendantSystem(HashMap<Integer,Attendant> attendants) {
		this.stations = new ArrayList<Checkout>();
		this.attendants = attendants; 
	}
	
	/**
	 * Allows an attendant to attempt to login to the system
	 * 
	 * @param employeeNumber
	 *            the number associated with the employee
	 * @param pin
	 *            the employee's pin linked to their account
	 * @return 
	 * 			true if the login was successful, false otherwise
	 */
	public boolean login(Integer employeeNumber,int pin) {
		Attendant temp = this.attendants.get(employeeNumber); 
		if(temp!= null) {
			if(temp.validatePin(pin)) {
				this.currentEmployee = temp; 
				this.state = ConsoleState.LoggedIn;
				// successful login
				return true; 
			}
		}
		// invalid pin or employee number
		return false; 
	}
	
	/**
	 * Allows the currently logged in attendant to log out.
	 * 
	 */
	public void logout() {
		this.currentEmployee = null; 
		this.state = ConsoleState.LoggedOut;
	}
	
	/**
	 * Registers a checkout to this AttendantSystem.
	 * 
	 * @param station
	 *            the checkout system to register.
	 * @return
	 * 			the number associated to this checkout. 
	 * @throws CheckoutException
	 *             if the station is not valid
	 */
	public int register(Checkout station) throws CheckoutException {
		if(station.equals(null)) throw new CheckoutException("You tried to register a station that does not exist.");
			int stationNum = this.stations.size();
			this.stations.add(station);
			return stationNum; 
	}
	
	/**
	 * Removes an item from the customers cart at a given station. 
	 * 
	 * @param stationNum
	 *            the number associated to the customer's station
	 * @param itemToRemove
	 *            the item to remove
	 * @throws CheckoutException
	 *             if the station is not valid
	 */
	public void removeItem(int stationNum, ReceiptItem itemToRemove) throws CheckoutException {	
		if(this.state == ConsoleState.LoggedIn) {
			Checkout station = this.stations.get(stationNum);
			if(station.equals(null)) throw new CheckoutException("This station does not exist!");
			station.deleteProductAdded(itemToRemove); 
		}
	}
	
	/**
	 * 
	 * 
	 * @param name
	 *            the name of the product
	 * @return 
	 * 			a list of all products associated with the current name
	 */
	public ArrayList<Product> searchProductDatabase(String name) {
		if(this.state == ConsoleState.LoggedIn) {
	        name = name.toLowerCase();
	        ArrayList<Product> results = new ArrayList<Product>();
	        for (PLUCodedProduct p : ProductDatabases.PLU_PRODUCT_DATABASE.values()) {
	            if (p.getDescription().toLowerCase().contains(name)) {
	                results.add(p);
	            }
	        }
	        for (BarcodedProduct p : ProductDatabases.BARCODED_PRODUCT_DATABASE.values()) {
	            if (p.getDescription().toLowerCase().contains(name)) {
	                results.add(p);
	            }
	        }
	        return results;
		}
		return null; 
    }
	
	/**
	 * Approves a weight discrepancy of a customer's station. 
	 * 
	 * @param stationNum
	 *            the number associated to the station
	 * @throws CheckoutException
	 *             if the station is not valid
	 */
	public void approveWeight(int stationNum) throws CheckoutException {
		if(this.state == ConsoleState.LoggedIn) {
			Checkout station = this.stations.get(stationNum);
			if(station.equals(null)) throw new CheckoutException("This station does not exist!");
			if(station.isPaused()) {
				station.approveWeightDiscrepency();
			}
			// if station is blocked from a weight discrepancy remove block and set station state to Scanning
		}
	}
	
	/**
	 * Starts up the station associated to the station number
	 * 
	 * @param stationNum
	 *            the number associated to the station
	 * @throws CheckoutException
	 *             if the station is not valid
	 */
	public void startUp(int stationNum) throws CheckoutException {
		if(this.state == ConsoleState.LoggedIn) {
			Checkout station = this.stations.get(stationNum); 
			if(station.equals(null)) throw new CheckoutException("This station does not exist!");
			station.powerOn();
			// Change state of checkout to ON? Might need to add a new state for checkout to determine if it is on or not. 
		}
	}
	
	/**
	 * Shuts down the station associated to the station number
	 * 
	 * @param stationNum
	 *            the number associated to the station
	 * @throws CheckoutException
	 *             if the station is not valid
	 */
	public void shutDown(int stationNum) throws CheckoutException {
		if(this.state == ConsoleState.LoggedIn) {
			Checkout station = this.stations.get(stationNum);
			if(station.equals(null)) throw new CheckoutException("This station does not exist!");
			station.shutDown();
			// Change state of checkout to OFF? Might need to add a new state for checkout to determine if it is on or not. 
		}
	}
	
	/**
	 * Approves an item that the customer does not want to bag at a given station. 
	 * 
	 * @param stationNum
	 *            the number associated to the station
	 * @throws CheckoutException
	 *             if the station is not valid
	 */
	public void approveDoNotBagLastItem(int stationNum) throws CheckoutException {
		if(this.state == ConsoleState.LoggedIn) {
			Checkout station = this.stations.get(stationNum);
			if(station != null) station.doNotBagLastItem();
			else throw new CheckoutException("This station does not exist!"); 
		}
	}
	
	public String getState(){return this.state.toString();}

}


