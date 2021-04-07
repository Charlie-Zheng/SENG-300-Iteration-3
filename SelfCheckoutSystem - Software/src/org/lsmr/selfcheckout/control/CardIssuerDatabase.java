/**
 * 
 */
package org.lsmr.selfcheckout.control;

import java.util.HashSet;
import java.util.Set;

import org.lsmr.selfcheckout.external.CardIssuer;

/**
 * @author charl
 * @date Apr. 6, 2021
 */
public class CardIssuerDatabase {
	/**
	 * This stores the set of card issuers
	 */
	public static final Set<CardIssuer> DEBIT_ISSUER_DATABASE = new HashSet<CardIssuer>();
	public static final Set<CardIssuer> CREDIT_ISSUER_DATABASE = new HashSet<CardIssuer>();
	public static final Set<CardIssuer> GIFT_ISSUER_DATABASE = new HashSet<CardIssuer>();
}
