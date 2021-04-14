/**
 * 
 */
package org.lsmr.selfcheckout.control;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Map;

import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Card;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.control.CardPayment.CardError;
import org.lsmr.selfcheckout.control.CardPayment.PaymentType;
import org.lsmr.selfcheckout.control.gui.GUIController;
import org.lsmr.selfcheckout.control.gui.StateHandler.StateUpdateListener;
import org.lsmr.selfcheckout.control.gui.statedata.BalanceStateData;
import org.lsmr.selfcheckout.control.gui.statedata.BooleanStateData;
import org.lsmr.selfcheckout.control.gui.statedata.BuyBagStateData;
import org.lsmr.selfcheckout.control.gui.statedata.InsertBarcodedProductData;
import org.lsmr.selfcheckout.control.gui.statedata.InsertPLUProductData;
import org.lsmr.selfcheckout.control.gui.statedata.KeypadStateData;
import org.lsmr.selfcheckout.control.gui.statedata.ListProductStateData;
import org.lsmr.selfcheckout.control.gui.statedata.LookupStateData;
import org.lsmr.selfcheckout.control.gui.statedata.MemberStateData;
import org.lsmr.selfcheckout.control.gui.statedata.ProductStateData;
import org.lsmr.selfcheckout.control.gui.statedata.RequestPricePerBagData;
import org.lsmr.selfcheckout.control.gui.statedata.ScaleStateData;
import org.lsmr.selfcheckout.control.gui.statedata.ScannedItemsRequestData;
import org.lsmr.selfcheckout.control.gui.statedata.StateData;
import org.lsmr.selfcheckout.control.gui.states.BuyingState;
import org.lsmr.selfcheckout.control.gui.states.AttendantLogInState;
import org.lsmr.selfcheckout.devices.BanknoteDispenser;
import org.lsmr.selfcheckout.devices.CoinDispenser;
import org.lsmr.selfcheckout.devices.DisabledException;
import org.lsmr.selfcheckout.devices.EmptyException;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.ReceiptPrinter;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.SimulationException;
import org.lsmr.selfcheckout.external.ProductDatabases;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;
import org.lsmr.selfcheckout.products.Product;

/**
 * Creates a Checkout instance for a checkoutStation. Uses the devices in the
 * given checkoutStation when operating.
 * <p>
 * The expected sequence of events is:
 * <p>
 * 1. Scan a barcoded item
 * <p>
 * 2. Place the item in the bagging area
 * <p>
 * 3. Repeat steps 1-2.
 * <p>
 * 4. Choose to start paying
 * <p>
 * 5. Pay with the chosen paying method (user can also choose to cancel payment
 * and go back to scanning)
 * <p>
 * 6. Choose to print the receipt, or not to
 * <p>
 * 7. Remove the purchased items from the bagging area
 * 
 * @author Group U08-2
 * @date Mar 31, 2021
 */
public class Checkout {
	private enum CheckoutState {
		Scanning, Paused, Paying_Cash, Paying_Credit, Paying_Debit, Paying_Gift, GivingChange, PrintingReceipt, Done, Off
	};

	public enum PayingState {
		Cash, Credit, Debit, Gift
	};

	private final BanknoteDispenserSlotListener bankNoteOutputListener;
	private final CardPayment cardPayment;
	private final SelfCheckoutStation checkoutStation;
	private final GiveChange giveChange;

	private final double WEIGHT_TOLERANCE = 10;
	private static BigDecimal pricePerPlasticBag = new BigDecimal("0.05");

	private BigDecimal currentBalance;
	private boolean customerBag;
	private boolean paidCash;
	protected double expectedWeightOnBaggingArea;
	private int inkTotal;
	private int paperTotal;
	private ArrayList<ReceiptItem> productsAdded; //Wanna keep track of what was scanned
	private ArrayList<Item> itemsAdded = new ArrayList<Item>(); //keep track of items added so they can be removed
	private String loggedInMemberName;
	private String loggedInMemberNumber;
	private CheckoutState state;
	private double weightOnBaggingArea;
	private double weightOnScanScale;
	private AttendantSystem attendantSystem;
	protected GUIController guiController;


	public Checkout(SelfCheckoutStation checkoutStation) {
		if (checkoutStation == null) {
			throw new SimulationException(new NullPointerException("Argument may not be null."));
		}

		this.checkoutStation = checkoutStation;

		BarcodeScannerUpdateListener scannerListener = new BarcodeScannerUpdateListener(this);
		checkoutStation.mainScanner.register(scannerListener);
		checkoutStation.handheldScanner.register(scannerListener);

		BaggingAreaWeightUpdateListener weightListener = new BaggingAreaWeightUpdateListener(this);
		checkoutStation.baggingArea.register(weightListener);
		
		ScaleWeightUpdateListener scaleListener = new ScaleWeightUpdateListener(this);
		checkoutStation.scale.register(scaleListener);

		checkoutStation.coinSlot.disable();
		CoinValidatorBalanceUpdateListener coinListener = new CoinValidatorBalanceUpdateListener(this);
		checkoutStation.coinValidator.register(coinListener);

		checkoutStation.banknoteInput.disable();
		BanknoteValidatorBalanceUpdateListener banknoteListener = new BanknoteValidatorBalanceUpdateListener(this);
		checkoutStation.banknoteValidator.register(banknoteListener);

		cardPayment = new CardPayment(checkoutStation);

		bankNoteOutputListener = new BanknoteDispenserSlotListener(this, checkoutStation.banknoteDispensers);
		checkoutStation.banknoteOutput.register(bankNoteOutputListener);

		giveChange = new GiveChange(checkoutStation);
		MembershipCardListener membershipListener = new MembershipCardListener(this);
		checkoutStation.cardReader.register(membershipListener);


		currentBalance = new BigDecimal(0);
		customerBag = false;
		paidCash = false;
		expectedWeightOnBaggingArea = 0;
		productsAdded = new ArrayList<ReceiptItem>(); //Wanna keep track of what was scanned
		itemsAdded = new ArrayList<Item>(); //keep track of items added so they can be removed
		loggedInMemberName = null;
		loggedInMemberNumber = null;
		state = CheckoutState.Scanning;
		weightOnBaggingArea = 0;
		weightOnScanScale = 0;

	}

	public void run() {
		checkoutStation.screen.setVisible(true);
	}

	/**
	 * Resets the checkout to the initial state. Intended to be used only for
	 * testing. Simulates a person unloading everything, then loading anything
	 * needed.
	 */
	public void reset() {
		removePurchasedItemFromBaggingArea();
		checkoutStation.banknoteInput.removeDanglingBanknote();
		checkoutStation.banknoteStorage.unload();
		checkoutStation.cardReader.remove();
		checkoutStation.coinTray.collectCoins();
		checkoutStation.coinStorage.unload();
		Currency cad = checkoutStation.coinValidator.currency;

		for (Integer value : checkoutStation.banknoteDispensers.keySet()) {
			Banknote[] banknotes = new Banknote[10];
			for (int i = 0; i < 10; i++) {

				banknotes[i] = new Banknote(value, cad);
			}
			try {
				checkoutStation.banknoteDispensers.get(value).unload();
				checkoutStation.banknoteDispensers.get(value).load(banknotes);
			} catch (SimulationException | OverloadException e) {
				//should not happen
				e.printStackTrace();
			}
		}
		for (BigDecimal value : checkoutStation.coinDispensers.keySet()) {
			Coin[] coins = new Coin[10];
			for (int i = 0; i < 10; i++) {
				coins[i] = new Coin(value, cad);
			}
			try {
				checkoutStation.coinDispensers.get(value).unload();
				checkoutStation.coinDispensers.get(value).load(coins);
			} catch (SimulationException | OverloadException e) {
				//should not happen
				e.printStackTrace();
			}
		}

		currentBalance = new BigDecimal(0);
		customerBag = false;
		paidCash = false;
		expectedWeightOnBaggingArea = 0;
		productsAdded = new ArrayList<ReceiptItem>(); //Wanna keep track of what was scanned
		itemsAdded = new ArrayList<Item>(); //keep track of items added so they can be removed
		loggedInMemberName = null;
		loggedInMemberNumber = null;
		state = CheckoutState.Scanning;
		weightOnBaggingArea = 0;
		weightOnScanScale = 0;
		bankNoteOutputListener.reset();

	}

	/**
	 * Add the change value to the current balance.
	 * 
	 * @param curr
	 *            The value of the change to add to the balance
	 */
	protected void addBalanceCurr(BigDecimal curr) {
		currentBalance = currentBalance.add(curr);
	}

	/**
	 * Add the change value to the current balance.
	 * 
	 * @param curr
	 *            The value of the change to add to the balance
	 */
	protected void addBalanceCurr(int curr) {
		currentBalance = currentBalance.add(new BigDecimal(curr));
	}

	/**
	 * Add the price to the current balance.
	 * 
	 * @param pricePerUnit
	 *            The price to add to the balance
	 */
	protected void addBalanceUnit(BigDecimal pricePerUnit) {
		currentBalance = currentBalance.add(pricePerUnit);
	}

	protected void addBarcodedProductToList(BarcodedProduct p, double weight) {
		productsAdded.add(new ReceiptItem(p, p.getPrice(), weight, p.getPrice()));
	}

	protected void addBagsToList(int number, BigDecimal totalPrice) {
		BarcodedProduct plasticBags = new BarcodedProduct(new Barcode("000" + number), "Plastic Bag x" + number,
				totalPrice);
		productsAdded.add(new ReceiptItem(plasticBags, totalPrice, 0, totalPrice));

	}

	/**
	 * Adds a PLU product to the receipt list
	 * 
	 * @param p
	 * @param totalPrice
	 * @param weightInGrams
	 * @param pricePerKilo
	 */
	protected void addPLUProductToList(PLUCodedProduct p, BigDecimal totalPrice, double weightInGrams,
			BigDecimal pricePerKilo) {
		productsAdded.add(new ReceiptItem(p, totalPrice, weightInGrams, pricePerKilo));
	}

	/**
	 * Add a customer's own bag to the bagging area.
	 * 
	 * @param The
	 *            weight of the bag
	 * @throws OverloadException
	 */
	public void addCustomerBag(double weight) throws CheckoutException, OverloadException {
		if (usingCustomerBag()) {
			throw new CheckoutException("Customer bag(s) already added");
		}
		if (isScanning()) {
			Barcode b = new Barcode("000"); //N/A
			BarcodedItem cbag = new BarcodedItem(b, weight);
			expectedWeightOnBaggingArea += weight;
			addItemToBaggingArea(cbag);
			customerBag = true;
		} else if (isPaused()) {
			throw new CheckoutException("Please add previously scanned items before adding a bag");
		} else {
			throw new CheckoutException("Cannot add bag outside scanning process");
		}
	}

	/**
	 * Use Case: Customer returns to adding items Use Case: Station detects that the
	 * weight in the bagging area does not conform to expectations Adds an item to
	 * the bagging area. Resumes scanning state when customer puts item in bagging
	 * area that they previously did not.
	 * 
	 * @param item
	 *            The item to add to the bagging area
	 * @throws OverloadException
	 *             If the item causes the scale to be over limit
	 * @throws CheckoutException
	 *             If the weight in the bagging area is not within tolerance of the
	 *             expected weight
	 */
	public void addItemToBaggingArea(Item item) throws OverloadException, CheckoutException {
		checkoutStation.baggingArea.add(item);
		itemsAdded.add(item);
		if (Double.isNaN(getWeightOnBaggingArea())) {
			throw new OverloadException("The scale is over weight");
		}
		if (isScanning() || isPaused()) {
			if (Math.abs(getWeightOnBaggingArea() - expectedWeightOnBaggingArea) <= WEIGHT_TOLERANCE) {
				state = CheckoutState.Scanning;
			} else {
				throw new CheckoutException("Weight on bagging area is incorrect (greater than " + WEIGHT_TOLERANCE
						+ " deviation from expected weight).\n\tExpected: " + expectedWeightOnBaggingArea + "\tActual: "
						+ getWeightOnBaggingArea());
			}
		}
	}

	/**
	 * Use Case: Customer removes purchased items from bagging area Removes all the
	 * previously added items from the bagging area. If the checkout is currently
	 * done printing the receipt, the checkout will be ready for scanning after this
	 * is called
	 * 
	 * @return The list of items removed from bagging area
	 */
	public List<Item> removePurchasedItemFromBaggingArea() {
		//		checkoutStation.baggingArea
		ArrayList<Item> itemsRemoved = new ArrayList<Item>();
		for (Item item : itemsAdded) {
			try {
				removeItemFromBaggingArea(item);
				itemsRemoved.add(item);
			} catch (SimulationException e) {

			}
		}
		itemsAdded.clear();
		if (state == CheckoutState.Done) {
			state = CheckoutState.Scanning;
		}
		return itemsRemoved;
	}
	/**
	 * Attempts to remove the item from the bagging area
	 * @param item
	 */
	protected void removeItemFromBaggingArea(Item item) {
		checkoutStation.baggingArea.remove(item);
	}
	/**
	 * Use Case: Customer does not want to bag a scanned item The customer chooses
	 * not to add the last scanned item to the bagging area. The expected weight is
	 * reduced by the weight of the product
	 * 
	 * @throws CheckoutException
	 *             if the last added item was not a scanned item
	 */
	protected void doNotBagLastItem() throws CheckoutException {
		Product lastAdded = productsAdded.get(productsAdded.size() - 1).product;
		if (lastAdded instanceof BarcodedProduct) {
			Barcode bar = ((BarcodedProduct) lastAdded).getBarcode();
			expectedWeightOnBaggingArea -= ProductWeightDatabase.PRODUCT_WEIGHT_DATABASE.get(bar);
		} else {
			throw new CheckoutException("last added item was not scanned");
		}

		if (isPaused()) {
			if (Math.abs(getWeightOnBaggingArea() - expectedWeightOnBaggingArea) <= WEIGHT_TOLERANCE) {
				state = CheckoutState.Scanning;
			}
		}
	}

	/**
	 * Use Case: Customer looks up product Returns an array list of products with
	 * descriptions that contain the search string
	 * <p>
	 * Use case: customer looks up product
	 * 
	 * @param name
	 * @return A list of products that match the search
	 */
	public ArrayList<Product> searchProductDatabase(String name) {
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

	/**
	 * Places an item onto the scale
	 * 
	 * @param item
	 */
	public void addItemToScale(Item item) {
		checkoutStation.scale.add(item);
	}

	/**
	 * Removes the item from the scale
	 * 
	 * @param item
	 * @throws SimulationException
	 *             If the item is not on the scale
	 */
	public void removeItemFromScale(Item item) throws SimulationException {
		checkoutStation.scale.remove(item);
	}

	/**
	 * Use Case: Customer enters PLU code for a product Expects the item to already
	 * be added onto the scale using addItemToScale()
	 * <p>
	 * Adds the product specified by the PLUcode to the checkout. The balance is
	 * incremented by the weight on the scale (in grams) multiplied by the price per
	 * kilogram of the item, converted appropriately to units match.
	 * <p>
	 * Once done, remember to removeItemFromScale()
	 * <p>
	 * Throws a checkout exception if the Checkout is currently paused
	 * 
	 * @param code
	 *            The PLUcode to enter
	 * @throws CheckoutException
	 *             if the Checkout is currently paused
	 */
	public void enterPLUCode(PriceLookupCode code) throws CheckoutException {
		if (isScanning()) {
			PLUCodedProduct product = ProductDatabases.PLU_PRODUCT_DATABASE.get(code);
			// need to convert price per kilo to price per gram, which is done by divide 1000. However, want to round to whole cents, so round after dividing by 10, then divide by 100.
			// then do price per gram * totalweightingram
			BigDecimal totalPrice = product.getPrice().multiply(new BigDecimal(weightOnScanScale))
					.divideToIntegralValue(new BigDecimal(10)).divide(new BigDecimal(100));
			addBalanceCurr(totalPrice);
			addPLUProductToList(product, totalPrice, weightOnScanScale, totalPrice);
			addExpectedWeightOnScale(weightOnScanScale);
		} else if (isPaused()) {
			throw new CheckoutException("Previously scanned item has not been added to the bagging area");
		}
	}

	/**
	 * Use Case: Customer enters their membership card information The customer logs
	 * in by entering the card number. The member name will default to the primary
	 * account holder. If the card number is not in the membership number list,
	 * nothing happens.
	 * 
	 * @param number
	 * @return true if the info associated with the membership card exists, false
	 *         otherwise
	 * @throws CheckoutException
	 *             If a member is already logged in
	 */
	public boolean enterMembershipCardInfo(String number) throws CheckoutException {

		String check = MembershipCardDatabase.MEMBERSHIP_CARD_DATABASE.get(number);
		if (check != null) {
			if (this.loggedInMemberName == null) {
				this.loggedInMemberName = check;
				this.loggedInMemberNumber = number;
				return true;
			} else {
				throw new CheckoutException("Attempted to log in when a member was already logged in");
			}
		}

		return false;

	}

	/**
	 * The checkout calculates the change required and has the coin dispenser and
	 * banknote dispensers dispense the change. All the coins will be immediately
	 * dispensed, but only the first banknote will be dispensed. Subsequent
	 * banknotes will be dispensed upon removal of previously dispensed banknotes.
	 * <p>
	 * The change must then be collected using getChangeFromCoinTray() and
	 * getChangeFromBanknoteSlots().
	 * <p>
	 * The checkout will give change such that the value of the change is the
	 * smallest possible while also giving back enough change. (E.G., need to give
	 * back $0.02 but the smallest denomination is $0.05, the checkout will give
	 * $0.05 in change). Additionally, the checkout will then try to give out the
	 * fewest number of bills+coins so customers are not burdened with excess
	 * coinage/bills.
	 * <p>
	 * 
	 * @throws EmptyException
	 * @throws DisabledException
	 * @throws OverloadException
	 * @throws CheckoutException
	 */
	public void emitChange() throws EmptyException, DisabledException, OverloadException, CheckoutException {
		if (state == CheckoutState.GivingChange) {
			giveChange.calculateChange(currentBalance);
			Map<BigDecimal, Integer> coins = giveChange.getCoinsToGive();
			Map<Integer, Integer> notes = giveChange.getNotesToGive();

			for (Map.Entry<BigDecimal, Integer> entry : coins.entrySet()) {
				for (int i = 0; i < entry.getValue(); i++) {
					checkoutStation.coinDispensers.get(entry.getKey()).emit();
					addBalanceCurr(entry.getKey());
				}
			}
			for (Map.Entry<Integer, Integer> entry : notes.entrySet()) {
				for (int i = 0; i < entry.getValue(); i++) {
					bankNoteOutputListener.addBanknoteToEmit(entry.getKey());
				}
			}
			return;
		}
		throw new CheckoutException("Attempted to get change without paying enough money");
	}

	/**
	 * @return The outstanding balance
	 */
	public BigDecimal getBalance() {
		return currentBalance;
	}

	/**
	 * Returns a list of items added, as they are stored on the receipt.
	 * 
	 * @return
	 */
	public ArrayList<ReceiptItem> getProductsAdded() {
		return new ArrayList<ReceiptItem>(productsAdded);
	}

	/**
	 * Removes an item from the list of products added. Updates the balance
	 * accordingly
	 * 
	 * @param i
	 * @return true if the item was removed, false otherwise
	 */
	public boolean deleteProductAdded(ReceiptItem i) {

		if (productsAdded.remove(i)) {
			currentBalance = currentBalance.subtract(i.totalPrice);

			expectedWeightOnBaggingArea -= i.weightInGrams;
			return true;
		}

		return false;
	}

	public List<Banknote> getChangeFromBanknoteSlots() {
		List<Banknote> banknotes = new ArrayList<Banknote>();
		Banknote banknote;
		while ((banknote = checkoutStation.banknoteOutput.removeDanglingBanknote()) != null) {
			banknotes.add(banknote);
		}
		return banknotes;
	}

	/**
	 * Simulates the customer removing coins from the Coin Tray
	 * 
	 * @return the list of coins collected from the coin tray
	 */
	public List<Coin> getChangeFromCoinTray() {
		List<Coin> coins = new ArrayList<Coin>();
		for (Coin c : checkoutStation.coinTray.collectCoins()) {
			if (c != null) {
				coins.add(c);
			}
		}
		return coins;
	}

	/**
	 * Returns null if a member has not logged in
	 * 
	 * @return the loggedInMemberName
	 */
	public String getLoggedInMemberName() {
		return loggedInMemberName;
	}

	/**
	 * Returns null if a member has not logged in
	 * 
	 * @return the loggedInMemberNumber
	 */
	public String getLoggedInMemberNumber() {
		return loggedInMemberNumber;
	}

	/**
	 * @return The total weight of the items current in the bagging area. If the
	 *         scale is currently over limit, returns Double.NaN
	 */
	public double getWeightOnBaggingArea() {
		return weightOnBaggingArea;
	}

	/**
	 * @return The total weight of the items currently on the Scale. If the scale is
	 *         currently over limit, returns Double.NaN
	 */
	public double getWeightOnScale() {
		return weightOnScanScale;
	}

	/**
	 * Initializes the machine to prepare to pay with a credit card
	 * 
	 * @throws CheckoutException
	 *             if there is an issue initializing the payment machine
	 */
	private void initializeCreditPayment() throws CheckoutException {
		cardPayment.initialize(PaymentType.CREDIT, currentBalance);
	}

	/**
	 * Initializes the machine to prepare to pay with a debit card
	 * 
	 * @throws CheckoutException
	 *             if there is an issue initializing the payment machine
	 */
	private void initializeDebitPayment() throws CheckoutException {
		cardPayment.initialize(PaymentType.DEBIT, currentBalance);
	}

	/**
	 * Use Case: Customer pays with gift card Initializes the machine to prepare to
	 * pay with a gift card
	 * 
	 * @throws CheckoutException
	 *             if there is an issue initializing the payment machine
	 */
	private void initializeGiftPayment() throws CheckoutException {
		cardPayment.initialize(PaymentType.GIFT, currentBalance);
	}

	/**
	 * Checks whether this checkout instance is in the paused state. Paused state is
	 * the checkout station waiting for an item to be added to the bagging area
	 * 
	 * @return true if this checkout instance is in the paused state, false
	 *         otherwise
	 */
	public boolean isPaused() {
		return state == CheckoutState.Paused;
	}

	/**
	 * Checks whether this checkout instance is in the payment process
	 * 
	 * @return true if this checkout instance is in the payment process, false
	 *         otherwise
	 */
	public boolean isPaying() {
		return state == CheckoutState.Paying_Cash || state == CheckoutState.Paying_Credit
				|| state == CheckoutState.Paying_Debit;

	}

	/**
	 * Checks whether this checkout instance is in the scanning process
	 * 
	 * @return true if this checkout instance is in the scanning process, false
	 *         otherwise
	 */
	public boolean isScanning() {
		return state == CheckoutState.Scanning;
	}

	/**
	 * Logs the member in
	 * 
	 * @param name
	 *            The name of the member
	 * @param number
	 *            The member number of the member
	 * @throws CheckoutException
	 *             If a member is already logged in
	 */
	protected void memberLogIn(String name, String number) throws CheckoutException {

		if (this.loggedInMemberName == null) {
			String check = MembershipCardDatabase.MEMBERSHIP_CARD_DATABASE.get(number);
			if (check != null) {
				if (this.loggedInMemberName == null) {
					this.loggedInMemberName = name;
					this.loggedInMemberNumber = number;
				} else {
					throw new CheckoutException("Attempted to log in when a member was already logged in");
				}
			}
		} else {
			throw new CheckoutException("Attempted to log in when a member was already logged in");
		}
	}

	/**
	 * Make a transaction by inserting a card
	 * 
	 * @param card
	 *            the card to insert
	 * @param pin
	 *            the pin of the card
	 * @throws CheckoutException
	 *             if there is an error with the transaction
	 */
	public void payByInsertingCard(Card card, String pin) throws CheckoutException {
		if (!cardPayment.isReady())
			throw new CheckoutException("Card machine not ready. Is it initialized?");

		CardError e = cardPayment.insert(card, pin);
		// true if there are errors
		if (e != null) {
			switch (e) {
			case CHIP_FAILURE:
				throw new CheckoutException("Error while reading the card's chip");
			case INVALID_CARD_TYPE:
				throw new CheckoutException(
						String.format("User chose %s payment but the card used was not the same type",
								cardPayment.getPaymentType().toString()));
			case WRONG_PIN:
				throw new CheckoutException("Wrong pin inputted");
			case AUTHORIZATION_FAIL:
				throw new CheckoutException("Unable to authorize card.");

			default: // ex. there should never be a chip error while swiping
				throw new CheckoutException("Software error! Unknown issue with card payment");
			}
		} else {
			currentBalance = new BigDecimal("0.00");
			state = CheckoutState.PrintingReceipt;
		}

	}

	/**
	 * Removes the previously inserted card
	 */
	public void removeCard() {
		checkoutStation.cardReader.remove();
	}

	/**
	 * Make a transaction by swiping a card
	 * 
	 * @param card
	 *            the card to swipe
	 * @param signature
	 *            the customer's signature
	 * @throws CheckoutException
	 *             if there is an error with the transaction
	 */
	public void payBySwipingCard(Card card, BufferedImage signature) throws CheckoutException {
		if (!cardPayment.isReady())
			throw new CheckoutException("Card machine not ready. Is it initialized?");

		CardError e = cardPayment.swipe(card, signature);

		// true if there are errors
		if (e != null) {
			switch (e) {
			case SWIPE_ERROR:
				throw new CheckoutException("Error while reading the card's magnetic strip");
			case INVALID_CARD_TYPE:
				throw new CheckoutException(
						String.format("User chose %s payment but the card used was not the same type",
								cardPayment.getPaymentType().toString()));
			case AUTHORIZATION_FAIL:
				throw new CheckoutException("Unable to authorize card.");

			default: // ex. there should never be a chip error while swiping
				throw new CheckoutException("Software error! Unknown issue with card payment");
			}
		} else {
			currentBalance = new BigDecimal("0.00");
			state = CheckoutState.PrintingReceipt;
		}
	}

	/**
	 * Use Case: Customer enters number of plastic bags used
	 * 
	 * @param n
	 */
	public void usePlasticBags(int n) {
		BigDecimal totalPrice = pricePerPlasticBag.multiply(new BigDecimal(n));
		addBalanceCurr(totalPrice);
		addBagsToList(n, totalPrice);
	}

	/**
	 * Make a transaction by tapping the card on the card reader
	 * 
	 * @param card
	 *            the card to tap
	 * @throws CheckoutException
	 *             if there is an error with the transaction
	 */
	public void payByTappingCard(Card card) throws CheckoutException {
		if (!cardPayment.isReady())
			throw new CheckoutException("Card machine not ready. Is it initialized?");

		CardError e = cardPayment.tap(card);

		// true if there are errors
		if (e != null) {

			switch (e) {
			case CHIP_FAILURE:
				throw new CheckoutException("Error while reading the card's chip");
			case INVALID_CARD_TYPE:
				throw new CheckoutException(
						String.format("User chose %s payment but the card used was not the same type",
								cardPayment.getPaymentType().toString()));
			case AUTHORIZATION_FAIL:
				throw new CheckoutException("Unable to authorize card.");
			case TAP_FAIL:
				throw new CheckoutException("Card is not tap enabled.");
			default: // ex. there should never be a chip error while swiping
				throw new CheckoutException("Software error! Unknown issue with card payment");
			}
		} else {
			currentBalance = new BigDecimal("0.00");
			state = CheckoutState.PrintingReceipt;
		}
	}

	/**
	 * Pay with a banknote. If the banknote is valid, reduces the outstanding
	 * balance by the value of the banknote. If the banknote is not a valid
	 * banknote, the balance is not changed.
	 * <p>
	 * Only works while in the payment process. If the balance becomes 0 or
	 * negative, the checkout instance goes into the giving change process
	 * 
	 * @param coin
	 *            The banknote to pay with
	 * @throws OverloadException
	 *             If the banknote input is currently full (possibly due to a
	 *             rejected banknote not being retrieved)
	 * @throws CheckoutException
	 *             If the user has not yet chosen to pay
	 */
	public void payWithBanknote(Banknote banknote) throws OverloadException, CheckoutException {
		if (isPaying()) {
			try {
				checkoutStation.banknoteInput.accept(banknote);
				paidCash = true;
			} catch (DisabledException e) {
				throw new CheckoutException("Cannot pay with banknote because the banknote input is out of order");
			}
		} else if (isScanning()) {
			throw new CheckoutException("Cannot pay with coin because the user has not chosen to pay yet");
		} else {
			throw new CheckoutException("Cannot pay with coin because the user has finished paying");
		}
	}

	/**
	 * Pay with a coin. If the coin is valid, reduces the outstanding balance by the
	 * value of the coin. If the coin is not a valid coin, the balance is not
	 * changed.
	 * <p>
	 * Only works while in the payment process. If the balance becomes 0 or
	 * negative, the checkout instance goes into the giving change process
	 * 
	 * @param coin
	 *            The coin to pay with
	 * @throws CheckoutException
	 *             If the user has not yet chosen to pay
	 */
	public void payWithCoin(Coin coin) throws CheckoutException {
		if (isPaying()) {
			try {
				checkoutStation.coinSlot.accept(coin);
				paidCash = true;
			} catch (DisabledException e) {
				//should not happen
				throw new CheckoutException("Cannot pay with coin because coin slot is out of order");
			}
		} else if (isScanning()) {
			throw new CheckoutException("Cannot pay with coin because the user has not chosen to pay yet");
		} else {
			throw new CheckoutException("Cannot pay with coin because the user has finished paying");
		}
	}

	/**
	 * Prints receipt and gets it ready for removal. Call removeReceipt to get the
	 * actual receipt.
	 * 
	 * @throws CheckoutException
	 */
	public void printReceipt() throws CheckoutException {
		if (state == CheckoutState.PrintingReceipt) {
			for (int i = 0; i < productsAdded.size(); i++) {
				if (i != 0) {
					checkoutStation.printer.print('\n');
				}
				String itemOnReceipt = productsAdded.get(i).toString();
				for (char c : itemOnReceipt.toCharArray()) {
					printReceiptChar(c);
				}
			}
			checkoutStation.printer.cutPaper();
			state = CheckoutState.Done;
			currentBalance = new BigDecimal(0);
			customerBag = false;
			paidCash = false;
			expectedWeightOnBaggingArea = 0;
			productsAdded.clear();
			loggedInMemberName = null;
			loggedInMemberNumber = null;

		} else {
			throw new CheckoutException("Not in print receipt state");
		}

	}

	/**
	 * The user chooses to not print a receipt.
	 */
	public void doNotPrintReceipt() {
		state = CheckoutState.Done;
		currentBalance = new BigDecimal(0);
		customerBag = false;
		paidCash = false;
		expectedWeightOnBaggingArea = 0;
		productsAdded.clear();
		loggedInMemberName = null;
		loggedInMemberNumber = null;
	}

	/**
	 * Prints a character on the receipt, and also updates the amount of paper and
	 * ink remaining
	 * 
	 * @param c
	 */
	private void printReceiptChar(char c) {

		try {
			if (c == '\n') {
				--paperTotal;
			}
			checkoutStation.printer.print(c);
			if (!Character.isWhitespace(c)) {
				inkTotal--;
			}

		} catch (SimulationException e) {

		}

	}

	/**
	 * Returns a string representation of the receipt, if possible. If the receipt
	 * cannot be removed, returns null. If other customers forgot to remove their
	 * receipt, this receipt could include that receipt too.
	 * 
	 * @return The receipt if it has been cut; otherwise, null
	 */
	public String removeReceipt() {
		return checkoutStation.printer.removeReceipt();
	}

	/**
	 * Cancels the payment process and returns to scanning. Cannot cancel while not
	 * in payment state. Cannot cancel after already paying some coins/banknotes
	 * into the checkout
	 * 
	 * @throws CheckoutException
	 *             If some cash has already been paid, or if the current state is
	 *             not a payment state
	 */
	public void cancelPayment() throws CheckoutException {
		switch (state) {
		case Paying_Cash:
			if (paidCash) {
				throw new CheckoutException("Already paid some cash. Cannot cancel.");
			}
		case Paying_Credit:
		case Paying_Debit:
			state = CheckoutState.Scanning;
			break;
		default:
			throw new CheckoutException("Cannot cancel payment while not in payment state");
		}
	}

	/**
	 * If there is a dangling banknote, removes it from the banknote input,
	 * otherwise does nothing.
	 * 
	 * @return The removed banknote
	 */
	public Banknote removeRejectedBanknote() {
		return checkoutStation.banknoteInput.removeDanglingBanknote();
	}

	/**
	 * Scan an item. If in the scanning state and not paused, the unit price of the
	 * product is added to the balance. If paused, throws a CheckoutException,
	 * otherwise nothing happens
	 * 
	 * @param item
	 *            The item to scan
	 * @throws CheckoutException
	 *             if previously scanned items have not been added to the bagging
	 *             area
	 */
	public void scanItem(Item item) throws CheckoutException {
		if (isScanning()) {
			checkoutStation.mainScanner.scan(item);
		} else if (isPaused()) {
			throw new CheckoutException("Previously scanned item has not been added to the bagging area");
		}
	}

	/**
	 * @return the pricePerPlasticBag
	 */
	public static BigDecimal getPricePerPlasticBag() {
		return pricePerPlasticBag;
	}

	/**
	 * @param pricePerPlasticBag
	 *            the pricePerPlasticBag to set
	 */
	public static void setPricePerPlasticBag(BigDecimal pricePerPlasticBag) {
		Checkout.pricePerPlasticBag = pricePerPlasticBag;
	}

	/**
	 * Updates the weight of items in the bagging area.
	 * 
	 * @param weight
	 *            The new total weight of the items in the bagging area
	 */
	protected void setWeightOnBaggingArea(double weight) {
		weightOnBaggingArea = weight;
	}

	/**
	 * Updates the weight of items on the scale
	 * 
	 * @param weight
	 *            The new total weight of the items in the bagging area
	 */
	protected void setWeightOnScanScale(double weight) {
		weightOnScanScale = weight;
	}

	/**
	 * Updates the weight of items in the bagging area.
	 * 
	 * @param weight
	 *            The new total weight of the items in the bagging area
	 */
	protected void addExpectedWeightOnScale(double weight) {
		expectedWeightOnBaggingArea += weight;
		if (Math.abs(getWeightOnBaggingArea() - expectedWeightOnBaggingArea) <= WEIGHT_TOLERANCE) {
			state = CheckoutState.Scanning;
		} else {
			state = CheckoutState.Paused;
		}
	}

	/**
	 * Choose to start payment. While in the payment process, attempting to scan an
	 * item or add an item to the bagging area does nothing. Cannot start paying
	 * process if no items have been added. The resulting payment state is equal to
	 * the input payment state.
	 * 
	 * @throws CheckoutException
	 *             If the requested state is not a payment state, or if the current
	 *             state is not a scanning state
	 */
	public void startPayment(PayingState payState) throws CheckoutException {
		if (getBalance().doubleValue() <= 0.000001) {
			throw new CheckoutException("No items have been added yet");
		}
		if (!isScanning()) {
			if (isPaused()) {
				throw new CheckoutException("Please add previously scanned item to bagging area");
			} else {
				throw new CheckoutException("Requested to pay while not scanning");
			}
		}

		//This is legal, I thought it looked nicer
		switch (payState) {
		case Cash:
			state = CheckoutState.Paying_Cash;
			checkoutStation.coinSlot.enable();
			checkoutStation.banknoteInput.enable();
			break;
		case Credit:
			state = CheckoutState.Paying_Credit;
			initializeCreditPayment();
			break;
		case Debit:
			state = CheckoutState.Paying_Debit;
			initializeDebitPayment();
			break;
		case Gift:
			state = CheckoutState.Paying_Gift;
			initializeGiftPayment();
			break;
		}

	}

	/**
	 * Reduces the balance by the given value. If the checkout instance is in the
	 * payment process and the balance becomes 0 or negative, the checkout instance
	 * goes into the giving change process
	 * 
	 * @param value
	 */
	protected void subtractBalance(BigDecimal value) {
		currentBalance = currentBalance.subtract(value);
		if (currentBalance.compareTo(new BigDecimal(0)) <= 0) {
			state = CheckoutState.GivingChange;
		}
	}

	/**
	 * Reduces the balance by the given value. If the checkout instance is in the
	 * payment process and the balance becomes 0 or negative, the checkout instance
	 * goes into the giving change process
	 * 
	 * @param value
	 */
	protected void subtractBalance(int value) {
		currentBalance = currentBalance.subtract(new BigDecimal(value));
		if (currentBalance.compareTo(new BigDecimal(0)) <= 0) {
			state = CheckoutState.GivingChange;
		}
	}

	/**
	 * Uses a default null signature.
	 * 
	 * @param c
	 * @throws CheckoutException
	 *             If the insert failed
	 */
	public void swipeCard(Card c) throws CheckoutException {
		try {
			checkoutStation.cardReader.swipe(c, null);
		} catch (IOException e) {
			throw new CheckoutException("The swipe failed");
		}
	}

	/**
	 * @param c
	 * @throws CheckoutException
	 *             If the insert failed
	 */
	public void tapCard(Card c) throws CheckoutException {
		try {
			checkoutStation.cardReader.tap(c);
		} catch (IOException e) {
			throw new CheckoutException("The tap failed");
		}
	}

	public boolean usingCustomerBag() {
		return customerBag;
	}

	/**
	 * Use Case: Attendant empties the coin storage unit
	 */
	public void emptyCoinStorage() {
		checkoutStation.coinStorage.unload();
	}

	/**
	 * Use Case: Attendant empties the banknote storage unit
	 */
	public void emptyBanknoteStorage() {
		checkoutStation.banknoteStorage.unload();
	}

	/**
	 * Use Case: Attendant refills the coin dispenser
	 * 
	 * @param coins
	 *            The coins to be added. Any unloaded coins will be returned.
	 */
	public List<Coin> refillCoinDispenser(List<Coin> coins) {
		List<Coin> unloaded = new ArrayList<Coin>();

		for (Coin c : coins) {
			CoinDispenser dispenser = checkoutStation.coinDispensers.get(c.getValue());
			if (dispenser != null) {
				try {
					dispenser.load(c);
				} catch (SimulationException | OverloadException e) {
					unloaded.add(c);
				}
			} else {
				unloaded.add(c);
			}
		}
		return unloaded;
	}

	/**
	 * Use Case: Attendant refills the banknote dispenser
	 * 
	 * @param notes
	 *            The notes to be added. Any unloaded notes will be returned.
	 * @return any unloaded notes
	 */
	public List<Banknote> refillBanknoteDispenser(List<Banknote> notes) {
		List<Banknote> unloaded = new ArrayList<Banknote>();

		for (Banknote c : notes) {
			BanknoteDispenser dispenser = checkoutStation.banknoteDispensers.get(c.getValue());
			if (dispenser != null) {
				try {
					dispenser.load(c);
				} catch (SimulationException | OverloadException e) {
					unloaded.add(c);
				}
			} else {
				unloaded.add(c);
			}
		}
		return unloaded;
	}

	/**
	 * Use Case: Attendant adds paper to receipt printer
	 * 
	 * @param quantity
	 *            The amount of paper being added
	 */
	public void addPaper(int quantity) {
		checkoutStation.printer.addPaper(quantity);
		paperTotal += quantity;
	}

	/**
	 * Use Case: Attendant adds ink to receipt printer
	 * 
	 * @param quantity
	 *            The amount of ink being added
	 */
	public void addInk(int quantity) {
		checkoutStation.printer.addInk(quantity);
		inkTotal += quantity;
	}

	/**
	 * Use Case: Station detects that the ink in a receipt printer is low.
	 * 
	 * @return true, if the ink is low, false otherwise
	 */
	public boolean isInkLow() {
		return inkTotal < ReceiptPrinter.MAXIMUM_INK * 0.1;
	}

	/**
	 * Use Case: Station detects that the paper in a receipt printer is low.
	 * 
	 * @return true, if the paper is low, false otherwise
	 */
	public boolean isPaperLow() {
		return paperTotal < ReceiptPrinter.MAXIMUM_PAPER * 0.1;
	}

	// Blocks the station from being scanned
	protected void blockStation() {
		this.state = CheckoutState.Paused;
	}

	public String getState() {
		return this.state.toString();
	}

	public String getpState() {
		if (state == CheckoutState.Off) {
			return "Off";
		} else {
			return "On";
		}
	}

	public int getPaperTotal() {
		return this.paperTotal;
	}

	public int getInkTotal() {
		return this.inkTotal;
	}

	public int getCoinCount() {
		return checkoutStation.coinStorage.getCoinCount();
	}

	public int getNoteCount() {
		return checkoutStation.banknoteStorage.getBanknoteCount();
	}

	protected void shutDown() {
		state = CheckoutState.Off;
	}

	protected void powerOn() {
		state = CheckoutState.Scanning;
	}

	public void approveWeightDiscrepency() {
		expectedWeightOnBaggingArea = weightOnBaggingArea;
		this.state = CheckoutState.Scanning;
	}

	protected void registerAttendantSystem(AttendantSystem attendantSystem) {
		this.attendantSystem = attendantSystem;
	}

	protected AttendantSystem getAttendantSystem() {
		return attendantSystem;
	}

}
