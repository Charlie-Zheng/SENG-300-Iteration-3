package org.lsmr.selfcheckout.control.test;

import static org.junit.Assert.*;

import java.awt.List;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;

import org.junit.Test;
import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.control.Attendant;
import org.lsmr.selfcheckout.control.AttendantSystem;
import org.lsmr.selfcheckout.control.Checkout;
import org.lsmr.selfcheckout.control.CheckoutException;
import org.lsmr.selfcheckout.control.ReceiptItem;
import org.lsmr.selfcheckout.devices.CoinStorageUnit;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SimulationException;
import org.lsmr.selfcheckout.external.ProductDatabases;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;
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
			multiTestAssertEquals("On", c.getpState());
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
			sys.login(num, 2002);
			int stationNum = sys.register(c);
			sys.shutDown(stationNum);
			sys.logout();
			sys.startUp(stationNum);
			multiTestAssertEquals("Off", c.getpState());
			sys.shutDown(stationNum);

		}
	}
	
	@Test(expected = CheckoutException.class)
	public void attendantStartupInvalidStation() throws CheckoutException {
		for (int i = 0; i < REPEAT; i++) {
			c.reset();
			Integer num = new Integer(1019);
			Attendant br = new Attendant(num, "Brian",2002);
			HashMap<Integer, Attendant> attendants = new HashMap<Integer,Attendant>(){{put(num, br);}};
			AttendantSystem sys = new AttendantSystem(attendants);	
			boolean bool = sys.login(num,2002);
			int stationNum = sys.register(c);
			sys.startUp(5);

		}
	}
	
	@Test(expected = CheckoutException.class)
	public void attendantStartupInvalidStation2() throws CheckoutException {
		for (int i = 0; i < REPEAT; i++) {
			c.reset();
			Integer num = new Integer(1019);
			Attendant br = new Attendant(num, "Brian",2002);
			HashMap<Integer, Attendant> attendants = new HashMap<Integer,Attendant>(){{put(num, br);}};
			AttendantSystem sys = new AttendantSystem(attendants);	
			boolean bool = sys.login(num,2002);
			int stationNum = sys.register(null);
			sys.startUp(5);

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
			multiTestAssertEquals("Off",c.getpState());
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
			multiTestAssertEquals("On", c.getpState());
		}
	}
	
	@Test(expected = CheckoutException.class)
	public void attendantShutdownInvalidStation() throws CheckoutException {
		for (int i = 0; i < REPEAT; i++) {
			c.reset();
			Integer num = new Integer(1019);
			Attendant br = new Attendant(num, "Brian",2002);
			HashMap<Integer, Attendant> attendants = new HashMap<Integer,Attendant>(){{put(num, br);}};
			AttendantSystem sys = new AttendantSystem(attendants);	
			boolean bool = sys.login(num,2002);
			int stationNum = sys.register(c);
			sys.shutDown(4);

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
			sys.blockCheckout(stationNum);
			sys.approveWeight(stationNum);
			multiTestAssertEquals(c.getState(), "Scanning");
			sys.shutDown(stationNum);
		}
	}
	
	@Test(expected = CheckoutException.class)
	public void attendantApproveWeightOnInvalidStation() throws CheckoutException {
		for (int i = 0; i < REPEAT; i++) {
			c.reset();
			Integer num = new Integer(1019);
			Attendant br = new Attendant(num, "Brian",2002);
			HashMap<Integer, Attendant> attendants = new HashMap<Integer,Attendant>(){{put(num, br);}};
			AttendantSystem sys = new AttendantSystem(attendants);	
			sys.login(num,2002);
			int stationNum = sys.register(c);
			sys.startUp(stationNum);
			sys.blockCheckout(stationNum);
			sys.approveWeight(stationNum+1);
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
	
	@Test(expected = CheckoutException.class)
	public void attendantRemoveItemOnInvalidStation() throws CheckoutException {
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
			sys.removeItem(2, itemToRemove);
			BigDecimal newBal = bal.subtract(BigDecimal.valueOf(5));
			
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
			assertEquals(50,c.getInkTotal());
		
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
	
	@Test
	public void attendantBlocksStation() throws CheckoutException {
		for (int i = 0; i < REPEAT; i++) {
			c.reset();
			Integer num = new Integer(1019);
			Attendant br = new Attendant(num, "Brian",2002);
			HashMap<Integer, Attendant> attendants = new HashMap<Integer,Attendant>(){{put(num, br);}};
			AttendantSystem sys = new AttendantSystem(attendants);	
			int stationNum = sys.register(c);
			sys.login(num,2002);
			sys.blockCheckout(stationNum);
			multiTestAssertEquals("Paused",c.getState());
			
		}
	}
	
	@Test
	public void attendantBlocksStationNotLoggedIn() throws CheckoutException {
		for (int i = 0; i < REPEAT; i++) {
			c.reset();
			Integer num = new Integer(1019);
			Attendant br = new Attendant(num, "Brian",2002);
			HashMap<Integer, Attendant> attendants = new HashMap<Integer,Attendant>(){{put(num, br);}};
			AttendantSystem sys = new AttendantSystem(attendants);	
			int stationNum = sys.register(c);
			String preBlock = c.getState();
			sys.blockCheckout(stationNum);
			String postBlock = c.getState();
			multiTestAssertEquals(preBlock,postBlock);
			
		}
	}
	
	
	@Test(expected = CheckoutException.class)
	public void attendantBlocksInvalidStation() throws CheckoutException {
		for (int i = 0; i < REPEAT; i++) {
			c.reset();
			Integer num = new Integer(1019);
			Attendant br = new Attendant(num, "Brian",2002);
			HashMap<Integer, Attendant> attendants = new HashMap<Integer,Attendant>(){{put(num, br);}};
			AttendantSystem sys = new AttendantSystem(attendants);	
			sys.login(num,2002);

			sys.blockCheckout(2);
			
		}
	}
	

	
	@Test
	public void attendantSearchesProductNotLoggedIn() throws CheckoutException {
		for (int i = 0; i < REPEAT; i++) {
			c.reset();
			Integer num = new Integer(1019);
			Attendant br = new Attendant(num, "Brian",2002);
			HashMap<Integer, Attendant> attendants = new HashMap<Integer,Attendant>(){{put(num, br);}};
			AttendantSystem sys = new AttendantSystem(attendants);	
			PriceLookupCode lkup = new PriceLookupCode("5420");
			PLUCodedProduct p = new PLUCodedProduct(lkup, "Fake test item", BigDecimal.valueOf(5));
			ArrayList<Product> results = sys.searchProductDatabase("test item");
			assertEquals(null, results);	
	
		}
	}		
	
	@Test
	public void attendantSearchesPLUProduct() throws CheckoutException {
	
			c.reset();
			Integer num = new Integer(1019);
			Attendant br = new Attendant(num, "Brian",2002);
			HashMap<Integer, Attendant> attendants = new HashMap<Integer,Attendant>(){{put(num, br);}};
			AttendantSystem sys = new AttendantSystem(attendants);	
			sys.login(num,2002);
			PriceLookupCode plk = new PriceLookupCode("5542");
			PLUCodedProduct fakePProduct = new PLUCodedProduct(plk, "Fake PLU Product with price of $5", new BigDecimal(5.00));
			ProductDatabases.PLU_PRODUCT_DATABASE.put(plk,fakePProduct);
			ArrayList<Product> results = sys.searchProductDatabase("Fake plu");
			assertEquals(fakePProduct.hashCode(), results.get(0).hashCode());
		
	}

	@Test
	public void attendantSearchesBarcodedProduct() throws CheckoutException {
			c.reset();
			Integer num = new Integer(1019);
			Attendant br = new Attendant(num, "Brian",2002);
			HashMap<Integer, Attendant> attendants = new HashMap<Integer,Attendant>(){{put(num, br);}};
			AttendantSystem sys = new AttendantSystem(attendants);	
			sys.login(num,2002);
			Barcode bc = new Barcode("8830");
			BarcodedProduct fakeBProduct = new BarcodedProduct(bc, "Fake Barcoded Product with price of $5", new BigDecimal(5.00));
			ProductDatabases.BARCODED_PRODUCT_DATABASE.put(bc,fakeBProduct);
			ArrayList<Product> results = sys.searchProductDatabase("Fake Barcoded");
			assertEquals(fakeBProduct.hashCode(), results.get(0).hashCode());
		
	}
	
	@Test
	public void attendantRefillsCoinDispenser() throws CheckoutException {
		for (int i = 0; i < REPEAT; i++) {
			c.reset();
			Integer num = new Integer(1019);
			Attendant br = new Attendant(num, "Brian",2002);
			HashMap<Integer, Attendant> attendants = new HashMap<Integer,Attendant>(){{put(num, br);}};
			AttendantSystem sys = new AttendantSystem(attendants);	
			int stationNum = sys.register(c);
			sys.login(num,2002);
			Coin dolla = new Coin(new BigDecimal("1.00"), Currency.getInstance("CAD"));
			java.util.List<Coin> Coins = new ArrayList<Coin>();
			Coins.add(dolla);
			java.util.List<Coin> unadded = c.refillCoinDispenser(Coins);
			assertEquals("[]",unadded.toString()); //empty list since all coins have been loaded successfully
		}
	}
	
	@Test
	public void attendantRefillsCoinDispenserWithInvalidDenomination() throws CheckoutException {
		for (int i = 0; i < REPEAT; i++) {
			c.reset();
			Integer num = new Integer(1019);
			Attendant br = new Attendant(num, "Brian",2002);
			HashMap<Integer, Attendant> attendants = new HashMap<Integer,Attendant>(){{put(num, br);}};
			AttendantSystem sys = new AttendantSystem(attendants);	
			int stationNum = sys.register(c);
			sys.login(num,2002);
			Coin dolla = new Coin(new BigDecimal("3"), Currency.getInstance("CAD"));
			java.util.List<Coin> Coins = new ArrayList<Coin>();
			Coins.add(dolla);
			java.util.List<Coin> unadded = c.refillCoinDispenser(Coins);
			assertEquals("[3 CAD]",unadded.toString());
		}
	}
	@Test
	public void attendantRefillsCoinDispenserOverCapacity() throws CheckoutException {
		for (int i = 0; i < REPEAT; i++) {
			c.reset();
			Integer num = new Integer(1019);
			Attendant br = new Attendant(num, "Brian",2002);
			HashMap<Integer, Attendant> attendants = new HashMap<Integer,Attendant>(){{put(num, br);}};
			AttendantSystem sys = new AttendantSystem(attendants);	
			int stationNum = sys.register(c);
			sys.login(num,2002);
			Coin dolla = new Coin(new BigDecimal("1.00"), Currency.getInstance("CAD"));
			java.util.List<Coin> Coins = new ArrayList<Coin>();
			Coins.add(dolla);
			while (i<500) {c.refillCoinDispenser(Coins); i++; }	
			java.util.List<Coin> unadded = c.refillCoinDispenser(Coins);
			assertEquals("[1.00 CAD]",unadded.toString());
		}
	}
	
	
	@Test
	public void attendantRefillsBanknoteDispenser() throws CheckoutException {
		for (int i = 0; i < REPEAT; i++) {
			c.reset();
			Integer num = new Integer(1019);
			Attendant br = new Attendant(num, "Brian",2002);
			HashMap<Integer, Attendant> attendants = new HashMap<Integer,Attendant>(){{put(num, br);}};
			AttendantSystem sys = new AttendantSystem(attendants);	
			int stationNum = sys.register(c);
			sys.login(num,2002);
			Banknote bill = new Banknote(5, Currency.getInstance("CAD"));
			java.util.List<Banknote> Banknotes = new ArrayList<Banknote>();
			Banknotes.add(bill);
			java.util.List<Banknote> unadded = c.refillBanknoteDispenser(Banknotes);
			assertEquals("[]",unadded.toString()); //empty list since all coins have been loaded successfully
		}
	}
	
	@Test
	public void attendantRefillsBanknoteDispenserWithInvalidDenomination() throws CheckoutException {
		for (int i = 0; i < REPEAT; i++) {
			c.reset();
			Integer num = new Integer(1019);
			Attendant br = new Attendant(num, "Brian",2002);
			HashMap<Integer, Attendant> attendants = new HashMap<Integer,Attendant>(){{put(num, br);}};
			AttendantSystem sys = new AttendantSystem(attendants);	
			int stationNum = sys.register(c);
			sys.login(num,2002);
			Banknote bill = new Banknote(500, Currency.getInstance("CAD"));
			java.util.List<Banknote> Banknotes = new ArrayList<Banknote>();
			Banknotes.add(bill);
			java.util.List<Banknote> unadded = c.refillBanknoteDispenser(Banknotes);
			assertEquals("[500 CAD]",unadded.toString()); 
		}
	}
	@Test
	public void attendantRefillsBanknoteDispenserOverCapacity() throws CheckoutException {
		for (int i = 0; i < REPEAT; i++) {
			c.reset();
			Integer num = new Integer(1019);
			Attendant br = new Attendant(num, "Brian",2002);
			HashMap<Integer, Attendant> attendants = new HashMap<Integer,Attendant>(){{put(num, br);}};
			AttendantSystem sys = new AttendantSystem(attendants);	
			int stationNum = sys.register(c);
			sys.login(num,2002);
			Banknote bill = new Banknote(5, Currency.getInstance("CAD"));
			java.util.List<Banknote> Banknotes = new ArrayList<Banknote>();
			Banknotes.add(bill);
			while (i<150) {c.refillBanknoteDispenser(Banknotes); i++; }	
			java.util.List<Banknote> unadded = c.refillBanknoteDispenser(Banknotes);
			assertEquals("[5 CAD]",unadded.toString());
		}
	}
}
