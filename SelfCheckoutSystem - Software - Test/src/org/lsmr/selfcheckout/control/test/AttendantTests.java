package org.lsmr.selfcheckout.control.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;

import org.junit.Test;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.control.Attendant;
import org.lsmr.selfcheckout.control.AttendantSystem;
import org.lsmr.selfcheckout.control.Checkout;
import org.lsmr.selfcheckout.control.CheckoutException;
import org.lsmr.selfcheckout.control.ReceiptItem;
import org.lsmr.selfcheckout.devices.CoinStorageUnit;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SimulationException;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.Product;

public class AttendantTests extends BaseTest {

	
	

	
	@Test
	public void attendantLogin() throws CheckoutException {
		for (int i = 0; i < REPEAT; i++) {
			c.reset();
			Integer num = new Integer(1019);
			Attendant br = new Attendant(num, "Brian",2002);
			HashMap<Integer, Attendant> attendants = new HashMap<Integer,Attendant>(){{put(num, br);}};
			AttendantSystem sys = new AttendantSystem(attendants);	
			boolean bool = sys.login(num,2002);
			multiTestAssertEquals(true, bool);
		}
	}
	@Test
	public void attendantLoginButInvalidPin() throws CheckoutException {
		for (int i = 0; i < REPEAT; i++) {
			c.reset();
			Integer num = new Integer(1019);
			Attendant br = new Attendant(1019, "Brian", 2002);
			HashMap attendants = new HashMap<Integer,Attendant>(){{put(num, br);}};
			AttendantSystem sys = new AttendantSystem(attendants);
			boolean bool = sys.login(num,2005);
			multiTestAssertEquals(false, bool);	
		}
	}

	@Test
	public void attendantLoginButInvalidEmployeeNumber() throws CheckoutException {
		for (int i = 0; i < REPEAT; i++) {
			c.reset();
			Integer num = new Integer(1019);
			Attendant br = new Attendant(1019, "Brian", 2002);
			HashMap attendants = new HashMap<Integer,Attendant>(){{put(num, br);}};
			AttendantSystem sys = new AttendantSystem(attendants);
			boolean bool = sys.login(2002,2005);
			multiTestAssertEquals(false, bool);
			
		}
	}	

	@Test
	public void attendantLogout() throws CheckoutException {
		for (int i = 0; i < REPEAT; i++) {
			c.reset();
			Integer num = new Integer(1019);
			Attendant br = new Attendant(num, "Brian",2002);
			HashMap<Integer, Attendant> attendants = new HashMap<Integer,Attendant>(){{put(num, br);}};
			AttendantSystem sys = new AttendantSystem(attendants);	
			sys.login(num,2002);
			sys.logout();
			multiTestAssertEquals("LoggedOut", sys.getState());
		}
	}

	
	@Test
	public void attendantStartup() throws CheckoutException {
		for (int i = 0; i < REPEAT; i++) {
			c.reset();
			Integer num = new Integer(1019);
			Attendant br = new Attendant(num, "Brian",2002);
			HashMap<Integer, Attendant> attendants = new HashMap<Integer,Attendant>(){{put(num, br);}};
			AttendantSystem sys = new AttendantSystem(attendants);	
			boolean bool = sys.login(num,2002);
			int stationNum = sys.register(c);
			sys.startUp(stationNum);
			//  assertEquals(c.state, ON); | will fix this once the state is properly implemented
			sys.shutDown(stationNum);

		}
	}

	
	@Test
	public void attendantStartupButNotLoggedIn() throws CheckoutException {
		for (int i = 0; i < REPEAT; i++) {
			c.reset();
			Integer num = new Integer(1019);
			Attendant br = new Attendant(num, "Brian",2002);
			HashMap<Integer, Attendant> attendants = new HashMap<Integer,Attendant>(){{put(num, br);}};
			AttendantSystem sys = new AttendantSystem(attendants);	
			int stationNum = sys.register(c);
			sys.startUp(stationNum);
			
			 // exception to be made
			sys.shutDown(stationNum);

		}
	}
	
	@Test
	public void attendantShutdown() throws CheckoutException {
		for (int i = 0; i < REPEAT; i++) {
			c.reset();
			Integer num = new Integer(1019);
			Attendant br = new Attendant(num, "Brian",2002);
			HashMap<Integer, Attendant> attendants = new HashMap<Integer,Attendant>(){{put(num, br);}};
			AttendantSystem sys = new AttendantSystem(attendants);	
			boolean bool = sys.login(num,2002);
			int stationNum = sys.register(c);
			sys.shutDown(stationNum);
			 //  assertEquals(c.state, OFF); | will fix this once the state is properly implemented
		}
	}
	
	@Test
	public void attendantShutdownButNotLoggedIn() throws CheckoutException {
		for (int i = 0; i < REPEAT; i++) {
			c.reset();
			Integer num = new Integer(1019);
			Attendant br = new Attendant(num, "Brian",2002);
			HashMap<Integer, Attendant> attendants = new HashMap<Integer,Attendant>(){{put(num, br);}};
			AttendantSystem sys = new AttendantSystem(attendants);	
			int stationNum = sys.register(c);
			sys.shutDown(stationNum);
			 // exception to be made
		}
	}
	
	@Test
	public void attendantApproveWeight() throws CheckoutException {
		for (int i = 0; i < REPEAT; i++) {
			c.reset();
			Integer num = new Integer(1019);
			Attendant br = new Attendant(num, "Brian",2002);
			HashMap<Integer, Attendant> attendants = new HashMap<Integer,Attendant>(){{put(num, br);}};
			AttendantSystem sys = new AttendantSystem(attendants);	
			sys.login(num,2002);
			int stationNum = sys.register(c);
			sys.startUp(stationNum);
			sys.approveWeight(stationNum);
			sys.shutDown(stationNum);
			multiTestAssertEquals(c.getState(), "Scanning");
		}
	}
	@Test
	public void attendantRemoveItem() throws CheckoutException {
		for (int i = 0; i < REPEAT; i++) {
			c.reset();
			Integer num = new Integer(1019);
			Attendant br = new Attendant(num, "Brian",2002);
			HashMap<Integer, Attendant> attendants = new HashMap<Integer,Attendant>(){{put(num, br);}};
			AttendantSystem sys = new AttendantSystem(attendants);	
			sys.login(num,2002);
			int stationNum = sys.register(c);
			sys.startUp(stationNum);
			Barcode b = new Barcode("8888");
			Product p = new BarcodedProduct(b, "fake item" ,BigDecimal.valueOf(5));
			BigDecimal bal = c.getBalance();
			ReceiptItem itemToRemove = new ReceiptItem(p, BigDecimal.valueOf(5), stationNum, BigDecimal.valueOf(5));
			sys.removeItem(stationNum, itemToRemove);
			BigDecimal newBal = bal.subtract(BigDecimal.valueOf(5));
			
			multiTestAssertEquals(newBal,bal.subtract(BigDecimal.valueOf(5)));
		}
	}
	
	
	@Test
	public void attendantAddPaper() throws CheckoutException {
			c.reset();
			Integer num = new Integer(1019);
			Attendant br = new Attendant(num, "Brian",2002);
			HashMap<Integer, Attendant> attendants = new HashMap<Integer,Attendant>(){{put(num, br);}};
			AttendantSystem sys = new AttendantSystem(attendants);	
			boolean bool = sys.login(num,2002);
			int stationNum = sys.register(c);
			c.addPaper(5);
			multiTestAssertEquals(5,c.getPaperTotal());
		
	}
	
	@Test(expected = SimulationException.class)
	public void attendantAddPaperNegative() throws CheckoutException {
			c.reset();
			Integer num = new Integer(1019);
			Attendant br = new Attendant(num, "Brian",2002);
			HashMap<Integer, Attendant> attendants = new HashMap<Integer,Attendant>(){{put(num, br);}};
			AttendantSystem sys = new AttendantSystem(attendants);	
			boolean bool = sys.login(num,2002);
			int stationNum = sys.register(c);
			c.addPaper(-50);
		
	}	
	
	@Test(expected = SimulationException.class)
	public void attendantAddPaper50() throws CheckoutException {
		for (int i = 0; i < REPEAT; i++) {
			c.reset();
			Integer num = new Integer(1019);
			Attendant br = new Attendant(num, "Brian",2002);
			HashMap<Integer, Attendant> attendants = new HashMap<Integer,Attendant>(){{put(num, br);}};
			AttendantSystem sys = new AttendantSystem(attendants);	
			boolean bool = sys.login(num,2002);
			int stationNum = sys.register(c);
			c.addPaper(50);
		
	
		}	
	}

	@Test
	public void attendantAddInk() throws CheckoutException {
			c.reset();
			Integer num = new Integer(1019);
			Attendant br = new Attendant(num, "Brian",2002);
			HashMap<Integer, Attendant> attendants = new HashMap<Integer,Attendant>(){{put(num, br);}};
			AttendantSystem sys = new AttendantSystem(attendants);	
			boolean bool = sys.login(num,2002);
			int stationNum = sys.register(c);
			c.addInk(50);
			multiTestAssertEquals(50,c.getInkTotal());
		
	}
	@Test(expected = SimulationException.class)
	public void attendantAddInkNegative() throws CheckoutException {
			c.reset();
			Integer num = new Integer(1019);
			Attendant br = new Attendant(num, "Brian",2002);
			HashMap<Integer, Attendant> attendants = new HashMap<Integer,Attendant>(){{put(num, br);}};
			AttendantSystem sys = new AttendantSystem(attendants);	
			boolean bool = sys.login(num,2002);
			int stationNum = sys.register(c);
			c.addInk(-50);
		
	}	

	@Test
	public void attendantEmptyCoinStorage() throws Exception {
		for (int i = 0; i < REPEAT; i++) {
			c.reset();
			c.emptyCoinStorage();
			multiTestAssertEquals(0, c.getCoinCount());
		}
		
		
		
	}
	
	@Test
	public void attendantEmptyBanknoteStorage() throws Exception {
		for (int i = 0; i < REPEAT; i++) {
			c.reset();
			c.emptyBanknoteStorage();
			
			multiTestAssertEquals(0, c.getNoteCount());
		}
		
		
		
	}
		
}