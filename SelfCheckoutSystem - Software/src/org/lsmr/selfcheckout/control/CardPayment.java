package org.lsmr.selfcheckout.control;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Set;

import org.lsmr.selfcheckout.Card;
import org.lsmr.selfcheckout.Card.CardData;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.external.CardIssuer;

public class CardPayment {

	public enum PaymentType {
		//names should be in lowercase
		DEBIT("debit"), CREDIT("credit"), GIFT("gift");

		private String name;

		PaymentType(String type) {
			this.name = type;
		}

		@Override
		public String toString() {
			return name;
		}

	}

	public enum CardError {
		WRONG_PIN, SWIPE_ERROR, INVALID_CARD_TYPE, CHIP_FAILURE, AUTHORIZATION_FAIL, TAP_FAIL
	}

	private boolean ready;
	private SelfCheckoutStation checkoutStation;
	private PaymentType type = null;
	private BigDecimal amount;

	public CardPayment(SelfCheckoutStation station) {
		checkoutStation = station;
		ready = false;
	}

	/**
	 * Prepares the machine to accept card payments
	 * 
	 * @param type
	 *            the payment method, either Credit or Debit
	 * @param cardIssuer
	 *            the company that issued the card
	 * @param amt
	 *            the balance to pay
	 * @throws CheckoutException
	 */
	public void initialize(PaymentType type, BigDecimal amt) throws CheckoutException {

		this.amount = amt;

		ready = true;
		checkoutStation.cardReader.enable();

		this.type = type;
	}

	/**
	 * Reset the state of the card payment
	 */
	public void reset() {
		ready = false;
	}

	/**
	 * Swipes a card on the card reader
	 * 
	 * @param card
	 *            the card to swipe
	 * @param signature
	 *            the customer's signature
	 * @return null if payment succeeded, otherwise a Pair instance of an error type
	 *         is returned.
	 */
	public CardError swipe(Card card, BufferedImage signature) {
		try {
			CardData data = checkoutStation.cardReader.swipe(card, signature);

			return attemptTransaction(data);
		} catch (IOException e) {
			return CardError.SWIPE_ERROR;
		}

	}

	/**
	 * Inserts a card on the card reader
	 * 
	 * @param card
	 *            the card to insert
	 * @param pin
	 *            the pin of the card
	 * @return null if payment succeeded, otherwise a Pair instance of an error type
	 *         is returned.
	 */
	public CardError insert(Card card, String pin) {
		try {
			CardData data = checkoutStation.cardReader.insert(card, pin);

			return attemptTransaction(data);

		} catch (IOException e) {
			return CardError.CHIP_FAILURE;
		}

	}

	/**
	 * Taps the card on the card reader
	 * 
	 * @param card
	 *            the card to tap
	 * @return null if payment succeeded, otherwise a Pair instance of an error type
	 *         is returned.
	 */
	public CardError tap(Card card) {
		// verify that the machine can read the card
		try {
			CardData data = checkoutStation.cardReader.tap(card);
			if (data != null) {
				return attemptTransaction(data);
			} else {
				return CardError.TAP_FAIL;
			}
		} catch (IOException e) {
			return CardError.CHIP_FAILURE;
		}

	}

	private CardError attemptTransaction(CardData data) {
		// user specified card is debit/credit but received credit/debit
		if (!data.getType().toLowerCase().equals(type.name)) {
			return CardError.INVALID_CARD_TYPE;
		}

		int holdNum = -1;
		Set<CardIssuer> cardIssuerDatabase;
		switch (type) {
		case DEBIT:
			cardIssuerDatabase = CardIssuerDatabase.DEBIT_ISSUER_DATABASE;
			break;
		case CREDIT:
			cardIssuerDatabase = CardIssuerDatabase.CREDIT_ISSUER_DATABASE;
			break;

		default:
			//last one left is Gift
			cardIssuerDatabase = CardIssuerDatabase.GIFT_ISSUER_DATABASE;
		}
		for (CardIssuer c : cardIssuerDatabase) {
			holdNum = c.authorizeHold(data.getNumber(), amount);
			if (holdNum != -1) {
				c.postTransaction(data.getNumber(), holdNum, amount);
				break;
			}
		}
		if (holdNum == -1) {
			return CardError.AUTHORIZATION_FAIL;
		}

		reset();
		return null;

	}

	/**
	 * Returns the card type the payment machine is expecting
	 * 
	 * @return the expected card type
	 */
	public PaymentType getPaymentType() {
		return type;
	}

	/**
	 * Checks if the machine is ready to accept card payments
	 * 
	 * @return true if the machine is initialized, false otherwise
	 */
	public boolean isReady() {
		return ready;
	}
}
