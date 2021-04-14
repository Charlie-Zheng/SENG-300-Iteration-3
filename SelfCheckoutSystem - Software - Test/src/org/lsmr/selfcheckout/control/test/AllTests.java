/**
 * 
 */
package org.lsmr.selfcheckout.control.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Group U08-2
 * @date Mar 31, 2021
 */
@RunWith(Suite.class)
@SuiteClasses({ AttendantTests.class, BaggingAreaTest.class, CardPaymentTest.class, CashPaymentTest.class, ChangeTest.class,
		CheckoutTest.class, MembershipCardTest.class, PaperInkTest.class, PlasticBagTest.class, ScanItemTest.class, StartPaymentTest.class,
		TestCustomerBag.class, TestCustomerCases.class })
		
public class AllTests {

}
