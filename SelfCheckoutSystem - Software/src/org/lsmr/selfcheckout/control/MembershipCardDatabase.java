package org.lsmr.selfcheckout.control;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a membership database that the simulation can interact with.
 */
public class MembershipCardDatabase {
	/**
	 * This stores the primary account holder for each membership number. 
	 */
	public static final Map<String, String> MEMBERSHIP_CARD_DATABASE = new HashMap<String, String>();

}
