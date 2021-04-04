package org.lsmr.selfcheckout.control;

/**
 * Helper class that returns a pair of values (Java only allows one return value)
 * @author Vianney Nguyen
 *
 * @param <T>
 * @param <V>
 */
public class Pair<T, V> {
	T type;
	V value;
	
	/**
	 * For returns with only one parameter
	 * @param type
	 */
	public Pair(T type) {
		this.type = type;
		this.value = null;
	}
	
	public Pair(T type, V value) {
		this.type = type;
		this.value = value;
	}
	
	
}
