package org.lsmr.selfcheckout.control.test;

import org.junit.Test;
import org.lsmr.selfcheckout.Card;
import org.lsmr.selfcheckout.control.Checkout;
import org.lsmr.selfcheckout.control.CheckoutException;

public class MembershipCardTest extends BaseTest {

	@Test
	public void testTapping() {
		for (int i = 0; i < REPEAT; i++) {
			Checkout c = makeNewDefaultCheckout();

			Card testCard = new Card("Membership", "123456789", "R.Walker", null, null, true, false);
			try {
				c.tapCard(testCard);
				success();
			} catch (CheckoutException e) {
				fail();
			}
		}
		for (int i = 0; i < REPEAT; i++) {
			Checkout c = makeNewDefaultCheckout();

			Card testCard = new Card("credit", "123456789", "R.Walker", null, null, false, false);
			try {
				c.tapCard(testCard);
				success();
			} catch (CheckoutException e) {
				fail();
			}
		}
	}
	@Test
	public void testSwiping() {
		for (int i = 0; i < REPEAT; i++) {
			Checkout c = makeNewDefaultCheckout();

			Card testCard = new Card("Membership", "123456789", "R.Walker", null, null, true, false);
			try {
				c.swipeCard(testCard);
				success();
			} catch (CheckoutException e) {
				fail();
			}
		}
		for (int i = 0; i < REPEAT; i++) {
			Checkout c = makeNewDefaultCheckout();

			Card testCard = new Card("credit", "123456789", "R.Walker", null, null, false, false);
			try {
				c.swipeCard(testCard);
				success();
			} catch (CheckoutException e) {
				fail();
			}
		}
	}

	@Test
	public void testTapNotMembership() {
		for (int i = 0; i < REPEAT; i++) {
			Checkout c = makeNewDefaultCheckout();

			Card testCard = new Card("credit", "123456789", "R.Walker", null, null, true, false);
			try {
				c.tapCard(testCard);
				multiTestAssertEquals(true, c.getLoggedInMemberName() == null);
				multiTestAssertEquals(true, c.getLoggedInMemberNumber() == null);
			} catch (CheckoutException e) {
				fail();
			}
		}
		for (int i = 0; i < REPEAT; i++) {
			Checkout c = makeNewDefaultCheckout();

			Card testCard = new Card("debit", "6541236", "M. Joe", "231", "1234", true, true);
			try {
				c.tapCard(testCard);
				multiTestAssertEquals(true, c.getLoggedInMemberName() == null);
				multiTestAssertEquals(true, c.getLoggedInMemberNumber() == null);
			} catch (CheckoutException e) {
				fail();
			}
		}
	}

	@Test
	public void testSwipeNotMembership() {
		for (int i = 0; i < REPEAT; i++) {
			Checkout c = makeNewDefaultCheckout();

			Card testCard8 = new Card("Credit", "123456789", "R.Walker", null, null, true, false);
			try {
				c.swipeCard(testCard8);
				multiTestAssertEquals(true, c.getLoggedInMemberName() == null);
				multiTestAssertEquals(true, c.getLoggedInMemberNumber() == null);
			} catch (CheckoutException e) {
				fail();
			}
		}
	}

	@Test
	public void testSwipeMembership() {
		for (int i = 0; i < REPEAT; i++) {
			try {
				Checkout c = makeNewDefaultCheckout();
				Card testCard8 = new Card("Membership", "123456789", "R.Walker", null, null, true, false);
				c.swipeCard(testCard8);
				multiTestAssertEquals(true, c.getLoggedInMemberName().equals("R.Walker"));
				multiTestAssertEquals(true, c.getLoggedInMemberNumber().equals("123456789"));
			} catch (CheckoutException | NullPointerException e) {
				fail();
			}
		}
	}
	@Test
	public void testTapMembership() {
		for (int i = 0; i < REPEAT; i++) {
			try {
				Checkout c = makeNewDefaultCheckout();
				Card testCard8 = new Card("Membership", "123456789", "R.Walker", null, null, true, false);
				c.tapCard(testCard8);
				multiTestAssertEquals(true, c.getLoggedInMemberName().equals("R.Walker"));
				multiTestAssertEquals(true, c.getLoggedInMemberNumber().equals("123456789"));
			} catch (CheckoutException | NullPointerException e) {
				fail();
			}
		}
	}
	@Test
	public void testSwipeMembershipNotInDB() {
		for (int i = 0; i < REPEAT; i++) {
			try {
				Checkout c = makeNewDefaultCheckout();
				Card testCard8 = new Card("Membership", "00000", "R.Walker", null, null, true, false);
				c.swipeCard(testCard8);
				multiTestAssertEquals(true, c.getLoggedInMemberName()==null);
				multiTestAssertEquals(true, c.getLoggedInMemberNumber()==null);
			} catch (CheckoutException e) {
				fail();
			}
		}
	}
	@Test
	public void testTapMembershipNotInDB() {
		for (int i = 0; i < REPEAT; i++) {
			try {
				Checkout c = makeNewDefaultCheckout();
				Card testCard8 = new Card("Membership", "0001200", "R.Walker", null, null, true, false);

				
				c.tapCard(testCard8);
				multiTestAssertEquals(true, c.getLoggedInMemberName()==null);
				multiTestAssertEquals(true, c.getLoggedInMemberNumber()==null);
			} catch (CheckoutException e) {
				fail();
			}
		}
	}
	@Test
	public void testSwipeMemberAlreadyLoggedIn() {
		for (int i = 0; i < REPEAT; i++) {
			try {
				Checkout c = makeNewDefaultCheckout();
				Card testCard0 = new Card("Membership", "123456789", "R.Walker", null, null, true, false);
				Card testCard1 = new Card("Membership", "11111111", "C.Nolan", null, null, true, false);
				c.swipeCard(testCard0);
				c.swipeCard(testCard1);
				//the member should remain the first logged in member
				multiTestAssertEquals(true, c.getLoggedInMemberName().equals("R.Walker"));
				multiTestAssertEquals(true, c.getLoggedInMemberNumber().equals("123456789"));
			} catch (CheckoutException e) {
				fail();
			}
		}
	}
	@Test
	public void testTapMemberAlreadyLoggedIn() {
		for (int i = 0; i < REPEAT; i++) {
			try {
				Checkout c = makeNewDefaultCheckout();
				Card testCard0 = new Card("Membership", "123456789", "R.Walker", null, null, true, false);
				Card testCard1 = new Card("Membership", "11111111", "C.Nolan", null, null, true, false);
				c.swipeCard(testCard0);
				c.tapCard(testCard1);
				//the member should remain the first logged in member
				multiTestAssertEquals(true, c.getLoggedInMemberName().equals("R.Walker"));
				multiTestAssertEquals(true, c.getLoggedInMemberNumber().equals("123456789"));
			} catch (CheckoutException e) {
				fail();
			}
		}
	}
}
