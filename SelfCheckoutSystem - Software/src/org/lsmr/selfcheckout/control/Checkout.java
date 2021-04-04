/**
 * 
 */
package org.lsmr.selfcheckout.control;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Card;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.control.CardPayment.CardError;
import org.lsmr.selfcheckout.control.CardPayment.PaymentType;
import org.lsmr.selfcheckout.devices.DisabledException;
import org.lsmr.selfcheckout.devices.EmptyException;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.SimulationException;
import org.lsmr.selfcheckout.external.CardIssuer;

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
 * 5. Pay with the chosen paying method
 * <p>
 * 
 * @author Group U08-2
 * @date Mar 31, 2021
 */
public class Checkout {
	private enum CheckoutState {
		GivingChange, Paused, Paying_Cash, Paying_Credit, Paying_Debit, PrintingReceipt, Scanning
	};

	public enum PayingState {
		Cash, Credit, Debit
	};

	private BanknoteDispenserSlotListener bankNoteOutputListener;
	private CardIssuer cardIssuer;
	private CardPayment cardPayment;
	private SelfCheckoutStation checkoutStation;
	private BigDecimal currentBalance;
	private final double WEIGHT_TOLERANCE = 10;

	private boolean customerBag;

	private double expectedWeight = 0;

	private GiveChange giveChange;

	private ArrayList<Item> itemsScanned = new ArrayList<Item>(); //Wanna keep track of what was scanned

	private String loggedInMemberName = null;

	private String loggedInMemberNumber = null;

	private CheckoutState state = CheckoutState.Scanning;

	private double weightOnScale;

	public Checkout(SelfCheckoutStation checkoutStation) {
		if (checkoutStation == null) {
			throw new SimulationException(new NullPointerException("Argument may not be null."));
		}
		weightOnScale = 0;
		currentBalance = new BigDecimal(0);
		this.checkoutStation = checkoutStation;

		BarcodeScannerBalanceUpdateListener scannerListener = new BarcodeScannerBalanceUpdateListener(this);
		checkoutStation.mainScanner.register(scannerListener);
		checkoutStation.handheldScanner.register(scannerListener);

		BaggingAreaWeightUpdateListener weightListener = new BaggingAreaWeightUpdateListener(this);
		checkoutStation.baggingArea.register(weightListener);

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
			expectedWeight += weight;
			addItemToBaggingArea(cbag);
			customerBag = true;
		} else if (isPaused()) {
			throw new CheckoutException("Please add previously scanned items before adding a bag");
		} else {
			throw new CheckoutException("Cannot add bag outside scanning process");
		}

	}

	/**
	 * Adds an item to the bagging area. Resumes scanning state when customer puts
	 * item in bagging area that they previously did not.
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
		if (Double.isNaN(getWeightOnScale())) {
			throw new OverloadException("The scale is over weight");
		}
		if (isScanning() || isPaused()) {
			if (Math.abs(getWeightOnScale() - expectedWeight) <= WEIGHT_TOLERANCE) {
				state = CheckoutState.Scanning;
			} else {
				throw new CheckoutException("Weight on bagging area is incorrect (greater than " + WEIGHT_TOLERANCE
						+ " deviation from expected weight)");
			}
		}
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
	public double getWeightOnScale() {
		return weightOnScale;
	}

	/**
	 * Initializes the machine to prepare to pay with a credit card
	 * 
	 * @throws CheckoutException
	 *             if there is an issue initializing the payment machine
	 */
	private void initializeCreditPayment() throws CheckoutException {
		cardPayment.initialize(PaymentType.CREDIT, cardIssuer, currentBalance);

	}

	/**
	 * Initializes the machine to prepare to pay with a debit card
	 * 
	 * @throws CheckoutException
	 *             if there is an issue initializing the payment machine
	 */
	private void initializeDebitPayment() throws CheckoutException {
		cardPayment.initialize(PaymentType.DEBIT, cardIssuer, currentBalance);

	}

	/**
	 * Checks whether this checkout instance is in the paused state
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
	 * Logs a member in.
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
			this.loggedInMemberName = name;
			this.loggedInMemberNumber = number;
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

		Pair<CardError, String> p = cardPayment.insert(card, pin);

		// true if there are errors
		if (p != null) {
			CardError e = p.type;
			switch (e) {
			case CHIP_FAILURE:
				throw new CheckoutException("Error while reading the card's chip");
			case INVALID_CARD_TYPE:
				throw new CheckoutException(String.format("User chose %s payment but received %s",
						cardPayment.getPaymentType().toString(), p.value));
			case WRONG_PIN:
				throw new CheckoutException("Wrong pin inputted");
			case AUTHORIZATION_FAIL:
				throw new CheckoutException("Unable to authorize card.");

			default: // ex. there should never be a chip error while swiping
				throw new CheckoutException("Software error! Unknown issue with card payment");
			}
		} else {
			currentBalance = new BigDecimal("0.00");
		}
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

		Pair<CardError, String> p = cardPayment.swipe(card, signature);

		// true if there are errors
		if (p != null) {
			CardError e = p.type;
			switch (e) {
			case SWIPE_ERROR:
				throw new CheckoutException("Error while reading the card's magnetic strip");
			case INVALID_CARD_TYPE:
				throw new CheckoutException(String.format("User chose %s payment but received %s",
						cardPayment.getPaymentType().toString(), p.value));
			case AUTHORIZATION_FAIL:
				throw new CheckoutException("Unable to authorize card.");

			default: // ex. there should never be a chip error while swiping
				throw new CheckoutException("Software error! Unknown issue with card payment");
			}
		} else {
			currentBalance = new BigDecimal("0.00");
		}
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

		Pair<CardError, String> p = cardPayment.tap(card);

		// true if there are errors
		if (p != null) {
			CardError e = p.type;

			switch (e) {
			case CHIP_FAILURE:
				throw new CheckoutException("Error while reading the card's chip");
			case INVALID_CARD_TYPE:
				throw new CheckoutException(String.format("User chose %s payment but received %s",
						cardPayment.getPaymentType().toString(), p.value));
			case AUTHORIZATION_FAIL:
				throw new CheckoutException("Unable to authorize card.");
			case TAP_FAIL:
				throw new CheckoutException("Card is not tap enabled.");
			default: // ex. there should never be a chip error while swiping
				throw new CheckoutException("Software error! Unknown issue with card payment");
			}
		} else {
			currentBalance = new BigDecimal("0.00");
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
			if (Math.abs(getWeightOnScale() - expectedWeight) <= WEIGHT_TOLERANCE) {
				state = CheckoutState.Scanning;
			} else {
				state = CheckoutState.Paused;
			}
		} else if (isPaused()) {
			throw new CheckoutException("Previously scanned item has not been added to the bagging area");
		}
	}

	/**
	 * Sets the card issuer
	 * 
	 * @param issuer
	 */
	public void setCardIssuer(CardIssuer issuer) {
		cardIssuer = issuer;
	}

	/**
	 * Updates the weight of items in the bagging area.
	 * 
	 * @param weight
	 *            The new total weight of the items in the bagging area
	 */
	protected void setWeightOnScale(double weight) {
		weightOnScale = weight;
	}

	/**
	 * Updates the weight of items in the bagging area.
	 * 
	 * @param weight
	 *            The new total weight of the items in the bagging area
	 */
	protected void addExpectedWeightOnScale(double weight) {
		expectedWeight += weight;
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

}
