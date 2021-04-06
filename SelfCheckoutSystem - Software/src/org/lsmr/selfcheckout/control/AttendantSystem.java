package org.lsmr.selfcheckout.control;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.PLUCodedItem;
import org.lsmr.selfcheckout.external.ProductDatabases;

public class AttendantSystem {
	// stations numbered from 0 ... stations.length-1
	public ArrayList<Checkout> stations; 
	public HashMap<Integer,Attendant> attendants; 
	public Attendant currentEmployee = null;
	private enum ConsoleState{
		LoggedIn,LoggedOut
	};
	private ConsoleState state = ConsoleState.LoggedOut;
	
	public AttendantSystem(HashMap<Integer,Attendant> attendants) {
		this.stations = new ArrayList<Checkout>();
		this.attendants = attendants; 
	}
	
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
	public void logout() {
		this.currentEmployee = null; 
		this.state = ConsoleState.LoggedOut;
	}
	
	public int register(Checkout station) throws CheckoutException {
		if(station.equals(null)) throw new CheckoutException("You tried to register a station that does not exist.");
			int stationNum = this.stations.size();
			this.stations.add(station);
			return stationNum; 
	}
	
	public void removeItem(int stationNum, Item itemToRemove) throws CheckoutException {	
		if(this.state == ConsoleState.LoggedIn) {
			Checkout station = this.stations.get(stationNum);
			if(station.equals(null)) throw new CheckoutException("This station does not exist!");
			// Removes item and checks if the item was actually in the itemsScanned to remove it from the bagging area expected weight and balance
			if(station.itemsScanned.remove(itemToRemove)) {
				if(itemToRemove instanceof BarcodedItem) {
					BarcodedItem itemToRemoveBarcoded = (BarcodedItem) itemToRemove; 
					BigDecimal price = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(itemToRemoveBarcoded.getBarcode()).getPrice(); 
					double itemWeightNegative = 0.0 - ProductWeightDatabase.PRODUCT_WEIGHT_DATABASE.get(itemToRemoveBarcoded.getBarcode());
					// Remove item from scale
					station.addExpectedWeightOnScale(itemWeightNegative);
					station.subtractBalance(price);
					// subtract item price from bill 
				}
				else if(itemToRemove instanceof PLUCodedItem) {
					// same as above with new weight database
				}
			}
		}
	}
	
	public void lookup() {
		if(this.state == ConsoleState.LoggedIn) {
			// same as customer
			
		}
	}
	
	public void approveWeight(int stationNum) throws CheckoutException {
		if(this.state == ConsoleState.LoggedIn) {
			Checkout station = this.stations.get(stationNum);
			if(station.equals(null)) throw new CheckoutException("This station does not exist!");
			// if station is blocked from a weight discrepancy remove block and set station state to Scanning
		}
	}
	
	public void startUp(int stationNum) throws CheckoutException {
		if(this.state == ConsoleState.LoggedIn) {
			Checkout station = this.stations.get(stationNum); 
			if(station.equals(null)) throw new CheckoutException("This station does not exist!");
			// Change state of checkout to ON? Might need to add a new state for checkout to determine if it is on or not. 
		}
	}
	
	public void shutDown(int stationNum) throws CheckoutException {
		if(this.state == ConsoleState.LoggedIn) {
			Checkout station = this.stations.get(stationNum);
			if(station.equals(null)) throw new CheckoutException("This station does not exist!");
			// Change state of checkout to OFF? Might need to add a new state for checkout to determine if it is on or not. 
		}
	}
}



















