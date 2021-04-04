/**
 * 
 */
package org.lsmr.selfcheckout.control;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.lsmr.selfcheckout.devices.SelfCheckoutStation;

/**
 * @author Group U08-2
 * @date Mar 28, 2021
 */
public class GiveChange {
	SelfCheckoutStation checkoutStation;
	Map<BigDecimal, Integer> coinsToGive = new HashMap<BigDecimal, Integer>();
	Map<Integer, Integer> notesToGive = new HashMap<Integer, Integer>();

	public GiveChange(SelfCheckoutStation checkout) {
		this.checkoutStation = checkout;
		reset();
	}

	private void reset() {
		coinsToGive.clear();
		notesToGive.clear();
		for (BigDecimal coinValue : checkoutStation.coinDenominations) {
			coinsToGive.put(coinValue, 0);
		}
		for (Integer noteValue : checkoutStation.banknoteDenominations) {
			notesToGive.put(noteValue, 0);
		}

	}

	/**
	 * Calculates the change needed to return for the given balance. This will give
	 * change such that the value of the change is the smallest possible while also
	 * giving back enough change. (E.G., need to give back $0.02 but the smallest
	 * denomination is $0.05, the checkout will give $0.05 in change). Additionally,
	 * this will then try to give out the fewest number of bills+coins so customers
	 * are not burdened with excess coinage/bills.Use
	 * <p>
	 * .getCoinsToGive() to get the number of coins to return, and .getNotesToGive()
	 * to get the number of notes to return
	 * 
	 * @param currentBalance
	 *            The current balance
	 */
	public void calculateChange(BigDecimal currentBalance) {
		reset();
		//convert all currencies to number of cents (so calculations can be done with integers)
		int balanceInt = -currentBalance.multiply(new BigDecimal(100)).toBigInteger().intValue();

		//use dynamic programming to find how much of each denomination of coin/banknote is needed
		ArrayList<Integer> denoms = new ArrayList<Integer>();

		//need the value of the smallestDenomination
		int smallestDenom = Integer.MAX_VALUE;

		//Aggregate the denominations of coins and banknotes
		for (BigDecimal denom : checkoutStation.coinDenominations) {
			//convert all currencies to number of cents (so calculations can be done with integers)
			denoms.add(denom.multiply(new BigDecimal(100)).toBigInteger().intValue());
			if (smallestDenom > denoms.get(denoms.size() - 1)) {
				smallestDenom = denoms.get(denoms.size() - 1);
			}
		}

		for (int denom : checkoutStation.banknoteDenominations) {
			//convert all currencies to number of cents (so calculations can be done with integers)
			denoms.add(denom * 100);
			if (smallestDenom > denom * 100) {
				smallestDenom = denom * 100;
			}
		}

		//store the number of returned banknotes+coins that is the smallest
		int[] numberOfCurReturned = new int[balanceInt + smallestDenom];

		//store the origin of where the dynamic programming used to get the new value
		int[] source = new int[balanceInt + smallestDenom];

		//initialize the numerOfCurRerturned to be very high. Use Integer.MAX_VALUE/2 to avoid int overflow when adding.
		for (int i = 0; i < balanceInt + smallestDenom; i++) {
			numberOfCurReturned[i] = Integer.MAX_VALUE / 2;
		}

		//the numberOfCurReturned for the base denominations is 1
		for (int denom : denoms) {
			if (denom < balanceInt + smallestDenom)
				numberOfCurReturned[denom] = 1;
		}

		// let f(x) be min number of coins+bills needed for X cents  
		// f(x) = min(f(x-denom[0])+1, f(x-denom[1])+1, ... )
		//
		for (int i = 0; i < balanceInt + smallestDenom; i++) {
			int smallest = numberOfCurReturned[i], smallestSource = 0;
			for (int denom : denoms) {
				if (i - denom >= 0 && numberOfCurReturned[i - denom] + 1 < smallest) {
					smallest = numberOfCurReturned[i - denom] + 1;
					smallestSource = i - denom;
				}
			}
			numberOfCurReturned[i] = smallest;
			source[i] = smallestSource;
		}

		// finds the smallest possible amount of change that needs to be returned.
		// If the value is the initial value, then cannot give that amount of change
		int amountToReturn = balanceInt;
		while (numberOfCurReturned[amountToReturn] == Integer.MAX_VALUE / 2) {
			amountToReturn++;
		}

		// A hashtable of how much of each denomination much be returned
		Hashtable<Integer, Integer> currToReturn = new Hashtable<Integer, Integer>();

		// trace the denoms returned all the way back to 0.
		while (amountToReturn != 0) {
			int note = amountToReturn - source[amountToReturn];
			if (!currToReturn.containsKey(note)) {
				currToReturn.put(note, 0);
			}
			currToReturn.put(note, currToReturn.get(note) + 1);
			amountToReturn = source[amountToReturn];
		}

		// For each denom that needs to be returned
		for (Integer curr : currToReturn.keySet()) {
			//if the denom is a banknote denom,
			if (curr % 100 == 0 && checkoutStation.banknoteDispensers.containsKey(curr / 100)) {
				//convert cents back to dollars
				int noteCurr = curr / 100;
				notesToGive.put(noteCurr, currToReturn.get(curr));
			} else {
				//otherwise the denom is a coin denom.

				//convert cents back to dollars
				BigDecimal coinCurr = new BigDecimal(curr).divide(new BigDecimal(100));

				//get the exact value of the coin denomination. Because 2.0 != 2.00 for BigDecimals, must get the same one used originally. 
				for (BigDecimal c : checkoutStation.coinDenominations) {
					if (c.compareTo(coinCurr) == 0) {
						coinCurr = c;
						break;
					}
				}

				coinsToGive.put(coinCurr, currToReturn.get(curr));
			}
		}
	}

	/**
	 * @return the coinsToGive
	 */
	public Map<BigDecimal, Integer> getCoinsToGive() {
		return new HashMap<BigDecimal, Integer>(coinsToGive);
	}

	/**
	 * @return the notesToGive
	 */
	public Map<Integer, Integer> getNotesToGive() {
		return new HashMap<Integer, Integer>(notesToGive);
	}
}
